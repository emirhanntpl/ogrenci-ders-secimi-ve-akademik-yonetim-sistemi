package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hocalar")
public class Hoca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin veritabanı tarafından otomatik olarak 1, 2, 3 şeklinde atanmasını sağlar.
    private Long id;

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private String soyad;

    @Column(nullable = false, unique = true)
    private String email;

    // Boş constructor (JPA için zorunlu)
    public Hoca() {
    }

    // Getter ve Setter metotları
    public Long getId() {
        return id;
    }

    // ID veritabanı tarafından üretileceği için setId metodunu kullanırken dikkatli olmalısınız veya silebilirsiniz.
    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}