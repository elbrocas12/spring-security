package com.galaxy.trabajofinal.archive.domain.service;

import com.galaxy.trabajofinal.archive.domain.DTOs.ArchiveResponse;
import com.galaxy.trabajofinal.archive.domain.DTOs.CreateArchiveRequest;
import com.galaxy.trabajofinal.archive.domain.DTOs.CreateArchiveResponse;

import java.util.List;

public interface ArchiveService {
    List<ArchiveResponse> getAllArchives();
    List<ArchiveResponse> getByUserId(Long userId);
    CreateArchiveResponse createArchive(CreateArchiveRequest request);

}
