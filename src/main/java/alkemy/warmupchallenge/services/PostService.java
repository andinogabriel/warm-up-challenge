package alkemy.warmupchallenge.services;

import alkemy.warmupchallenge.dtos.PostCreationDto;
import alkemy.warmupchallenge.dtos.PostDto;
import alkemy.warmupchallenge.dtos.PostToShowDto;
import alkemy.warmupchallenge.entities.CategoryEntity;
import alkemy.warmupchallenge.entities.PostEntity;
import alkemy.warmupchallenge.entities.UserEntity;
import alkemy.warmupchallenge.repositories.CategoryRepository;
import alkemy.warmupchallenge.repositories.PostRepository;
import alkemy.warmupchallenge.repositories.UserRepository;
import alkemy.warmupchallenge.utils.BucketName;
import alkemy.warmupchallenge.utils.PatchHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.json.JsonPatch;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UploadImageService imageService;

    @Autowired
    FileStore fileStore;

    @Autowired
    ModelMapper mapper;

    @Autowired
    PatchHelper patchHelper;

    public PostDto createPost(PostCreationDto postCreationDto) {
        CategoryEntity categoryEntity = getCategory(postCreationDto.getCategory());
        UserEntity userEntity = userRepository.findByEmail(postCreationDto.getUser()).get();

        PostEntity postEntity = PostEntity.builder()
                .title(postCreationDto.getTitle())
                .body(postCreationDto.getBody())
                .category(categoryEntity)
                .user(userEntity)
                .build();

        return mapper.map(postRepository.save(postEntity), PostDto.class);
    }

    //https://cassiomolin.com/2019/06/10/using-http-patch-in-spring/
    public PostDto updatePost(long id, JsonPatch patchDocument) {
        PostEntity postEntity = getPost(id);
        PostDto postDto = mapper.map(postEntity, PostDto.class);
        PostDto postPatched = patchHelper.patch(patchDocument, postDto, PostDto.class);
        PostEntity postEntityPatched = mapper.map(postPatched, PostEntity.class);
        return mapper.map(postRepository.save(postEntityPatched), PostDto.class);
    }

    public void deletePost(long id) {
        PostEntity postEntity = getPost(id);
        postRepository.delete(postEntity);
    }

    public PostDto getPostById(long id) {
        return mapper.map(getPostById(id), PostDto.class);
    }

    public Page<PostDto> getAllPosts(int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<PostToShowDto> postEntities = postRepository.findAllProjectedBy(pageable);
        return mapper.map(postEntities, Page.class);
    }

    public Page<PostDto> getPostsByTitle(String title, int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<PostToShowDto> postEntities = postRepository.findByTitleContaining(pageable, title);
        return mapper.map(postEntities, Page.class);
    }

    public Page<PostDto> getPostsByCategory(long categoryId, int page, int limit, String sortBy, String sortDir) {
        CategoryEntity categoryEntity = getCategory(categoryId);

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<PostToShowDto> postEntities = postRepository.findByCategory(pageable, categoryEntity);
        return mapper.map(postEntities, Page.class);
    }

    public Page<PostDto> getPostsByCategoryAndTitle(long categoryId, String title, int page, int limit, String sortBy, String sortDir) {
        CategoryEntity categoryEntity = getCategory(categoryId);

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<PostToShowDto> postEntities = postRepository.findByTitleContainingAndCategory(pageable, title, categoryEntity);
        return mapper.map(postEntities, Page.class);
    }

    public void uploadPostImage(long id, MultipartFile file) {
        //1. Check if image isn't empty
        imageService.fileIsEmpty(file);

        //2. Check if the file is a valid image
        imageService.isAnImage(file);

        //3. Check if character exists in the DB
        PostEntity postEntity = getPost(id);

        //4. Save file metadata
        Map<String, String> metadata = imageService.extractMetadata(file);

        //5. Store the image in s3 and update Character image link with the image link from s3
        //Path -> bucket name + folder name, the folder name gonna be equal to character id - character name
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), postEntity.getId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            postEntity.setImageLink(filename);
            postRepository.save(postEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadPostImage(long id) {
        PostEntity postEntity = getPost(id);
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), postEntity.getId());
        return fileStore.download(path, postEntity.getImageLink());
    }


    private PostEntity getPost(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post doesn't exist."));
    }

    private CategoryEntity getCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category doesn't exist."));
    }


    /*
        //www.baeldung.com/spring-rest-json-patch
        public PostDto updatePost(long id, JsonPatch patch) {
            try {
                PostEntity postEntity = getPost(id);
                PostEntity postPatched = applyPatchToPost(patch, postEntity);
                postRepository.save(postPatched);
                return mapper.map(postRepository.save(postPatched), PostDto.class);
            } catch (JsonPatchException | JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }


        private PostEntity applyPatchToPost(
                    JsonPatch patch, PostEntity targetPost) throws JsonPatchException, JsonProcessingException {
                JsonNode patched = patch.apply(objectMapper.convertValue(targetPost, JsonNode.class));
                return objectMapper.treeToValue(patched, PostEntity.class);
        }
     */


}
