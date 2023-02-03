package de.jkueck.blackstar.api.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Permission extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

}
