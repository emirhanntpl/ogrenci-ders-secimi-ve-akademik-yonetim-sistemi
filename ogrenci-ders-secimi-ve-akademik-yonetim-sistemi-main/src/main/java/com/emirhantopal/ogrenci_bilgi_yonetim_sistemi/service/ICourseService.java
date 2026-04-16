package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseIU;

import java.util.List;

public interface ICourseService {

    public DtoCourse courseAdd(DtoCourseIU dtoCourseIU);

    public DtoCourse courseUpdate(Long id ,DtoCourseIU dtoCourseIU);

    public void courseDelete(Long id);

    public List<DtoCourse> courseAll();



}
