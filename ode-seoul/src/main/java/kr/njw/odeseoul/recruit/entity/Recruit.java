package kr.njw.odeseoul.recruit.entity;

import jakarta.persistence.*;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.course.entity.Course;
import kr.njw.odeseoul.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    public boolean isFull() {
        return (this.maxPeople != MAX_PEOPLE_INFINITY) && (this.currentPeople >= this.maxPeople);
    }

    public boolean isMemberEmpty() {
        return (this.currentPeople <= 1);
    }

    public void changeProgress(RecruitProgressStatus nextProgressStatus) {
        switch (this.progressStatus) {
            case OPEN -> {
                if (nextProgressStatus != RecruitProgressStatus.CLOSED && nextProgressStatus != RecruitProgressStatus.DONE) {
                    throw new BaseException(BaseResponseStatus.CHANGE_RECRUIT_PROGRESS_ERROR_ALLOW_CLOSED_OR_DONE);
                }
            }
            case CLOSED -> {
                if (nextProgressStatus != RecruitProgressStatus.DONE) {
                    throw new BaseException(BaseResponseStatus.CHANGE_RECRUIT_PROGRESS_ERROR_ALLOW_DONE);
                }
            }
            case DONE -> throw new BaseException(BaseResponseStatus.CHANGE_RECRUIT_PROGRESS_ERROR_ALREADY_DONE);
        }

        this.progressStatus = nextProgressStatus;
    }

    public void increaseMember() {
        if (this.isFull()) {
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }

        this.currentPeople++;
    }

    public void decreaseMember() {
        if (this.isMemberEmpty()) {
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }

        this.currentPeople--;
    }
}
