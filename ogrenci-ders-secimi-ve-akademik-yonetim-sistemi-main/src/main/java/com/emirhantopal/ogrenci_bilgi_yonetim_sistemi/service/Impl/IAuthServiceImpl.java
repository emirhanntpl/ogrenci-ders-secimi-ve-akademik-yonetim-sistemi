package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.JwtService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.RefreshToken;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.RefreshTokenRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.UserRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class IAuthServiceImpl implements IAuthService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Override
    public DtoUser register(AuthRequest request) {
        DtoUser dto=new DtoUser();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        BeanUtils.copyProperties(savedUser, dto);
        return dto;
    }


    public DtoUser registerAdmin(AuthRequest request){
        DtoUser dtoAdmin=new DtoUser();
        User userAdmin=new User();
        userAdmin.setUsername(request.getUsername());
        userAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        userAdmin.setRole(Role.ADMIN);
        User savedAdmin = userRepository.save(userAdmin);
        BeanUtils.copyProperties(savedAdmin, dtoAdmin);
        return dtoAdmin;

    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {

        try {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
            authenticationProvider.authenticate(auth);

            Optional<User> userName = userRepository.findByUsername(request.getUsername());

            String accessToken = jwtService.generateToken(userName.get());

            RefreshToken refreshToken = createRefreshToken(userName.get());

            refreshTokenRepository.save(refreshToken);

            return new AuthResponse(accessToken,refreshToken.getRefreshToken());

        }catch (Exception e){
            System.out.println("Kullanıcı adı veya şifre hatalı, tekrar deneyiniz." + e.getMessage());
        }

        return null;
    }

    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() +1000*60*60*12));
        refreshToken.setUser(user);

        return  refreshToken;
    }




}
