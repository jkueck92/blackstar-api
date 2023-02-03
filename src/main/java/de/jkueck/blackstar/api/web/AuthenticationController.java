package de.jkueck.blackstar.api.web;

import de.jkueck.LoginDto;
import de.jkueck.UserDto;
import de.jkueck.UserRegisterDto;
import de.jkueck.blackstar.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        Optional<UserDto> optionalUserDto = this.authenticationService.login(loginDto);
        return optionalUserDto.map(userDto -> ResponseEntity.status(HttpStatus.OK).body(userDto)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto registerDto) {
        Optional<UserDto> optionalUserDto = this.authenticationService.register(registerDto);
        return optionalUserDto.map(userDto -> ResponseEntity.status(HttpStatus.OK).body(userDto)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
