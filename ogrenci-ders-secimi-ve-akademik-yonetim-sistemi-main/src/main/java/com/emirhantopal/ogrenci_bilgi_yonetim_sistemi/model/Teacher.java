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
public class Teacher extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String firstName;

    private String lastName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
