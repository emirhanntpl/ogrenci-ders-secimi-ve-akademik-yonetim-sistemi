package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;

import java.util.List;

public interface IAnnouncementController {
    DtoAnnouncement addAnnouncement(DtoAnnouncementIU dtoAnnouncementIU);
    DtoAnnouncement updateAnnouncement(Long id, DtoAnnouncementIU dtoAnnouncementIU);
    void deleteAnnouncement(Long id);
    DtoAnnouncement getAnnouncementById(Long id);
    List<DtoAnnouncement> getAllAnnouncements();
}
