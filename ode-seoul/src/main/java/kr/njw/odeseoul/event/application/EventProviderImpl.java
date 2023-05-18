package kr.njw.odeseoul.event.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.event.application.dto.FindEventResponse;
import kr.njw.odeseoul.event.application.dto.SearchEventsRequest;
import kr.njw.odeseoul.event.application.dto.SearchEventsResponse;
import kr.njw.odeseoul.event.entity.Event;
import kr.njw.odeseoul.event.repository.EventRepository;
import kr.njw.odeseoul.event.repository.dto.SearchEventsRepoRequest;
import kr.njw.odeseoul.location.entity.Location;
import kr.njw.odeseoul.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventProviderImpl implements EventProvider {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public SearchEventsResponse searchEvents(SearchEventsRequest request) {
        int maxPagingSize = 100;

        SearchEventsRepoRequest repoRequest = new SearchEventsRepoRequest();

        if (request.getCategory() != null) {
            repoRequest.setCodenamesOneOf(Event.getCodenamesOfCategory(request.getCategory()));
        }

        if (StringUtils.isNotBlank(request.getGugunCode())) {
            Location location = this.locationRepository.findByCodeAndDeletedAtIsNull(request.getGugunCode()).orElse(null);

            if (location == null || StringUtils.isBlank(location.getAddress2())) {
                throw new BaseException(BaseResponseStatus.SEARCH_EVENTS_ERROR_BAD_GUGUN);
            }

            repoRequest.setGunamesOneOf(List.of(location.getAddress2()));
        }

        if (StringUtils.isNotBlank(request.getTitleContains())) {
            repoRequest.setTitleContains(StringUtils.trimToEmpty(request.getTitleContains()));
        }

        repoRequest.setPageable(PageRequest.of(request.getPageNumber() - 1, Math.min(request.getPagingSize(), maxPagingSize)));

        Page<Event> eventPage = this.eventRepository.search(repoRequest);

        SearchEventsResponse response = new SearchEventsResponse();
        response.setPage(eventPage.getNumber() + 1);
        response.setPageSize(eventPage.getNumberOfElements());
        response.setPagingSize(eventPage.getSize());
        response.setTotalPage(eventPage.getTotalPages());
        response.setTotalSize(eventPage.getTotalElements());
        response.setEvents(eventPage.getContent().stream().map(event -> {
            FindEventResponse eventResponse = new FindEventResponse();
            eventResponse.setUuid(event.getUuid());
            eventResponse.setCategory(event.getCategory());
            eventResponse.setCodename(event.getCodename());
            eventResponse.setGuname(event.getGuname());
            eventResponse.setTitle(event.getTitle());
            eventResponse.setPlace(event.getPlace());
            eventResponse.setUseTarget(event.getUseTarget());
            eventResponse.setUseFee(event.getUseFee());
            eventResponse.setOrgLink(event.getOrgLink());
            eventResponse.setMainImage(event.getMainImage());
            eventResponse.setStartDate(event.getStartDate());
            eventResponse.setEndDate(event.getEndDate());
            return eventResponse;
        }).collect(Collectors.toList()));

        return response;
    }
}
