package de.jkueck.blackstar.api.web;

import de.jkueck.UserDto;
import de.jkueck.UserSettingDto;
import de.jkueck.blackstar.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(name = "email") String email) {
        Optional<UserDto> optionalUserDto = this.userService.getUserByEmail(email);
        return optionalUserDto.map(userDto -> ResponseEntity.status(HttpStatus.OK).body(userDto)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/users/{id}/settings")
    public ResponseEntity<List<UserSettingDto>> getUserSettings(@PathVariable(name = "id") long id) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> update(@RequestBody UserDto update) {
        Optional<UserDto> optionalUserDto = this.userService.updateUserInformation(update);
        return optionalUserDto.map(userDto -> ResponseEntity.status(HttpStatus.ACCEPTED).body(userDto)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
