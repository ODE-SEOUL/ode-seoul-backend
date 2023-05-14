package kr.njw.odeseoul.user.repository;

import kr.njw.odeseoul.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    Optional<User> findByLoginIdAndDeletedAtIsNull(String loginId);

    Optional<User> findByRefreshTokenAndDeletedAtIsNull(String refreshToken);
}
