package kr.njw.odeseoul.location.application;

import kr.njw.odeseoul.location.application.dto.FindSeoulGugunLocationResponse;
import kr.njw.odeseoul.location.entity.Location;
import kr.njw.odeseoul.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LocationProviderImpl implements LocationProvider {
    private final LocationRepository locationRepository;

    public List<FindSeoulGugunLocationResponse> findSeoulGugunLocations() {
        List<Location> locations = this.locationRepository.findAllBySeoulGugunAndDeletedAtIsNullOrderByCode(true);

        return locations.stream().map(location -> {
            FindSeoulGugunLocationResponse response = new FindSeoulGugunLocationResponse();
            response.setCode(location.getCode());
            response.setName(location.getAddress2());
            response.setLatitude(location.getLatitude());
            response.setLongitude(location.getLongitude());
            return response;
        }).collect(Collectors.toList());
    }
}
