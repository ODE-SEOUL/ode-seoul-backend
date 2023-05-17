package kr.njw.odeseoul.notice.application.dto;

import kr.njw.odeseoul.common.dto.PageResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FindNoticesResponse extends PageResponse {
    List<FindNoticeResponse> notices;
}
