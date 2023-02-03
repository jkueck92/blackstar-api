package de.jkueck.blackstar.api.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Post extends BaseEntity {

    private String title;

    private String keyword;

    private String location;

    private String text;

    private String alarmTimestamp;

    private LocalDateTime plannedReleaseAt;

    private String status;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post")
    private Operation operation;

}
