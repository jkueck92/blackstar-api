package de.jkueck.blackstar.api.database.repository;

import de.jkueck.blackstar.api.database.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(final String name);

}
