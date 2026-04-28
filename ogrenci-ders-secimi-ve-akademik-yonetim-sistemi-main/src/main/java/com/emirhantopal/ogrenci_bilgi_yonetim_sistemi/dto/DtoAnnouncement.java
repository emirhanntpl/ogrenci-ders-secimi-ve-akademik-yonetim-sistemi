package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAnnouncement extends DtoBaseEntity {
    private String title;
    private String content;
    private String publishDate;
    private String author;
}
