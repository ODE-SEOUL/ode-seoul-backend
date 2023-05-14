package kr.njw.odeseoul.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.location.application.LocationProvider;
import kr.njw.odeseoul.location.application.dto.FindSeoulGugunLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationProvider locationProvider;

    @Operation(summary = "서울시 자치구 목록", description = "서울시 자치구의 행정동코드, 위경도 정보 등이 필요할 때 사용")
    @GetMapping("/seoul/guguns")
    public ResponseEntity<BaseResponse<List<FindSeoulGugunLocationResponse>>> findSeoulGugunLocations() {
        return ResponseEntity.ok(new BaseResponse<>(this.locationProvider.findSeoulGugunLocations()));
    }
}
