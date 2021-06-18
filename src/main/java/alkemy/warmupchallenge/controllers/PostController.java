package alkemy.warmupchallenge.controllers;

import alkemy.warmupchallenge.dtos.PostCreationDto;
import alkemy.warmupchallenge.dtos.PostDto;
import alkemy.warmupchallenge.models.requests.PostRequest;
import alkemy.warmupchallenge.models.responses.OperationStatusModel;
import alkemy.warmupchallenge.models.responses.PostResponse;
import alkemy.warmupchallenge.services.PostService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.json.JsonPatch;
import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    private EntityManager entityManager;

    @PostMapping
    public PostResponse createPost(@Valid @RequestBody PostRequest postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        PostCreationDto postCreationDto = mapper.map(postRequest, PostCreationDto.class);
        postCreationDto.setUser(email);
        PostDto postDto = postService.createPost(postCreationDto);
        return mapper.map(postDto, PostResponse.class);
    }

    @PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadPostImage(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) {
        postService.uploadPostImage(id, file);
    }

    @GetMapping
    public Page<PostResponse> getPosts(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "category", required = false) Long category, @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value="limit", defaultValue = "5") int limit, @RequestParam(value = "sortBy", defaultValue = "creationDate") String sortBy, @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        Page<PostDto> posts = null;
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedPostFilter");
        filter.setParameter("isDeleted", isDeleted);

        if(title != null && category != null) {
            long categoryId = category.longValue();
            posts = postService.getPostsByCategoryAndTitle(categoryId, title, page, limit, sortBy, sortDir);
        } else if (title != null && category == null) {
            posts = postService.getPostsByTitle(title, page, limit, sortBy, sortDir);
        } else if (title == null && category != null) {
            posts = postService.getPostsByCategory(category, page, limit, sortBy, sortDir);
        } else {
            posts = postService.getAllPosts(page, limit, sortBy, sortDir);
        }

        session.disableFilter("deletedPostFilter");
        return mapper.map(posts, Page.class);
    }

    @GetMapping(path = "/{id}")
    public PostResponse getPost(@PathVariable long id) {
        return mapper.map(postService.getPostById(id), PostResponse.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public PostResponse updatePost(@PathVariable long id, @RequestBody JsonPatch patch) {
        return mapper.map(postService.updatePost(id, patch), PostResponse.class);
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deletePost(@PathVariable long id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setName("DELETE");
        postService.deletePost(id);
        operationStatusModel.setResult("SUCCESS");
        return operationStatusModel;
    }


}
