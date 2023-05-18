package kr.njw.odeseoul.event.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.njw.odeseoul.event.entity.Event;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FindEventResponse {
    @Schema(description = "행사 UUID", example = "879a1a52-70e6-ffc6-7cdd-41c440d3d676")
    private String uuid;
    @Schema(description = "카테고리", example = "SHOW")
    private Event.EventCategory category;
    @Schema(description = "분야", example = "콘서트")
    private String codename;
    @Schema(description = "자치구", example = "강동구")
    private String guname;
    @Schema(description = "행사명", example = "Dear Next Generation, 유다빈밴드(YUDABINBAND)")
    private String title;
    @Schema(description = "장소", example = "강동아트센터 대극장 한강")
    private String place;
    @Schema(description = "이용대상", example = "14세이상 관람가")
    private String useTarget;
    @Schema(description = "이용요금", example = "R석 66,000원 S석 44,000원")
    private String useFee;
    @Schema(description = "홈페이지 주소",
            example = "http://www.gdfac.or.kr/web/lay2/program/S1T204C211/show/view.do?show_seq=1925&cpage=2&rows=10&keyword=&place=&start_dt=&end_dt=")
    private String orgLink;
    @Schema(description = "대표 이미지",
            example = "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=c694a4f0777040b994e88390c2b982b1&thumb=Y")
    private String mainImage;
    @Schema(description = "시작일", example = "2023-09-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Schema(description = "종료일", example = "2023-09-30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
