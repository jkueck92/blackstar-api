package de.jkueck.blackstar.api.database.repository;

import de.jkueck.blackstar.api.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(final String email);

}
