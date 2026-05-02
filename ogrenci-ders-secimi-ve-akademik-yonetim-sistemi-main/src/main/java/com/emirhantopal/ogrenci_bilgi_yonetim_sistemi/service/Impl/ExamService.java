package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExam;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExamIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Exam;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.ExamRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.EmailService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IExamService;
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
public class ExamService implements IExamService {

    private final ExamRepository examRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EmailService emailService;

    @Autowired
    public ExamService(ExamRepository examRepository, 
                       CourseSectionRepository courseSectionRepository,
                       EnrollmentRepository enrollmentRepository,
                       EmailService emailService) {
        this.examRepository = examRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.emailService = emailService;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    @Transactional
    public DtoExam addExam(DtoExamIU dtoExamIU) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(dtoExamIU, exam);
        try {
            exam.setExamDate(LocalDateTime.parse(dtoExamIU.getExamDate(), formatter));
        } catch (DateTimeParseException e) {
            throw new BaseException(MessageType.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
        }

        CourseSection courseSection = courseSectionRepository.findById(dtoExamIU.getCourseSectionId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));
        exam.setCourseSection(courseSection);

        Exam savedExam = examRepository.save(exam);

        // Sınav eklendiğinde derse kayıtlı tüm öğrencilere e-posta gönder
        List<Enrollment> enrollments = enrollmentRepository.findByCourseSection_Id(courseSection.getId());
        String courseName = courseSection.getCourse() != null ? courseSection.getCourse().getName() : "Ders";
        
        new Thread(() -> {
            for (Enrollment enrollment : enrollments) {
                Student student = enrollment.getStudent();
                if (student != null && student.getEmail() != null && !student.getEmail().isEmpty()) {
                    String studentName = student.getFirstName() + " " + student.getLastName();
                    try {
                        emailService.sendExamNotificationEmail(
                            student.getEmail(), 
                            studentName, 
                            courseName, 
                            savedExam.getExamName(), 
                            savedExam.getClassroom(), 
                            savedExam.getExamDate()
                        );
                    } catch (Exception e) {
                        System.err.println("Sınav bildirimi e-postası gönderilemedi: " + e.getMessage());
                    }
                }
            }
        }).start();

        return convertToDto(savedExam);
    }

    @Override
    @Transactional
    public DtoExam updateExam(Long id, DtoExamIU dtoExamIU) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_EXAM_ID, HttpStatus.BAD_REQUEST));

        exam.setExamName(dtoExamIU.getExamName());
        try {
            exam.setExamDate(LocalDateTime.parse(dtoExamIU.getExamDate(), formatter));
        } catch (DateTimeParseException e) {
            throw new BaseException(MessageType.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
        }
        exam.setClassroom(dtoExamIU.getClassroom());

        CourseSection courseSection = courseSectionRepository.findById(dtoExamIU.getCourseSectionId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));
        exam.setCourseSection(courseSection);

        Exam updatedExam = examRepository.save(exam);
        return convertToDto(updatedExam);
    }

    @Override
    @Transactional
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_EXAM_ID, HttpStatus.BAD_REQUEST));
        examRepository.delete(exam);
    }

    @Override
    public DtoExam getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_EXAM_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(exam);
    }

    @Override
    public List<DtoExam> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<DtoExam> getExamsByCourseSection(Long courseSectionId) {
        List<Exam> exams = examRepository.findByCourseSection_Id(courseSectionId);
        return exams.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private DtoExam convertToDto(Exam exam) {
        DtoExam dto = new DtoExam();
        BeanUtils.copyProperties(exam, dto);
        dto.setId(exam.getId());
        if (exam.getExamDate() != null) {
            dto.setExamDate(exam.getExamDate().format(formatter));
        }
        if (exam.getCreatedDate() != null) {
            dto.setCreatedDate(exam.getCreatedDate().toString());
        }
        if (exam.getCourseSection() != null) {
            DtoCourseSection dtoCourseSection = new DtoCourseSection();
            BeanUtils.copyProperties(exam.getCourseSection(), dtoCourseSection);
            dtoCourseSection.setId(exam.getCourseSection().getId());

            if (exam.getCourseSection().getCourse() != null) {
                DtoCourse dtoCourse = new DtoCourse();
                BeanUtils.copyProperties(exam.getCourseSection().getCourse(), dtoCourse);
                dtoCourse.setId(exam.getCourseSection().getCourse().getId());
                dtoCourseSection.setCourse(dtoCourse);
            }
            if (exam.getCourseSection().getTeacher() != null) {
                DtoTeacher dtoTeacher = new DtoTeacher();
                BeanUtils.copyProperties(exam.getCourseSection().getTeacher(), dtoTeacher);
                dtoTeacher.setId(exam.getCourseSection().getTeacher().getId());
                dtoCourseSection.setTeacher(dtoTeacher);
            }
            dto.setCourseSection(dtoCourseSection);
        }
        return dto;
    }
}
