package com.user.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class StorageDto {

    @Schema(description = "Dto загруженного файла")
    public String fileName;
}
