package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAssignmentIU {
    private String title;
    private String description;
    private String dueDate; // Frontend'den string olarak alıp parse edeceğiz
    private Long courseSectionId;
}
