package com.businessapi.controller;


import com.businessapi.dto.request.DeleteFileRequestDTO;
import com.businessapi.dto.request.SaveFileRequestDTO;
import com.businessapi.dto.request.UpdateFileRequestDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.File;
import com.businessapi.service.FileService;
import com.businessapi.utilty.enums.EContentType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import static com.businessapi.constants.EndPoints.*;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@RequestMapping(FILE)
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE})
public class FileController {
    private final FileService fileService;

    @PostMapping(SAVE)
    @Operation(summary = "Upload a file")
    public ResponseEntity<ResponseDTO<String>> uploadFile (@ModelAttribute SaveFileRequestDTO dto) {
        try (InputStream inputStream = dto.file().getInputStream() ) {

            String uuid = fileService.save(dto);

            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .code(200)
                            .message("File uploaded successfully")
                            .data(uuid)
                            .build()
            );
        } catch (IOException e) {
            return ResponseEntity.status(500).body(
                    ResponseDTO.<String>builder()
                            .code(500)
                            .message("File upload failed: " + e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }


    @DeleteMapping(DELETE)
    @Operation(summary = "Delete a file")
    public ResponseEntity<ResponseDTO<String>> deleteFile(@RequestBody DeleteFileRequestDTO fileDeleteDTO) {
        fileService.deleteFile(fileDeleteDTO);
        return ResponseEntity.ok(
                ResponseDTO.<String>builder()
                        .code(200)
                        .message("File deleted successfully")
                        .data(fileDeleteDTO.uuid())
                        .build()
        );
    }



    @PostMapping(UPDATE)
    @Operation(summary = "Update a file")
    public ResponseEntity<ResponseDTO<String>> updateFile(@ModelAttribute UpdateFileRequestDTO fileUpdateDTO) {
        try (InputStream inputStream = fileUpdateDTO.file().getInputStream()) {
            fileService.updateFile(fileUpdateDTO);
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .code(200)
                            .message("Dosya başarıyla güncellendi.")
                            .data(fileUpdateDTO.uuid())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ResponseDTO.<String>builder()
                            .code(500)
                            .message("Dosya güncellenemedi: " + e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }


    @GetMapping(value = GET+"/{uuid}")
    public ResponseEntity<Resource> getFile(@PathVariable String uuid) {
        try {
            File existingFile = fileService.getFileMetadata(uuid);
            InputStream inputStream = fileService.getFile(uuid);
            Resource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uuid + "\"")
                    .contentType(MediaType.parseMediaType(existingFile.getContentType().getType()))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}





