package com.businessapi.service;


import com.businessapi.config.ConfigProperties;
import com.businessapi.dto.request.DeleteFileRequestDTO;
import com.businessapi.dto.request.SaveFileRequestDTO;
import com.businessapi.dto.request.UpdateFileRequestDTO;
import com.businessapi.entity.File;
import static com.businessapi.exception.ErrorType.*;
import com.businessapi.exception.FileManagementServiceException;
import com.businessapi.repository.FileRepository;
import com.businessapi.utilty.enums.EContentType;
import com.businessapi.utilty.enums.EStatus;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {
    private final FileRepository fileRepository;
    private final MinioClient minioClient;
    private final ConfigProperties configProperties;



    public String save(SaveFileRequestDTO dto) {
        String bucketName = configProperties.getBucket();
        String uuid = UUID.randomUUID().toString();

        try {

            MultipartFile file = dto.file();
            EContentType contentType = dto.contentType();

            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }


            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(uuid)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType.getType())
                    .bucket(bucketName)
                    .build();
            minioClient.putObject(putObjectArgs);


            File fileEntity = File.builder()
                    .uuid(uuid)
                    .authId(1L)
                    .status(EStatus.ACTIVE)
                    .contentType(contentType)
                    .build();
            fileRepository.save(fileEntity);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuid;
    }



    public void deleteFile(DeleteFileRequestDTO dto) {
        try {

            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(dto.bucketName())
                    .object(dto.uuid())
                    .build());

            File file = fileRepository.findByUuid(dto.uuid()).orElseThrow(() ->new FileManagementServiceException(FILE_NOT_FOUND));
            file.setStatus(EStatus.DELETED);
            fileRepository.save(file);


        } catch (Exception e) {
            System.out.println("\n" + "Unexpected error occurred " + e.getMessage());
        }

    }

    public String updateFile(UpdateFileRequestDTO dto) {
        try {

            File fileMetadata = getFileMetadata(dto.uuid());

            if (fileMetadata.getStatus() == EStatus.DELETED) {
                throw new FileManagementServiceException(FILE_ALREADY_DELETED);
            }


            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(configProperties.getBucket())
                    .object(dto.uuid())
                    .build());


            InputStream fileInputStream = dto.file().getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configProperties.getBucket())
                    .object(dto.uuid())
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .contentType(dto.contentType().getType())
                    .build());


            fileMetadata.setUpdateAt(LocalDateTime.now());
            fileRepository.save(fileMetadata);

        } catch (Exception e) {
            throw new RuntimeException("Beklenmeyen bir hata: " + e.getMessage());
        }
        return dto.uuid();
    }


    public InputStream getFile(String uuid)  {

        File file = getFileMetadata(uuid);

        if (file.getStatus() == EStatus.DELETED) {
            throw new FileManagementServiceException(FILE_ALREADY_DELETED);
        }
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(configProperties.getBucket())
                    .object(uuid)
                    .build();
            return minioClient.getObject(getObjectArgs);

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
    public File getFileMetadata(String uuid) {
        return fileRepository.findByUuid(uuid)
                .orElseThrow(() -> new FileManagementServiceException(FILE_NOT_FOUND));
    }




}


