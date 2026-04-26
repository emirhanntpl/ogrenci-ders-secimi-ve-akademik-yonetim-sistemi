package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IRestAuthController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.IAuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestAuthController implements IRestAuthController {

    @Autowired
    private IAuthServiceImpl authService;

    @PostMapping("/register")
    @Override
    public DtoUser register(@Valid @RequestBody AuthRequest request, @RequestParam(required = false) Role role) {
        return authService.register(request, role);
    }

    @PostMapping("/authenticate")
    @Override
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @GetMapping("/users/all")
    @Override
    public List<DtoUser> getAllUsers() {
        return authService.getAllUsers();
    }

    @DeleteMapping("/users/delete/{id}")
    @Override
    public void deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
    }
}
