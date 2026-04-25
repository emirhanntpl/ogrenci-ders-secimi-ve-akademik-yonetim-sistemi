package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroomIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Classroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.ClassroomRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IClassroomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomService implements IClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;


    @Override
    public DtoClassroom classroomAdd(DtoClassroomIU dtoClassroomIU) {
        Classroom classroom=new Classroom();
        DtoClassroom dtoClassroom = new DtoClassroom();
        classroom.setCapacity(dtoClassroomIU.getCapacity());
        classroom.setRoomNumber(dtoClassroomIU.getRoomNumber());
        Classroom savedClassroom = classroomRepository.save(classroom);
        BeanUtils.copyProperties(savedClassroom, dtoClassroom);
        return dtoClassroom;
    }

    @Override
    public void classroomDelete(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
        classroomRepository.delete(classroom);
        System.out.println("Classroom silindi. " + id);
    }

    @Override
    public DtoClassroom classroomUpdate(Long id, DtoClassroomIU dtoClassroomIU) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
        DtoClassroom dtoClassroom=new DtoClassroom();
        classroom.setCapacity(dtoClassroomIU.getCapacity());
        classroom.setRoomNumber(dtoClassroomIU.getRoomNumber());
        Classroom savedClassroom = classroomRepository.save(classroom);
        BeanUtils.copyProperties(savedClassroom, dtoClassroom);
        return dtoClassroom;
    }

    @Override
    public DtoClassroom findByClassroomId(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
        DtoClassroom dtoClassroom=new DtoClassroom();
        BeanUtils.copyProperties(classroom, dtoClassroom);
        return dtoClassroom;
    }

    @Override
    public List<DtoClassroom> getAllClassroom() {
        List<Classroom> allClassroom = classroomRepository.findAll();
        List<DtoClassroom> dtoClassrooms=new ArrayList<>();
        for (Classroom classroom:allClassroom){
            DtoClassroom dtoClassroom=new DtoClassroom();
            dtoClassroom.setCapacity(classroom.getCapacity());
            dtoClassroom.setRoomNumber(classroom.getRoomNumber());
            dtoClassrooms.add(dtoClassroom);
        }
        return dtoClassrooms;
    }
}
