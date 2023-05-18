package kr.njw.odeseoul.event.repository.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class SearchEventsRepoRequest {
    private List<String> codenamesOneOf;
    private List<String> gunamesOneOf;
    private String titleContains;
    private Pageable pageable;
}
