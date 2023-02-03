package de.jkueck.blackstar.api.service;

import de.jkueck.OperationDto;
import de.jkueck.blackstar.api.domain.OperationCreateDto;
import de.jkueck.blackstar.api.database.repository.OperationRepository;
import de.jkueck.blackstar.api.database.entity.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;

    public Optional<List<OperationDto>> getAll() {
        List<Operation> dbList = this.operationRepository.findAll();
        if (!dbList.isEmpty()) {
            List<OperationDto> list = new ArrayList<>();
            for (Operation operation : this.operationRepository.findAll()) {
                list.add(this.convert(operation));
            }
            return Optional.of(list);
        }
       return Optional.empty();
    }

    public Optional<OperationDto> getById(long id) {
        Optional<Operation> optionalOperation = this.operationRepository.findById(id);
        return optionalOperation.map(this::convert);
    }

    public Optional<Operation> getRawById(long id) {
        return this.operationRepository.findById(id);
    }

    public Optional<OperationDto> save(OperationCreateDto operationCreateDto) {
        try {
            Operation operation = Operation.builder()
                    .keyword(operationCreateDto.getKeyword())
                    .keywordText(operationCreateDto.getKeywordText())
                    .location(operationCreateDto.getLocation())
                    .timestamp(operationCreateDto.getTimestamp())
                    .build();
            operation = this.operationRepository.save(operation);
            return Optional.of(this.convert(operation));
        } catch (Exception e) {
            log.warn("error while save operation: " + e);
        }
        return Optional.empty();
    }

    private OperationDto convert(Operation db) {
        return OperationDto.builder()
                .id(db.getId())
                .keyword(db.getKeyword())
                .keywordText(db.getKeywordText())
                .location(db.getLocation())
                .timestamp(db.getTimestamp())
                .hasPost(db.getPost() != null ? Boolean.TRUE : Boolean.FALSE)
                .build();
    }

}
