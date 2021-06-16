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
import alkemy.warmupchallenge.utils.PatchHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.json.JsonPatch;

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
    ModelMapper mapper;

    @Autowired
    PatchHelper patchHelper;

    public PostDto createPost(PostCreationDto postCreationDto) {
        CategoryEntity categoryEntity = getCategory(postCreationDto.getCategory());
        UserEntity userEntity = userRepository.findByEmail(postCreationDto.getUser()).get();

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postCreationDto.getTitle());
        postEntity.setBody(postCreationDto.getBody());
        postEntity.setCategory(categoryEntity);
        postEntity.setUser(userEntity);
        postEntity.setImage(postCreationDto.getImage());

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

    public Page<PostDto> getAllPosts(int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<PostToShowDto> postEntities = postRepository.findAllByOrderByCreationDate(pageable);
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
