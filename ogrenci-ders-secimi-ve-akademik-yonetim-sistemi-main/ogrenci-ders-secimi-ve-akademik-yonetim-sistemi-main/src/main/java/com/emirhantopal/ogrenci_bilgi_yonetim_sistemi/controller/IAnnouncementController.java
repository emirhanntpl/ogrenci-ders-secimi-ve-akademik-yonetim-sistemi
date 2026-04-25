package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;

import java.util.List;

public interface IAnnouncementController {
    DtoAnnouncement addAnnouncement(DtoAnnouncementIU dtoAnnouncementIU);
    List<DtoAnnouncement> getAllAnnouncements();
    DtoAnnouncement getAnnouncementById(Long id);
    void deleteAnnouncement(Long id);
}