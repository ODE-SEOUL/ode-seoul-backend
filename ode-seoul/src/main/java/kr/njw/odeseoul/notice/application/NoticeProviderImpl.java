package kr.njw.odeseoul.notice.application;

import kr.njw.odeseoul.notice.application.dto.FindNoticeResponse;
import kr.njw.odeseoul.notice.application.dto.FindNoticesResponse;
import kr.njw.odeseoul.notice.entity.Notice;
import kr.njw.odeseoul.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeProviderImpl implements NoticeProvider {
    private final NoticeRepository noticeRepository;

    public FindNoticesResponse findNotices(int pageNumber, int pagingSize) {
        final int MAX_PAGING_SIZE = 100;

        Page<Notice> noticePage = this.noticeRepository.findAllByDeletedAtIsNullOrderByIdDesc(
                PageRequest.of(pageNumber - 1, Math.min(pagingSize, MAX_PAGING_SIZE)));

        FindNoticesResponse response = new FindNoticesResponse();
        response.setPage(noticePage.getNumber() + 1);
        response.setPageSize(noticePage.getNumberOfElements());
        response.setPagingSize(noticePage.getSize());
        response.setTotalPage(noticePage.getTotalPages());
        response.setTotalSize(noticePage.getTotalElements());
        response.setNotices(noticePage.getContent().stream().map(notice -> {
            FindNoticeResponse noticeResponse = new FindNoticeResponse();
            noticeResponse.setId(notice.getId());
            noticeResponse.setAuthor(notice.getAuthor());
            noticeResponse.setTitle(notice.getTitle());
            noticeResponse.setContent(notice.getContent());
            noticeResponse.setCreatedAt(notice.getCreatedAt());
            return noticeResponse;
        }).collect(Collectors.toList()));

        return response;
    }
}
