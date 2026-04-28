package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Course;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ICourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public DtoCourse courseAdd(DtoCourseIU dtoCourseIU) {
        Course course = new Course();
        course.setCode(dtoCourseIU.getCode());
        course.setName(dtoCourseIU.getName());
        course.setCredit(dtoCourseIU.getCredit());
        course.setAkts(dtoCourseIU.getAkts());
        course.setQuota(dtoCourseIU.getQuota());
        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    @Override
    @Transactional
    public DtoCourse courseUpdate(Long id ,DtoCourseIU dtoCourseIU) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_ID, HttpStatus.BAD_REQUEST));
        
        course.setAkts(dtoCourseIU.getAkts());
        course.setCredit(dtoCourseIU.getCredit());
        course.setCode(dtoCourseIU.getCode());
        course.setName(dtoCourseIU.getName());
        course.setQuota(dtoCourseIU.getQuota());
        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    @Override
    @Transactional
    public void courseDelete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_ID, HttpStatus.BAD_REQUEST));
        
        // 1. Önce bu derse bağlı şubeleri (CourseSection) bulalım
        List<CourseSection> sections = courseSectionRepository.findAll();
        for (CourseSection section : sections) {
            if (section.getCourse() != null && section.getCourse().getId().equals(course.getId())) {
                
                // 2. Bu şubeye bağlı kayıtları (Enrollment) bulup silelim
                List<Enrollment> enrollments = enrollmentRepository.findAll();
                for(Enrollment enrollment : enrollments){
                     if(enrollment.getCourseSection() != null && enrollment.getCourseSection().getId().equals(section.getId())){
                         enrollmentRepository.delete(enrollment);
                     }
                }
                // 3. Şubeyi silelim
                courseSectionRepository.delete(section);
            }
        }
        
        // 4. En son dersi silelim
        courseRepository.delete(course);
        System.out.println("Ders kaydı silindi. "+ id);
    }

    @Override
    public List<DtoCourse> courseAll() {
        List<Course> allCourse = courseRepository.findAll();
        List<DtoCourse> dtoCourses=new ArrayList<>();
        for (Course course:allCourse){
            dtoCourses.add(convertToDto(course));
        }
        return dtoCourses;
    }

    private DtoCourse convertToDto(Course course) {
        DtoCourse dto = new DtoCourse();
        BeanUtils.copyProperties(course, dto);
        dto.setId(course.getId());
        if (course.getCreatedDate() != null) {
            dto.setCreatedDate(course.getCreatedDate().toString());
        } else {
            dto.setCreatedDate(LocalDateTime.now().toString());
        }
        return dto;
    }
}
