package kr.njw.odeseoul.event.repository;

import kr.njw.odeseoul.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
}
