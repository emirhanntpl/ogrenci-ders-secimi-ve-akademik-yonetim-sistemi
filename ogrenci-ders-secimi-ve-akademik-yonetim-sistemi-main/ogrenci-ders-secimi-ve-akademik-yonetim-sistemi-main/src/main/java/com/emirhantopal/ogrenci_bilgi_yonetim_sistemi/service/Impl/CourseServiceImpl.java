package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Course;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ICourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public DtoCourse courseAdd(DtoCourseIU dtoCourseIU) {
        Course course = new Course();
        DtoCourse dtoCourse=new DtoCourse();
        course.setCode(dtoCourseIU.getCode());
        course.setName(dtoCourseIU.getName());
        course.setCredit(dtoCourseIU.getCredit());
        course.setAkts(dtoCourseIU.getAkts());
        course.setQuota(dtoCourseIU.getQuota());
        Course savedCourse = courseRepository.save(course);
        BeanUtils.copyProperties(savedCourse, dtoCourse);

        return dtoCourse;
    }

    @Override
    public DtoCourse courseUpdate(Long id ,DtoCourseIU dtoCourseIU) {
        Optional<Course> byId = courseRepository.findById(id);
        if(byId.isEmpty()){
            throw new BaseException(MessageType.INVALID_COURSE_ID, HttpStatus.BAD_REQUEST);
        }
        Course course = byId.get();
        DtoCourse dtoCourse=new DtoCourse();
        course.setAkts(dtoCourseIU.getAkts());
        course.setCredit(dtoCourseIU.getCredit());
        course.setCode(dtoCourseIU.getCode());
        course.setName(dtoCourseIU.getName());
        course.setQuota(dtoCourseIU.getQuota());
        Course savedCourse = courseRepository.save(course);
        BeanUtils.copyProperties(savedCourse, dtoCourse);
        return dtoCourse;
    }

    @Override
    public void courseDelete(Long id) {
        Optional<Course> byId = courseRepository.findById(id);
        if (byId.isEmpty()){
            throw new BaseException(MessageType.INVALID_COURSE_ID,HttpStatus.BAD_REQUEST);
        }
        Course course = byId.get();
        courseRepository.delete(course);
        System.out.println("Ders kaydı silindi. "+ id);

    }

    @Override
    public List<DtoCourse> courseAll() {
        List<Course> allCourse = courseRepository.findAll();
        if (allCourse.isEmpty()){
            throw new BaseException(MessageType.COURSE_LIST_IS_EMPTY,HttpStatus.BAD_REQUEST);
        }
        List<DtoCourse> dtoCourses=new ArrayList<>();
        for (Course course:allCourse){
            DtoCourse dtoCourse=new DtoCourse();
            dtoCourse.setCode(course.getCode());
            dtoCourse.setName(course.getName());
            dtoCourse.setCredit(course.getCredit());
            dtoCourse.setAkts(course.getAkts());
            dtoCourse.setQuota(course.getQuota());
            dtoCourses.add(dtoCourse);

        }
        return dtoCourses;
    }
}
