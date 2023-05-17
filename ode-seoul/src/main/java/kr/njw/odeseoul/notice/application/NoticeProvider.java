package kr.njw.odeseoul.notice.application;

import kr.njw.odeseoul.notice.application.dto.FindNoticesResponse;

public interface NoticeProvider {
    FindNoticesResponse findNotices(int pageNumber, int pagingSize);
}
