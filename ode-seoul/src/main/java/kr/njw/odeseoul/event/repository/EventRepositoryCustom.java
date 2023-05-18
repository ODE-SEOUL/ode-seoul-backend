package kr.njw.odeseoul.event.repository;

import kr.njw.odeseoul.event.entity.Event;
import kr.njw.odeseoul.event.repository.dto.SearchEventsRepoRequest;
import org.springframework.data.domain.Page;

public interface EventRepositoryCustom {
    Page<Event> search(SearchEventsRepoRequest request);
}
