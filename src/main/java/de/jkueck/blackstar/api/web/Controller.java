package de.jkueck.blackstar.api.web;

import de.jkueck.*;
import de.jkueck.blackstar.api.domain.OperationCreateDto;
import de.jkueck.blackstar.api.service.AuthenticationService;
import de.jkueck.blackstar.api.service.OperationService;
import de.jkueck.blackstar.api.service.UserService;
import de.jkueck.blackstar.api.service.WordpressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Controller {

    private final WordpressService wordpressService;

    @GetMapping("/tags")
    public ResponseEntity<List<WordpressTagDto>> getWordpressTags() {
        return ResponseEntity.ok(this.wordpressService.getTags());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<WordpressCategoryDto>> getWordpressCategories() {
        return ResponseEntity.ok(this.wordpressService.getCategories());
    }

}
