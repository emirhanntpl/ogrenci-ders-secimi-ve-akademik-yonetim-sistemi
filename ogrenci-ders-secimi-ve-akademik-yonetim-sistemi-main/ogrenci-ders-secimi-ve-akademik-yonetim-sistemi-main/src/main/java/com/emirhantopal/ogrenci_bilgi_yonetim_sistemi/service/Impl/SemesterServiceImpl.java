package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoSemester;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoSemesterIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Semester;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.SemesterRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ISemesterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterServiceImpl implements ISemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public DtoSemester semesterAdd(DtoSemesterIU dtoSemesterIU) {
        Semester semester = new Semester();
        DtoSemester dtoSemester = new DtoSemester();

        semester.setTerm(dtoSemesterIU.getTerm());
        semester.setIsActive(dtoSemesterIU.getIsActive());

        Semester savedSemester = semesterRepository.save(semester);
        BeanUtils.copyProperties(savedSemester, dtoSemester);

        return dtoSemester;
    }

    @Override
    public DtoSemester semesterUpdate(Long id, DtoSemesterIU dtoSemesterIU) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_SEMESTER_ID, HttpStatus.BAD_REQUEST));

        DtoSemester dtoSemester = new DtoSemester();

        semester.setTerm(dtoSemesterIU.getTerm());
        semester.setIsActive(dtoSemesterIU.getIsActive());

        Semester savedSemester = semesterRepository.save(semester);
        BeanUtils.copyProperties(savedSemester, dtoSemester);

        return dtoSemester;
    }

    @Override
    public void semesterDelete(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_SEMESTER_ID, HttpStatus.BAD_REQUEST));
        semesterRepository.delete(semester);
    }

    @Override
    public List<DtoSemester> getAllSemesters() {
        List<Semester> allSemesters = semesterRepository.findAll();
        if (allSemesters.isEmpty()) {
            throw new BaseException(MessageType.SEMESTER_LIST_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        List<DtoSemester> dtoSemesters = new ArrayList<>();
        for (Semester semester : allSemesters) {
            DtoSemester dtoSemester = new DtoSemester();
            BeanUtils.copyProperties(semester, dtoSemester);
            dtoSemesters.add(dtoSemester);
        }

        return dtoSemesters;
    }

    @Override
    public DtoSemester findBySemesterId(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_SEMESTER_ID, HttpStatus.BAD_REQUEST));

        DtoSemester dtoSemester = new DtoSemester();
        BeanUtils.copyProperties(semester, dtoSemester);
        return dtoSemester;
    }
}
