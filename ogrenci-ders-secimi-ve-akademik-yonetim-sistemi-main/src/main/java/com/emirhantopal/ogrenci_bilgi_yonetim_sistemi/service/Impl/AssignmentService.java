package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Assignment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.AssignmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.EmailService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAssignmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService implements IAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EmailService emailService;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, 
                             CourseSectionRepository courseSectionRepository,
                             EnrollmentRepository enrollmentRepository,
                             EmailService emailService) {
        this.assignmentRepository = assignmentRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.emailService = emailService;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    @Transactional
    public DtoAssignment addAssignment(DtoAssignmentIU dtoAssignmentIU) {
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(dtoAssignmentIU, assignment);
        
        try {
            assignment.setDueDate(LocalDateTime.parse(dtoAssignmentIU.getDueDate(), formatter));
        } catch (DateTimeParseException e) {
            throw new BaseException(MessageType.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
        }

        CourseSection courseSection = courseSectionRepository.findById(dtoAssignmentIU.getCourseSectionId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));
        assignment.setCourseSection(courseSection);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Ödev eklendiğinde derse kayıtlı tüm öğrencilere e-posta gönder
        List<Enrollment> enrollments = enrollmentRepository.findByCourseSection_Id(courseSection.getId());
        String courseName = courseSection.getCourse() != null ? courseSection.getCourse().getName() : "Ders";
        
        new Thread(() -> {
            for (Enrollment enrollment : enrollments) {
                Student student = enrollment.getStudent();
                if (student != null && student.getEmail() != null && !student.getEmail().isEmpty()) {
                    String studentName = student.getFirstName() + " " + student.getLastName();
                    try {
                        emailService.sendAssignmentNotificationEmail(
                            student.getEmail(), 
                            studentName, 
                            courseName, 
                            savedAssignment.getTitle(), 
                            savedAssignment.getDescription(), 
                            savedAssignment.getDueDate()
                        );
                    } catch (Exception e) {
                        System.err.println("Ödev bildirimi e-postası gönderilemedi: " + e.getMessage());
                    }
                }
            }
        }).start();

        return convertToDto(savedAssignment);
    }

    @Override
    @Transactional
    public DtoAssignment updateAssignment(Long id, DtoAssignmentIU dtoAssignmentIU) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ASSIGNMENT_ID, HttpStatus.BAD_REQUEST));

        assignment.setTitle(dtoAssignmentIU.getTitle());
        assignment.setDescription(dtoAssignmentIU.getDescription());
        
        try {
            assignment.setDueDate(LocalDateTime.parse(dtoAssignmentIU.getDueDate(), formatter));
        } catch (DateTimeParseException e) {
            throw new BaseException(MessageType.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
        }

        CourseSection courseSection = courseSectionRepository.findById(dtoAssignmentIU.getCourseSectionId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));
        assignment.setCourseSection(courseSection);

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return convertToDto(updatedAssignment);
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ASSIGNMENT_ID, HttpStatus.BAD_REQUEST));
        assignmentRepository.delete(assignment);
    }

    @Override
    public DtoAssignment getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ASSIGNMENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(assignment);
    }

    @Override
    public List<DtoAssignment> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return assignments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<DtoAssignment> getAssignmentsByCourseSection(Long courseSectionId) {
        List<Assignment> assignments = assignmentRepository.findByCourseSection_Id(courseSectionId);
        return assignments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private DtoAssignment convertToDto(Assignment assignment) {
        DtoAssignment dto = new DtoAssignment();
        BeanUtils.copyProperties(assignment, dto);
        dto.setId(assignment.getId());
        if (assignment.getDueDate() != null) {
            dto.setDueDate(assignment.getDueDate().format(formatter));
        }
        if (assignment.getCreatedDate() != null) {
            dto.setCreatedDate(assignment.getCreatedDate().toString());
        }
        if (assignment.getCourseSection() != null) {
            DtoCourseSection dtoCourseSection = new DtoCourseSection();
            BeanUtils.copyProperties(assignment.getCourseSection(), dtoCourseSection);
            dtoCourseSection.setId(assignment.getCourseSection().getId());
            
            if (assignment.getCourseSection().getCourse() != null) {
                DtoCourse dtoCourse = new DtoCourse();
                BeanUtils.copyProperties(assignment.getCourseSection().getCourse(), dtoCourse);
                dtoCourse.setId(assignment.getCourseSection().getCourse().getId());
                dtoCourseSection.setCourse(dtoCourse);
            }
            dto.setCourseSection(dtoCourseSection);
        }
        return dto;
    }
}