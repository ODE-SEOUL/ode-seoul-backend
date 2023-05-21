package kr.njw.odeseoul.recruit.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class FindRecruitResponse {
    @Schema(description = "모집 아이디", example = "2")
    private Long id;

    private FindRecruitResponseUser host;

    @Schema(description = "코스 아이디", example = "4")
    private Long courseId;

    @Schema(description = "모집 카테고리", example = "COM_PHOTO")
    private Recruit.RecruitCategory category;

    @Schema(description = "제목", example = "모집")
    private String title;

    @Schema(description = "내용", example = "여러분을 모집합니다", nullable = true)
    private String content;

    @Schema(description = "대표 이미지", example = "https://ik.imagekit.io/njw1204/tr:w-720,h-720,c-at_max/ode-seoul/img_20230521125825735", nullable = true)
    private String image;

    @Schema(description = "모집 참가인원수 (모임장 본인 포함)", example = "1")
    private int currentPeople;

    @Schema(description = "모집 최대인원수 (모임장 본인 포함, 0: 제한없음)", example = "5")
    private int maxPeople;

    @Schema(description = "약속 일정", type = "string", example = "2023-05-21T14:08:45")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledAt;

    @Schema(description = """
            모집 상태

            OPEN: 모집 중\040\040
            CLOSED: 모집 마감 (현재 날짜나 참가인원수에 관계 없이 모임장이 직접 모집 마감 처리를 해야 마감됨)\040\040
            DONE: 활동 완료 (모임장이 직접 활동 완료 처리를 해야 완료됨)""", example = "OPEN")
    private Recruit.RecruitProgressStatus progressStatus;

    @Schema(description = "모집 등록일시", type = "string", example = "2023-05-21T09:33:02")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private List<FindRecruitResponseComment> comments = new ArrayList<>();

    private List<FindRecruitResponseApplication> applications = new ArrayList<>();

    @Data
    public static class FindRecruitResponseUser {
        @Schema(description = "유저 아이디", example = "1")
        private Long id;

        @Schema(description = "닉네임", example = "닉넴")
        private String nickname;

        @Schema(description = "프로필 이미지", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", nullable = true)
        private String profileImage;

        @Schema(description = "사는 곳 (서울시 자치구 행정동코드)", example = "1135000000", nullable = true)
        private String locationCode;

        @Schema(description = "회원가입 상태", example = "REGISTERED")
        private User.UserSignupStatus signupStatus;
    }

    @Data
    public static class FindRecruitResponseComment {
        @Schema(description = "댓글 아이디", example = "14")
        private Long id;

        private FindRecruitResponseUser user;

        @Schema(description = "내용", example = "안녕하세요")
        private String content;

        @Schema(description = "작성일시", type = "string", example = "2023-05-21T09:33:02")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Data
    public static class FindRecruitResponseApplication {
        @Schema(description = "신청 아이디", example = "21")
        private Long id;

        private FindRecruitResponseUser member;

        @Schema(description = "신청일시", type = "string", example = "2023-05-21T09:33:02")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
    }
}
