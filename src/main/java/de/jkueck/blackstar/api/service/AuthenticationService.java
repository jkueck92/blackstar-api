package de.jkueck.blackstar.api.service;

import de.jkueck.*;
import de.jkueck.blackstar.api.database.entity.Permission;
import de.jkueck.blackstar.api.database.entity.Role;
import de.jkueck.blackstar.api.database.entity.User;
import de.jkueck.blackstar.api.database.repository.PermissionRepository;
import de.jkueck.blackstar.api.database.repository.RoleRepository;
import de.jkueck.blackstar.api.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements Serializable {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public Optional<UserDto> login(LoginDto loginDto) {
        User user = this.userRepository.findByEmail(loginDto.getEmail());
        if (user != null) {
            if (this.passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                UserDto userDto = this.convert(user);
                userDto.setRole(this.convertRoles(user));
                return Optional.of(userDto);
            }
        }
        return Optional.empty();
    }

    public PermissionDto createOrUpdatePermission(Permissions permission) {
        Permission p = this.permissionRepository.findByName(permission.name());
        if (p == null) {
            p = this.permissionRepository.save(Permission.builder().name(permission.name()).build());
        }
        return PermissionDto.builder().id(p.getId()).name(p.getName()).build();
    }

    public RoleDto createOrUpdateRole(Roles role) {
        Role r = this.roleRepository.findByName(role.name());
        if (r == null) {
            r = this.roleRepository.save(Role.builder().name(role.name()).build());
        }
        return RoleDto.builder().id(r.getId()).name(r.getName()).build();
    }

    public Optional<RoleDto> getRoleByName(String name) {
        Role role = this.roleRepository.findByName(name);
        if (role != null) {
            return Optional.of(RoleDto.builder().id(role.getId()).name(role.getName()).build());
        }
        return Optional.empty();
    }

    public Optional<PermissionDto> getPermissionByName(String name) {
        Permission permission = this.permissionRepository.findByName(name);
        if (permission != null) {
            return Optional.of(PermissionDto.builder().id(permission.getId()).name(permission.getName()).build());
        }
        return Optional.empty();
    }

    @Transactional
    public void addPermissionToRole(PermissionDto permission, RoleDto role) {
        Role r = this.roleRepository.findByName(role.getName());
        if (r != null) {
            Permission p = this.permissionRepository.findByName(permission.getName());
            if (p != null) {
                r.getPermissions().add(p);
                this.roleRepository.save(r);
            }
        }
    }

    private final PasswordEncoder passwordEncoder;

    public Optional<UserDto> register(UserRegisterDto registerDto) {
        User user = this.userRepository.findByEmail(registerDto.getEmail());
        if (user == null) {
            Optional<RoleDto> optionalRoleDto = this.getRoleByName(registerDto.getRole());
            if (optionalRoleDto.isPresent()) {
                Role role = this.roleRepository.findByName(optionalRoleDto.get().getName());
                User tmpUser = User.builder()
                        .role(role)
                        .firstName(registerDto.getFirstname())
                        .lastName(registerDto.getLastname())
                        .email(registerDto.getEmail())
                        .password(this.passwordEncoder.encode(registerDto.getPassword()))
                        .build();
                User newStoredUser = this.userRepository.save(tmpUser);
                return Optional.of(UserDto.builder()
                        .id(newStoredUser.getId())
                        .email(newStoredUser.getEmail())
                        .firstname(newStoredUser.getFirstName())
                        .lastname(newStoredUser.getLastName())
                        .role(this.convertRoles(newStoredUser))
                        .build());
            }
        }
        return Optional.empty();
    }

    private RoleDto convertRoles(User user) {
        RoleDto roleDto = RoleDto.builder().id(user.getRole().getId()).name(user.getRole().getName()).build();
        for (Permission permission : user.getRole().getPermissions()) {
            roleDto.addPermission(PermissionDto.builder().id(permission.getId()).name(permission.getName()).build());
        }
        return roleDto;
    }

    private UserDto convert(User user) {
        return UserDto.builder().id(user.getId()).firstname(user.getFirstName()).lastname(user.getLastName()).email(user.getEmail()).build();
    }

}
