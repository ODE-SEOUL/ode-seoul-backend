package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.location.entity.Location;
import kr.njw.odeseoul.location.repository.LocationRepository;
import kr.njw.odeseoul.user.application.dto.EditProfileRequest;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public void editProfile(EditProfileRequest request) {
        boolean locationWillChange = (request.getLocationCode() != null);
        boolean locationWillAssign = StringUtils.isNotBlank(request.getLocationCode());

        User user = this.userRepository.findByIdAndDeletedAtIsNull(request.getId()).orElse(null);
        Location location = this.locationRepository.findByCodeAndDeletedAtIsNull(request.getLocationCode()).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.EDIT_PROFILE_ERROR_BAD_USER);
        }

        if (locationWillAssign && location == null) {
            throw new BaseException(BaseResponseStatus.EDIT_PROFILE_ERROR_BAD_LOCATION);
        }

        if (location != null && !location.isSeoulGugun()) {
            throw new BaseException(BaseResponseStatus.EDIT_PROFILE_ERROR_BAD_LOCATION);
        }

        user.editBasicProfile(request.getNickname(), request.getProfileImage());

        if (locationWillChange) {
            user.setLocation(location);
        }
    }
}
