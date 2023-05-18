package kr.njw.odeseoul.event.application.dto;

import kr.njw.odeseoul.event.entity.Event;
import lombok.Data;

@Data
public class SearchEventsRequest {
    private Event.EventCategory category;
    private String gugunCode;
    private String titleContains;
    private int pageNumber;
    private int pagingSize;
}
