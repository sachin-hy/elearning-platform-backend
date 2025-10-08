package com.example.project.service.Interface;

import com.example.project.dto.SubSectionDto;
import com.example.project.dto.SubSectionResponseDto;

public interface SubSectionServiceInterface {

    public SubSectionResponseDto saveSubSection(SubSectionDto sectionDto);
    public SubSectionResponseDto findByIdAndUpdate(SubSectionDto subsectionDto);
    public void deleteById(String id);

}
