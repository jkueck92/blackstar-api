package de.jkueck.blackstar.api.database.repository;

import de.jkueck.blackstar.api.database.entity.User;
import de.jkueck.blackstar.api.database.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {

    List<UserSetting> findAllByUser(User user);

}
