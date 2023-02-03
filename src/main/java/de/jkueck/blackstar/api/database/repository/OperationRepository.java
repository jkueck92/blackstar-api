package de.jkueck.blackstar.api.database.repository;

import de.jkueck.blackstar.api.database.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
