package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IAnnouncementController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementControllerImpl implements IAnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;

    @PostMapping("/add")
    @Override
    public DtoAnnouncement addAnnouncement(@Valid @RequestBody DtoAnnouncementIU dtoAnnouncementIU) {
        return announcementService.addAnnouncement(dtoAnnouncementIU);
    }

    @GetMapping("/getAll")
    @Override
    public List<DtoAnnouncement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/{id}")
    @Override
    public DtoAnnouncement getAnnouncementById(@PathVariable Long id) {
        return announcementService.getAnnouncementById(id);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
    }
}