package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsRequest;
import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsResponse;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.repository.RecruitRepository;
import kr.njw.odeseoul.recruit.repository.dto.SearchRecruitsRepoRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecruitProviderImpl implements RecruitProvider {
    private final RecruitRepository recruitRepository;

    @Override
    public SearchRecruitsResponse searchRecruits(SearchRecruitsRequest request) {
        final int MAX_PAGING_SIZE = 100;

        SearchRecruitsRepoRequest repoRequest = new SearchRecruitsRepoRequest();

        if (request.getCategory() != null) {
            repoRequest.setCategory(request.getCategory());
        }

        if (request.getCourseId() != null) {
            repoRequest.setCourseId(request.getCourseId());
        }

        if (request.getHostUserId() != null) {
            repoRequest.setHostUserId(request.getHostUserId());
        }

        if (request.getMemberUserId() != null) {
            repoRequest.setMemberUserId(request.getMemberUserId());
        }

        if (request.getTitleContains() != null) {
            repoRequest.setTitleContains(StringUtils.trimToEmpty(request.getTitleContains()));
        }

        if (request.getOpen() != null) {
            if (request.getOpen()) {
                repoRequest.setProgressStatusOneOf(List.of(Recruit.RecruitProgressStatus.OPEN));
            } else {
                repoRequest.setProgressStatusOneOf(List.of(Recruit.RecruitProgressStatus.CLOSED, Recruit.RecruitProgressStatus.DONE));
            }
        }

        repoRequest.setPageable(PageRequest.of(request.getPageNumber() - 1, Math.min(request.getPagingSize(), MAX_PAGING_SIZE)));

        Page<Recruit> recruitPage = this.recruitRepository.search(repoRequest);

        SearchRecruitsResponse response = new SearchRecruitsResponse();
        response.setPage(recruitPage.getNumber() + 1);
        response.setPageSize(recruitPage.getNumberOfElements());
        response.setPagingSize(recruitPage.getSize());
        response.setTotalPage(recruitPage.getTotalPages());
        response.setTotalSize(recruitPage.getTotalElements());
        response.setRecruits(recruitPage.getContent().stream().map(recruit -> {
            SearchRecruitsResponse.SearchRecruitResponse recruitResponse = new SearchRecruitsResponse.SearchRecruitResponse();
            recruitResponse.setId(recruit.getId());

            SearchRecruitsResponse.SearchRecruitResponseHost host = new SearchRecruitsResponse.SearchRecruitResponseHost();
            host.setId(recruit.getHost().getId());
            host.setNickname(recruit.getHost().getNickname());
            host.setProfileImage(recruit.getHost().getProfileImage());
            host.setLocationCode(recruit.getHost().getLocationCode());
            host.setSignupStatus(recruit.getHost().getSignupStatus());
            recruitResponse.setHost(host);

            recruitResponse.setCourseId(recruit.getCourseId());
            recruitResponse.setCategory(recruit.getCategory());
            recruitResponse.setTitle(recruit.getTitle());
            recruitResponse.setContent(recruit.getContent());
            recruitResponse.setImage(recruit.getImage());
            recruitResponse.setCurrentPeople(recruit.getCurrentPeople());
            recruitResponse.setMaxPeople(recruit.getMaxPeople());
            recruitResponse.setScheduledAt(recruit.getScheduledAt());
            recruitResponse.setProgressStatus(recruit.getProgressStatus());
            recruitResponse.setCreatedAt(recruit.getCreatedAt());

            return recruitResponse;
        }).collect(Collectors.toList()));

        return response;
    }
}
