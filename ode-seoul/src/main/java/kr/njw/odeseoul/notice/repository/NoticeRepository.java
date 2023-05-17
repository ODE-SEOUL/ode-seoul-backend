package kr.njw.odeseoul.notice.repository;

import kr.njw.odeseoul.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByDeletedAtIsNullOrderByIdDesc(Pageable pageable);
}
