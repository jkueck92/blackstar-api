package de.jkueck.blackstar.api.service;

import de.jkueck.UserDto;
import de.jkueck.UserSettingDto;
import de.jkueck.blackstar.api.database.entity.UserSetting;
import de.jkueck.blackstar.api.database.repository.UserRepository;
import de.jkueck.blackstar.api.database.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements Serializable {

    private final UserRepository userRepository;

    public Optional<UserDto> updateUserInformation(UserDto updatedUserDto) {
        Optional<User> optionalUser = this.userRepository.findById(updatedUserDto.getId());
        if (optionalUser.isPresent()) {

            User oldUser = optionalUser.get();

            oldUser.setFirstName(updatedUserDto.getFirstname());
            oldUser.setLastName(updatedUserDto.getLastname());
            oldUser.setEmail(updatedUserDto.getEmail());

            User updatedUser = this.userRepository.save(oldUser);

            return Optional.of(this.convert(updatedUser));
        }
        return Optional.empty();
    }

    public Optional<UserDto> getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            return Optional.of(this.convert(user));
        }
        return Optional.empty();
    }

    public Optional<List<UserSettingDto>> getSettingsByUser(long id) {
        return Optional.empty();
    }

    private UserDto convert(User user) {
        return UserDto.builder().id(user.getId()).firstname(user.getFirstName()).lastname(user.getLastName()).email(user.getEmail()).build();
    }

}
