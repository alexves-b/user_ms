package com.user.client;

import com.user.client.config.FeignSupportConfig;
import com.user.dto.StorageDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "Admin-panel-microservice", url = "${gtw.ADMIN-PANEL}", configuration = FeignSupportConfig.class)

public interface AdminClient {
    @PostMapping(value = "/storage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Получение ссылки на загруженный файл")
    StorageDto getLinkForUploadAvatar(@RequestHeader("Authorization")@NonNull String bearerToken, @RequestBody MultipartFile file);
}
