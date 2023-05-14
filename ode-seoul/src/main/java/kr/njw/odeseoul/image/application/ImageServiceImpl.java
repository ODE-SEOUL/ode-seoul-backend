package kr.njw.odeseoul.image.application;

import com.google.gson.JsonObject;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import kr.njw.odeseoul.image.application.dto.UploadImageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageKit imageKit;

    public UploadImageResponse uploadImage(MultipartFile file) throws Exception {
        String originalFileName = StringUtils.trimToEmpty(file.getOriginalFilename());

        FileCreateRequest fileCreateRequest = new FileCreateRequest(file.getBytes(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        fileCreateRequest.setUseUniqueFileName(true);
        fileCreateRequest.setFolder("/ode-seoul/");

        JsonObject jsonObjectCustomMetadata = new JsonObject();
        jsonObjectCustomMetadata.addProperty("originalFileName", originalFileName);
        fileCreateRequest.setCustomMetadata(jsonObjectCustomMetadata);

        Result imageKitResult = this.imageKit.upload(fileCreateRequest);

        UploadImageResponse response = new UploadImageResponse();
        response.setUrl(imageKitResult.getUrl());
        return response;
    }
}
