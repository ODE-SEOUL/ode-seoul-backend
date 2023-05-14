package kr.njw.odeseoul.user.entity;

import jakarta.persistence.*;
import kr.njw.odeseoul.common.security.Role;
import kr.njw.odeseoul.location.entity.Location;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String nickname;

    @Column(length = 500, nullable = false)
    private String profileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_code")
    @ToString.Exclude
    private Location location;

    @Column(length = 45, nullable = false)
    private String loginId;

    @Column(length = 500, nullable = false)
    private String loginPw;

    @Column(length = 500, nullable = false)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM")
    private UserSignupStatus signupStatus;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public enum UserSignupStatus {
        BEFORE_REGISTERED,
        REGISTERED
    }

    public void renewRefreshToken() {
        this.refreshToken = RandomStringUtils.randomAlphanumeric(32);
    }

    public void signup(String nickname, Location location) {
        this.nickname = StringUtils.trimToEmpty(nickname);
        this.location = location;
        this.signupStatus = UserSignupStatus.REGISTERED;
    }

    public Role getRole() {
        return (this.signupStatus == UserSignupStatus.BEFORE_REGISTERED) ? Role.GUEST : Role.USER;
    }
}
