package kr.njw.odeseoul.course.application.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.njw.odeseoul.course.entity.Course;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FindCourseResponse {
    public static final String CATEGORY_LEVEL_BASIC = "LEVEL_BASIC";
    public static final String CATEGORY_LEVEL_ADVANCED = "LEVEL_ADVANCED";
    public static final String CATEGORY_LEVEL_PRO = "LEVEL_PRO";

    @Schema(example = "1")
    private Long id;
    @Schema(example = "강서문화 산책길")
    private String name;
    @Schema(description = "레벨 (1: 초급, 2: 중급, 3: 고급)", example = "1")
    private int level;
    @Schema(description = "길이 (미터 단위)", example = "2510")
    private int distance;
    @Schema(description = "소요시간 (분 단위)", example = "90")
    private int time;
    @Schema(example = "강서구의 명소들을 차례로 들르며 걷게 되는 코스 가장 먼저 서울 유일의 향교인 양천향교를 지난다.")
    private String description;
    @ArraySchema(
            arraySchema = @Schema(description = "카테고리 (SCENERY, DATE, NATURE, RUN, WALK, CARE, RELAX, LEVEL_BASIC, LEVEL_ADVANCED, LEVEL_PRO)",
                    example = "[\"SCENERY\", \"DATE\", \"NATURE\", \"RUN\", \"WALK\", \"CARE\", \"RELAX\", \"LEVEL_BASIC\", \"LEVEL_ADVANCED\", \"LEVEL_PRO\"]", nullable = true),
            minItems = 0,
            maxItems = Integer.MAX_VALUE
    )
    private List<String> categories;
    @Schema(description = "소속 자치구 정보", example = "강서구,노원구")
    private String gugunSummary;
    @Schema(description = "상세경로 정보", example = "양천향교~겸재정선기념관~구암공원~허준박물관")
    private String routeSummary;
    @Schema(description = "연계 지하철 정보", example = "9호선,공항철도", nullable = true)
    private String nearSubway;
    @Schema(description = "진입로 정보", example = "진입로 1 : 양천향교역 지하철 9호선 2번 출입구\\n※교통편의 변경이 있을 수 있으니 출발전 확인 바랍니다.")
    private String accessWay;
    @Schema(description = "강남강북구분 (NORTH: 강북, SOUTH: 강남)", example = "SOUTH")
    private Course.CourseRegion region;
    @Schema(description = "대표 이미지", example = "https://gil.seoul.go.kr/view/point/2013/09/26/8530222389919.jpg", nullable = true)
    private String image;
    @Schema(description = "검색 위치로부터 해당 코스까지의 최단 직선 거리 (요청에 검색 위치를 포함하지 않았으면 null, 미터 단위)", example = "21382", nullable = true)
    private Long distanceFromSearchLocation;
    @ArraySchema(
            arraySchema = @Schema(description = "선형 경로 목록 (주의: 하나의 코스에 여러 경로가 존재할 수 있음! 각각의 경로별로 선형으로 이어진 위경도 좌표들이 주어짐)",
                    example = "[[[37.572523655, 126.83997772299995], [37.57320093300001, 126.83981497299999]]," +
                            "[[37.572394782, 126.83926482899994], [37.57320093300001, 126.83981497299999], [37.57245534399999, 126.83897155600005]]]",
                    nullable = true),
            minItems = 1,
            maxItems = Integer.MAX_VALUE
    )
    private List<List<List<Double>>> routes = new ArrayList<>();
}
