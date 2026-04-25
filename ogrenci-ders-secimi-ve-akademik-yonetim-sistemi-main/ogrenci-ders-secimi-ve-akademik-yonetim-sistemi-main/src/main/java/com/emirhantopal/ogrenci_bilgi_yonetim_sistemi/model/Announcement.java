package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Announcement extends BaseEntity {

    private String title;

    @Column(length = 2000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}