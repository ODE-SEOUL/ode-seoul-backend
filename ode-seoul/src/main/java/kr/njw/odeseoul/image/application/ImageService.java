package kr.njw.odeseoul.image.application;

import kr.njw.odeseoul.image.application.dto.UploadImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    UploadImageResponse uploadImage(MultipartFile file) throws Exception;
}
