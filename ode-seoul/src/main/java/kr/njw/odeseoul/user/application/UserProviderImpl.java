package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.entity.RecruitApplication;
import kr.njw.odeseoul.recruit.repository.RecruitApplicationRepository;
import kr.njw.odeseoul.recruit.repository.RecruitRepository;
import kr.njw.odeseoul.user.application.dto.FindPickedCourseResponse;
import kr.njw.odeseoul.user.application.dto.FindStampResponse;
import kr.njw.odeseoul.user.application.dto.FindUserResponse;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.entity.UserPickedCourse;
import kr.njw.odeseoul.user.repository.UserPickedCourseRepository;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserProviderImpl implements UserProvider {
    private final UserRepository userRepository;
    private final UserPickedCourseRepository userPickedCourseRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;

    @Override
    public FindUserResponse findUser(Long id) {
        User user = this.userRepository.findByIdAndDeletedAtIsNull(id).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.FIND_USER_ERROR_NOT_FOUND_USER);
        }

        FindUserResponse response = new FindUserResponse();
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setProfileImage(user.getProfileImage());
        response.setLocationCode(user.getLocationCode());
        response.setSignupStatus(user.getSignupStatus());
        return response;
    }

    public List<FindStampResponse> findStamps(Long userId) {
        List<Recruit> stampByHostRecruits = this.recruitRepository
                .findAllByHostIdAndProgressStatusAndDeletedAtIsNull(userId, Recruit.RecruitProgressStatus.DONE);
        List<RecruitApplication> stampByMemberApplications = this.recruitApplicationRepository
                .findAllByMemberIdAndDeletedAtIsNullAndRecruitProgressStatusAndRecruitDeletedAtIsNull(userId, Recruit.RecruitProgressStatus.DONE);

        List<FindStampResponse> responses = new ArrayList<>();

        for (Recruit recruit : stampByHostRecruits) {
            this.applyRecruitToStampResponses(responses, recruit);
        }

        for (RecruitApplication application : stampByMemberApplications) {
            Recruit recruit = application.getRecruit();
            this.applyRecruitToStampResponses(responses, recruit);
        }

        responses.sort((o1, o2) -> (int) (o1.getCourseId() - o2.getCourseId()));
        return responses;
    }

    public List<FindPickedCourseResponse> findPickedCourses(Long userId) {
        User user = this.userRepository.findByIdAndDeletedAtIsNull(userId).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.FIND_PICKED_COURSES_ERROR_BAD_USER);
        }

        List<UserPickedCourse> userPickedCourses = this.userPickedCourseRepository.findAllByUserIdAndDeletedAtIsNullOrderByIdDesc(userId);

        return userPickedCourses.stream().map(userPickedCourse -> {
            FindPickedCourseResponse response = new FindPickedCourseResponse();
            response.setCourseId(userPickedCourse.getCourse().getId());
            response.setCourseName(userPickedCourse.getCourse().getName());
            response.setImage(userPickedCourse.getCourse().getImage());
            return response;
        }).collect(Collectors.toList());
    }

    private void applyRecruitToStampResponses(List<FindStampResponse> responses, Recruit recruit) {
        if (recruit.getCourseId() == null) {
            return;
        }

        FindStampResponse prevStamp = responses.stream().filter(stamp -> Objects.equals(stamp.getCourseId(), recruit.getCourseId())).findAny().orElse(null);

        if (prevStamp == null) {
            FindStampResponse response = new FindStampResponse();
            response.setCourseId(recruit.getCourseId());
            response.setCreatedAt(recruit.getScheduledAt());
            responses.add(response);
            return;
        }

        if (recruit.getScheduledAt() == null) {
            return;
        }

        if (prevStamp.getCreatedAt() == null || recruit.getScheduledAt().isBefore(prevStamp.getCreatedAt())) {
            prevStamp.setCreatedAt(recruit.getScheduledAt());
        }
    }
}
