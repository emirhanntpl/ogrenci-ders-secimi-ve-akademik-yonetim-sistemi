package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "announcement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Announcement extends BaseEntity {

    private String title;
    
    private String content;
    
    private LocalDateTime publishDate;
    
    private String author;

}
