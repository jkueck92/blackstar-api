package de.jkueck.blackstar.api.web;

import de.jkueck.CheckPostDto;
import de.jkueck.PostDto;
import de.jkueck.WordpressCreatePostDto;
import de.jkueck.blackstar.api.service.WordpressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final WordpressService wordpressService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createWordpressPost(@RequestBody WordpressCreatePostDto createPostDto) {
        this.wordpressService.save(createPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/notApproved")
    public ResponseEntity<List<PostDto>> getNotApprovedPosts() {
        return ResponseEntity.ok(this.wordpressService.getNotApprovedPosts());
    }

    @PostMapping(value = "/approved")
    public ResponseEntity<Object> approvePost(@RequestBody CheckPostDto checkPostDto) {
        this.wordpressService.approvePost(checkPostDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping(value = "/{id}/published")
    public ResponseEntity<Object> publishPost(@PathVariable(name = "id") Long id) {
        this.wordpressService.publishPost(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts() {
        return ResponseEntity.ok(this.wordpressService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.wordpressService.getById(id));
    }

    @GetMapping("/filter/{filter}")
    public ResponseEntity<List<PostDto>> getFilteredPosts(@PathVariable(name = "filter") String filter) {
        return ResponseEntity.ok(this.wordpressService.getFilteredPosts(filter));
    }

}
