package com.galaxy.trabajofinal.archive.interfaces;

import com.galaxy.trabajofinal.archive.domain.DTOs.CreateArchiveRequest;
import com.galaxy.trabajofinal.archive.domain.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/archives")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllArchives(){
        var response= this.archiveService.getAllArchives();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}/user")
    public ResponseEntity<?> getByUserId(@PathVariable("id")Long userId){
        var response=this.archiveService.getByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<?> createArchive(@RequestBody CreateArchiveRequest request){
        var response=this.archiveService.createArchive(request);
        return ResponseEntity.ok(response);

    }
}
