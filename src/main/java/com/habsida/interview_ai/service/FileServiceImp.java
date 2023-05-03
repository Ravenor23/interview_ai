package com.habsida.interview_ai.service;

import com.habsida.interview_ai.exception.NotFoundException;
import com.habsida.interview_ai.model.File;
import com.habsida.interview_ai.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImp implements FileService {

    private final FileRepository fileRepository;
    private final AmazonS3Service amazonS3Service;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImp.class);

    public FileServiceImp(FileRepository fileRepository, AmazonS3Service amazonS3Service) {
        this.fileRepository = fileRepository;
        this.amazonS3Service = amazonS3Service;
    }

    @Transactional(readOnly = true)
    @Override
    public File findFileById(Long id){
        LOGGER.info(id.toString());

        return fileRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<File> findAllFile(Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return fileRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public File addFile(MultipartFile file, String hashName) {
        LOGGER.info(file.toString());

        File file1 = File.builder()
                .path(amazonS3Service.create(file, hashName))
                .type(file.getContentType())
                .originalTitle(file.getOriginalFilename())
                .build();
        return fileRepository.save(file1);
    }

    @Transactional
    @Override
    public File updateFile(Long id, MultipartFile file) {
        LOGGER.info(id.toString(), file.toString());

        File fileToUpdate = this.findFileById(id);
        fileToUpdate.setPath(amazonS3Service.create(file, fileToUpdate.getShortPath())); //???
        fileToUpdate.setOriginalTitle(file.getOriginalFilename());
        fileToUpdate.setType(file.getContentType());
        return fileRepository.saveAndFlush(fileToUpdate);
    }

    @Transactional
    @Override
    public void deleteFile(Long id) {
        LOGGER.info(id.toString());

        if (this.findFileById(id) != null) {
            fileRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteFileFromBucket(String path) {
        LOGGER.info(path);

        amazonS3Service.deleteFile(path);
    }

    @Override
    public byte[] downloadFile(Long id) throws IOException {
        LOGGER.info(id.toString());

        File file = this.findFileById(id);
        return amazonS3Service.downloadFile(file.getShortPath());
    }

}
