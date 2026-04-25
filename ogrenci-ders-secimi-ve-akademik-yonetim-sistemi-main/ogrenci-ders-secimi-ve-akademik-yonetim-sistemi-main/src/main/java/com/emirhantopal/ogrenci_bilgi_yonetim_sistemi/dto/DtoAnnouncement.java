package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAnnouncement {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private String teacherFirstName;
    private String teacherLastName;
}