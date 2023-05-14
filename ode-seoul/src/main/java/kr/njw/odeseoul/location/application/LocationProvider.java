package kr.njw.odeseoul.location.application;

import kr.njw.odeseoul.location.application.dto.FindSeoulGugunLocationResponse;

import java.util.List;

public interface LocationProvider {
    List<FindSeoulGugunLocationResponse> findSeoulGugunLocations();
}
