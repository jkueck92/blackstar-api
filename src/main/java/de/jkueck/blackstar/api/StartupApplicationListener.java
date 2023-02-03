package de.jkueck.blackstar.api;

import de.jkueck.PermissionDto;
import de.jkueck.Permissions;
import de.jkueck.RoleDto;
import de.jkueck.Roles;
import de.jkueck.blackstar.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AuthenticationService authenticationService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (Permissions permission : Permissions.values()) {
            this.authenticationService.createOrUpdatePermission(permission);
        }

        for (Roles role : Roles.values()) {
            this.authenticationService.createOrUpdateRole(role);
        }

        Optional<RoleDto> optionalAdminRoleDto = this.authenticationService.getRoleByName(Roles.ADMIN.name());
        if (optionalAdminRoleDto.isPresent()) {
            for (Permissions permission : Permissions.values()) {
                this.addPermissionToRole(optionalAdminRoleDto.get(), permission);
            }
        }

        Optional<RoleDto> optionalUserRoleDto = this.authenticationService.getRoleByName(Roles.USER.name());
        if (optionalUserRoleDto.isPresent()) {
            this.addPermissionToRole(optionalUserRoleDto.get(), Permissions.MENU_PROFILE);
            this.addPermissionToRole(optionalUserRoleDto.get(), Permissions.MENU_PROFILE);
            this.addPermissionToRole(optionalUserRoleDto.get(), Permissions.POSTS_CHECK_POST);
        }

    }

    private void addPermissionToRole(RoleDto roleDto, Permissions permission) {
        Optional<PermissionDto> optionalPermissionDto = this.authenticationService.getPermissionByName(permission.name());
        optionalPermissionDto.ifPresent(permissionDto -> this.authenticationService.addPermissionToRole(permissionDto, roleDto));
    }

}
