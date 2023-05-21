package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.course.entity.Course;
import kr.njw.odeseoul.course.repository.CourseRepository;
import kr.njw.odeseoul.recruit.application.dto.CreateRecruitRequest;
import kr.njw.odeseoul.recruit.application.dto.CreateRecruitResponse;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.repository.RecruitRepository;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitServiceImpl implements RecruitService {
    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CreateRecruitResponse createRecruit(CreateRecruitRequest request) {
        User host = this.userRepository.findByIdAndDeletedAtIsNull(request.getHostUserId()).orElse(null);
        Course course = this.courseRepository.findByIdAndDeletedAtIsNull(request.getCourseId()).orElse(null);

        if (host == null) {
            throw new BaseException(BaseResponseStatus.CREATE_RECRUIT_ERROR_NOT_FOUND_HOST);
        }

        if (course == null) {
            throw new BaseException(BaseResponseStatus.CREATE_RECRUIT_ERROR_NOT_FOUND_COURSE);
        }

        Recruit recruit = Recruit.builder()
                .host(host)
                .course(course)
                .category(request.getCategory())
                .title(StringUtils.trimToEmpty(request.getTitle()))
                .content(StringUtils.trimToEmpty(request.getContent()))
                .image(StringUtils.trimToEmpty(request.getImage()))
                .currentPeople(1)
                .maxPeople(request.getMaxPeople())
                .scheduledAt(request.getScheduledAt())
                .progressStatus(Recruit.RecruitProgressStatus.OPEN)
                .build();

        this.recruitRepository.saveAndFlush(recruit);

        CreateRecruitResponse response = new CreateRecruitResponse();
        response.setId(recruit.getId());
        return response;
    }
}
