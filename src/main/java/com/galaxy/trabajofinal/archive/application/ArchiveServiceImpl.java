package com.galaxy.trabajofinal.archive.application;

import com.galaxy.trabajofinal.archive.domain.DTOs.ArchiveResponse;
import com.galaxy.trabajofinal.archive.domain.DTOs.CreateArchiveRequest;
import com.galaxy.trabajofinal.archive.domain.DTOs.CreateArchiveResponse;
import com.galaxy.trabajofinal.archive.domain.entities.Archive;
import com.galaxy.trabajofinal.archive.domain.exceptions.NotFoundException;
import com.galaxy.trabajofinal.archive.domain.service.ArchiveService;
import com.galaxy.trabajofinal.archive.infrastructure.ArchiveRepository;
import com.galaxy.trabajofinal.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArchiveServiceImpl implements ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final UserRepository userRepository;

    @Override
    public List<ArchiveResponse> getAllArchives() {
        List<Archive> archives=this.archiveRepository.findAll();
        List<ArchiveResponse> response = archives.stream()
                .map(archive -> new ArchiveResponse(
                        archive.getId(),
                        archive.getName(),
                        archive.getUser().getUserName()
                ))
                .toList();

        return response;
    }

    @Override
    public List<ArchiveResponse> getByUserId(Long userId) {
        var user=this.userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("Usuario con el ID: "+userId.toString() + " no encontrado");
        }
        List<Archive> archives=this.archiveRepository.findByUserId(userId);
        List<ArchiveResponse> response = archives.stream()
                .map(archive -> new ArchiveResponse(
                        archive.getId(),
                        archive.getName(),
                        archive.getUser().getUserName()
                ))
                .toList();

        return response;
    }

    @Override
    public CreateArchiveResponse createArchive(CreateArchiveRequest request) {
        var user=this.userRepository.findById(request.userId());
        if(user.isEmpty()){
            throw new NotFoundException("Usuario con el ID: "+request.userId().toString() + " no encontrado");
        }
        Archive archive = new Archive();
        archive.setName(request.name());
        archive.setUser(user.get());
        Archive savedArchive = archiveRepository.save(archive);
        return new CreateArchiveResponse(savedArchive.getName(),user.get().getUserName());
    }
}
