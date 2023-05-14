package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.user.application.dto.FindUserResponse;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserProviderImpl implements UserProvider {
    private final UserRepository userRepository;

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
        response.setLocationCode(user.getLocation().getCode());
        response.setSignupStatus(user.getSignupStatus());
        return response;
    }
}
