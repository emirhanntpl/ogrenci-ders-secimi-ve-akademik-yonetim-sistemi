package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface IStudentController {

    public DtoStudent studentAdd(@RequestBody DtoStudentIU dtoStudentIU);

    public DtoStudent studentUpdate(@PathVariable(name = "id") Long id, @RequestBody DtoStudentIU dtoStudentIU);

    public void studentDelete(@PathVariable(name = "id") Long id);

    public List<DtoStudent> getAllStudent();

    public DtoStudent getStudentById(@PathVariable(name = "id") Long id);

    public DtoStudent getStudentByUsername(@RequestParam(name = "username") String username);

    public DtoStudent updateStudentProfile(@PathVariable(name = "id") Long id, @RequestBody Map<String, String> payload);
}