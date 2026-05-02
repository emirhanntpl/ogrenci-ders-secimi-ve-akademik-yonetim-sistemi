package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IAnnouncementController {
    DtoAnnouncement addAnnouncement(@RequestBody DtoAnnouncementIU dtoAnnouncementIU);
    DtoAnnouncement updateAnnouncement(@PathVariable("id") Long id, @RequestBody DtoAnnouncementIU dtoAnnouncementIU);
    void deleteAnnouncement(@PathVariable("id") Long id);
    DtoAnnouncement getAnnouncementById(@PathVariable("id") Long id);
    List<DtoAnnouncement> getAllAnnouncements();
}