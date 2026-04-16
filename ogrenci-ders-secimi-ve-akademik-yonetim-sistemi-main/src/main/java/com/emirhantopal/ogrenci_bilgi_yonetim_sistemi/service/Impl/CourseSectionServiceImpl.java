package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ICourseSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseSectionServiceImpl implements ICourseSectionService {

    @Autowired
    private CourseSectionRepository courseSectionRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public DtoCourseSection courseSectionAdd(DtoCourseSectionIU dtoIU) {
        CourseSection courseSection = new CourseSection();
        

        mapEntitiesToCourseSection(dtoIU, courseSection);

        CourseSection savedSection = courseSectionRepository.save(courseSection);
        
        return convertToDto(savedSection);
    }

    @Override
    public DtoCourseSection courseSectionUpdate(Long id, DtoCourseSectionIU dtoIU) {
        CourseSection courseSection = courseSectionRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));


        mapEntitiesToCourseSection(dtoIU, courseSection);

        CourseSection updatedSection = courseSectionRepository.save(courseSection);

        return convertToDto(updatedSection);
    }

    @Override
    public void courseSectionDelete(Long id) {
        CourseSection courseSection = courseSectionRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));
        courseSectionRepository.delete(courseSection);
    }

    @Override
    public List<DtoCourseSection> getAllCourseSections() {
        List<CourseSection> allSections = courseSectionRepository.findAll();
        if (allSections.isEmpty()) {
            throw new BaseException(MessageType.COURSE_SECTION_LIST_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        List<DtoCourseSection> dtoList = new ArrayList<>();
        for (CourseSection section : allSections) {
            dtoList.add(convertToDto(section));
        }
        return dtoList;
    }



    private void mapEntitiesToCourseSection(DtoCourseSectionIU dtoIU, CourseSection courseSection) {
        if (dtoIU.getCourseId() != null) {
            Course course = courseRepository.findById(dtoIU.getCourseId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_ID, HttpStatus.BAD_REQUEST));
            courseSection.setCourse(course);
        }

        if (dtoIU.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dtoIU.getTeacherId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
            courseSection.setTeacher(teacher);
        }

        if (dtoIU.getClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(dtoIU.getClassroomId())
                    .orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
            courseSection.setClassroom(classroom);
        }

        if (dtoIU.getSemesterId() != null) {
            Semester semester = semesterRepository.findById(dtoIU.getSemesterId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_SEMESTER_ID, HttpStatus.BAD_REQUEST));
            courseSection.setSemester(semester);
        }
    }

    private DtoCourseSection convertToDto(CourseSection courseSection) {
        DtoCourseSection dto = new DtoCourseSection();
        BeanUtils.copyProperties(courseSection, dto);

        if (courseSection.getCourse() != null) {
            DtoCourse dtoCourse = new DtoCourse();
            BeanUtils.copyProperties(courseSection.getCourse(), dtoCourse);
            dto.setCourse(dtoCourse);
        }

        if (courseSection.getTeacher() != null) {
            DtoTeacher dtoTeacher = new DtoTeacher();
            BeanUtils.copyProperties(courseSection.getTeacher(), dtoTeacher);
            dto.setTeacher(dtoTeacher);
        }

        if (courseSection.getClassroom() != null) {
            DtoClassroom dtoClassroom = new DtoClassroom();
            BeanUtils.copyProperties(courseSection.getClassroom(), dtoClassroom);
            dto.setClassroom(dtoClassroom);
        }

        if (courseSection.getSemester() != null) {
            DtoSemester dtoSemester = new DtoSemester();
            BeanUtils.copyProperties(courseSection.getSemester(), dtoSemester);
            dto.setSemester(dtoSemester);
        }

        return dto;
    }
}