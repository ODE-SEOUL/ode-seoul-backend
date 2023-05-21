package kr.njw.odeseoul.recruit.entity;

import jakarta.persistence.*;
import kr.njw.odeseoul.course.entity.Course;
import kr.njw.odeseoul.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recruit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Recruit {
    public static final int MAX_PEOPLE_INFINITY = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_user_id", nullable = false)
    @ToString.Exclude
    private User host;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruit")
    @ToString.Exclude
    private List<RecruitApplication> applications;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    private Course course;

    @Column(name = "course_id", nullable = false, insertable = false, updatable = false)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private RecruitCategory category;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500, nullable = false)
    private String image;

    @Column(nullable = false)
    private int currentPeople;

    @Column(nullable = false)
    private int maxPeople;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM")
    private RecruitProgressStatus progressStatus;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public enum RecruitCategory {
        COM_ANIMAL,
        COM_HOUSE,
        COM_OFFICE,
        COM_NEIGHBOR,
        COM_EXERCISE,
        COM_PHOTO,
        COM_EXPER
    }

    public enum RecruitProgressStatus {
        OPEN,
        CLOSED,
        DONE
    }
}
