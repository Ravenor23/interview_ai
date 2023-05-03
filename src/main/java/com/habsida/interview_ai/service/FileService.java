package com.habsida.interview_ai.service;

import com.habsida.interview_ai.model.File;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    Page<File> findAllFile(Integer pageNo, Integer pageSize, String sortBy);
    File findFileById(Long id);
//    File findFileByIdWithFullPath(Long id);
    File addFile(MultipartFile file, String hashName);
    File updateFile(Long id, MultipartFile file);
    void deleteFile(Long id);
    void deleteFileFromBucket(String path);
    byte[] downloadFile(Long id) throws IOException;
}
