package de.jkueck.blackstar.api.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Operation extends BaseEntity {

    private String keyword;

    private String keywordText;

    private String location;

    private LocalDateTime timestamp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
