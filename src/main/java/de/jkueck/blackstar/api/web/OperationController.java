package de.jkueck.blackstar.api.web;

import de.jkueck.OperationDto;
import de.jkueck.blackstar.api.domain.OperationCreateDto;
import de.jkueck.blackstar.api.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operations")
public class OperationController {

    private final OperationService operationService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createNewOperation(@RequestBody OperationCreateDto operationCreateDto) {
        Optional<OperationDto> optionalOperationDto = this.operationService.save(operationCreateDto);
        if (optionalOperationDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(operationCreateDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OperationDto>> getOperations() {
        Optional<List<OperationDto>> optionalOperationDtos = this.operationService.getAll();
        return optionalOperationDtos.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationDto> getOperationById(@PathVariable(name = "id") Long id) {
        Optional<OperationDto> optionalOperationDto = this.operationService.getById(id);
        return optionalOperationDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
