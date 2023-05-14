package kr.njw.odeseoul.location.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "location")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Location {
    @Id
    @Column(length = 45, nullable = false)
    private String code;

    @Column(length = 45, nullable = false)
    private String address1;

    @Column(length = 45, nullable = false)
    private String address2;

    @Column(length = 45, nullable = false)
    private String address3;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private boolean seoulGugun;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;
}
