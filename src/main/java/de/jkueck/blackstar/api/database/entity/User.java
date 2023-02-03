package de.jkueck.blackstar.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<UserSetting> userSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

}
