package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.model.File;
import com.habsida.interview_ai.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "Файлы", tags = {"Файлы"})
@RestController
@RequestMapping("/api/file")
public class FileRestController {

    private final FileService fileService;

    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(
            summary = "Получение файла по идентификатору"
    )
    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable @Parameter(description = "Идентификатор файла") Long id) {
        return ResponseEntity.ok().body(fileService.findFileById(id));
    }

    @Operation(
            summary = "Скачивание файла?"
    )
    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable @Parameter(description = "Идентификатор файла") Long id) throws Exception {
        String fileName = fileService.findFileById(id).getPath();
        byte[] data = fileService.downloadFile(id);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @Operation(
            summary = "Найти все файлы"
    )
    @GetMapping
    public ResponseEntity<Page<File>> findAllFiles(@RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                   @RequestParam(defaultValue = "id") @Parameter(description = "Сортировка") String sortBy) {
        return ResponseEntity.ok().body(fileService.findAllFile(pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Создание файла"
    )
    @PostMapping
    public ResponseEntity<File> createFile(@RequestParam @Parameter(description = "Звуковая дорожка") MultipartFile file) {
        return ResponseEntity.ok(fileService.addFile(file, ""));
    }

    @Operation(
            summary = "Обновление файла"
    )
    @PutMapping("/{id}")
    public ResponseEntity<File> updateFile(@PathVariable @Parameter(description = "Идентификатор файла") Long id,
                                           @RequestParam @Parameter(description = "Звуковая дорожка") MultipartFile fileUpdate) {
        return ResponseEntity.ok(fileService.updateFile(id, fileUpdate));
    }

    @Operation(
            summary = ""
    )
    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable @Parameter(description = "Идентификатор файла") Long id) {
        fileService.deleteFile(id);
    }

}
