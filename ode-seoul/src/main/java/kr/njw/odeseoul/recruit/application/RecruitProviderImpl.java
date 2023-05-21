package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.recruit.application.dto.FindRecruitRequest;
import kr.njw.odeseoul.recruit.application.dto.FindRecruitResponse;
import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsRequest;
import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsResponse;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.entity.RecruitApplication;
import kr.njw.odeseoul.recruit.entity.RecruitComment;
import kr.njw.odeseoul.recruit.repository.RecruitApplicationRepository;
import kr.njw.odeseoul.recruit.repository.RecruitCommentRepository;
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
    private final RecruitCommentRepository recruitCommentRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;

    public FindRecruitResponse findRecruit(FindRecruitRequest request) {
        Recruit recruit = this.recruitRepository.findByIdAndDeletedAtIsNull(request.getId()).orElse(null);

        if (recruit == null) {
            throw new BaseException(BaseResponseStatus.FIND_RECRUIT_ERROR_NOT_FOUND);
        }

        List<RecruitComment> comments = this.recruitCommentRepository.findAllByRecruitIdAndDeletedAtIsNullOrderByIdDesc(recruit.getId());
        List<RecruitApplication> applications = this.recruitApplicationRepository.findAllByRecruitIdAndDeletedAtIsNullOrderByIdAsc(recruit.getId());

        FindRecruitResponse response = new FindRecruitResponse();
        response.setId(recruit.getId());

        FindRecruitResponse.FindRecruitResponseUser host = new FindRecruitResponse.FindRecruitResponseUser();
        host.setId(recruit.getHost().getId());
        host.setNickname(recruit.getHost().getNickname());
        host.setProfileImage(recruit.getHost().getProfileImage());
        host.setLocationCode(recruit.getHost().getLocationCode());
        host.setSignupStatus(recruit.getHost().getSignupStatus());
        response.setHost(host);

        response.setCourseId(recruit.getCourseId());
        response.setCategory(recruit.getCategory());
        response.setTitle(recruit.getTitle());
        response.setContent(recruit.getContent());
        response.setImage(recruit.getImage());
        response.setCurrentPeople(recruit.getCurrentPeople());
        response.setMaxPeople(recruit.getMaxPeople());
        response.setScheduledAt(recruit.getScheduledAt());
        response.setProgressStatus(recruit.getProgressStatus());
        response.setCreatedAt(recruit.getCreatedAt());

        response.setComments(comments.stream().map(recruitComment -> {
            FindRecruitResponse.FindRecruitResponseComment commentResponse = new FindRecruitResponse.FindRecruitResponseComment();
            commentResponse.setId(recruitComment.getId());

            FindRecruitResponse.FindRecruitResponseUser user = new FindRecruitResponse.FindRecruitResponseUser();
            user.setId(recruitComment.getUser().getId());
            user.setNickname(recruitComment.getUser().getNickname());
            user.setProfileImage(recruitComment.getUser().getProfileImage());
            user.setLocationCode(recruitComment.getUser().getLocationCode());
            user.setSignupStatus(recruitComment.getUser().getSignupStatus());
            commentResponse.setUser(user);

            commentResponse.setContent(recruitComment.getContent());
            commentResponse.setCreatedAt(recruitComment.getCreatedAt());

            return commentResponse;
        }).collect(Collectors.toList()));

        response.setApplications(applications.stream().map(recruitApplication -> {
            FindRecruitResponse.FindRecruitResponseApplication applicationResponse = new FindRecruitResponse.FindRecruitResponseApplication();
            applicationResponse.setId(recruitApplication.getId());

            FindRecruitResponse.FindRecruitResponseUser member = new FindRecruitResponse.FindRecruitResponseUser();
            member.setId(recruitApplication.getMember().getId());
            member.setNickname(recruitApplication.getMember().getNickname());
            member.setProfileImage(recruitApplication.getMember().getProfileImage());
            member.setLocationCode(recruitApplication.getMember().getLocationCode());
            member.setSignupStatus(recruitApplication.getMember().getSignupStatus());
            applicationResponse.setMember(member);

            applicationResponse.setCreatedAt(recruitApplication.getCreatedAt());

            return applicationResponse;
        }).collect(Collectors.toList()));

        return response;
    }

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

            FindRecruitResponse.FindRecruitResponseUser host = new FindRecruitResponse.FindRecruitResponseUser();
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
