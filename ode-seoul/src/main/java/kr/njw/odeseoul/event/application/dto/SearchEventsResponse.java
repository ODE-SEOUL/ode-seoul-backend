package kr.njw.odeseoul.event.application.dto;

import kr.njw.odeseoul.common.dto.PageResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchEventsResponse extends PageResponse {
    private List<FindEventResponse> events;
}
