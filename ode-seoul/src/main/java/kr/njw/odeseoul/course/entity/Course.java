package kr.njw.odeseoul.course.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.MultiLineString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Course {
    @Id
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int distance;

    @Column(nullable = false)
    private int time;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(nullable = false, columnDefinition = "JSON")
    private String categories;

    @Column(length = 45, nullable = false)
    private String gugunSummary;

    @Column(length = 500, nullable = false)
    private String routeSummary;

    @Column(length = 45, nullable = false)
    private String nearSubway;

    @Column(length = 500, nullable = false)
    private String accessWay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM")
    private CourseRegion region;

    @Column(length = 500, nullable = false)
    private String image;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Where(clause = "deleted_at IS NULL")
    @OrderBy("locationCode ASC")
    @ToString.Exclude
    private List<CourseGugun> guguns;

    @Column
    private MultiLineString route;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public enum CourseRegion {
        NORTH,
        SOUTH
    }
}
