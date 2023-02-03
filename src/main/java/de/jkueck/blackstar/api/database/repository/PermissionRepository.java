package de.jkueck.blackstar.api.database.repository;

import de.jkueck.blackstar.api.database.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(final String name);

}
