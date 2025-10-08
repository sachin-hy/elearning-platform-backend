package com.example.project.service.Interface;

import com.example.project.dto.SectionDto;
import com.example.project.dto.SectionResponseDto;
import com.example.project.entity.Section;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.concurrent.Future;

public interface SectionServiceInterface {
    public SectionResponseDto saveSection(SectionDto sectionDto);
    public void deleteByid(String sectionid);

    public boolean existsById(@NotNull(message = "Section id Can Not be Null") Long sectionid);

    public Optional<Section> findById(@NotNull(message = "Section id Can Not be Null") Long sectionid);

    public void save(Section section);
}
