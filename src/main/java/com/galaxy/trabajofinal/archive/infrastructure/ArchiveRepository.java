package com.galaxy.trabajofinal.archive.infrastructure;

import com.galaxy.trabajofinal.archive.domain.entities.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive,Long> {
    List<Archive> findByUserId(Long userId);
}
