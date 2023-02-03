package de.jkueck.blackstar.api.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_settings")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserSetting extends BaseEntity {

    private String property;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
