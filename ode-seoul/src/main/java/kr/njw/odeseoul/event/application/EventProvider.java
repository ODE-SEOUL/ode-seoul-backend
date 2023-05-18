package kr.njw.odeseoul.event.application;

import kr.njw.odeseoul.event.application.dto.SearchEventsRequest;
import kr.njw.odeseoul.event.application.dto.SearchEventsResponse;

public interface EventProvider {
    SearchEventsResponse searchEvents(SearchEventsRequest request);
}
