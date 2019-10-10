package com.michonski.football.service;

import com.michonski.football.dto.security.RegisterUser;
import com.michonski.football.exception.AppException;
import com.michonski.football.model.security.User;
import com.michonski.football.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 1. metoda ktora dodaje usera przekazanego jako RegisterUser do bazy
    // metoda sprawdza najpierw czy:
    // - username nie jest null i czy juz takiego username nie ma w db
    // - czy password jest takie samo jak passwordConfirmation
    // pozniej mapowanie do User i dodanie do db

    public String registerUserName(RegisterUser registerUser){

        if(registerUser == null){
            throw new AppException("user shouldn't be null");
        }

        if(registerUser.getUsername() == null){
            throw new AppException("username shouldn't be null");
        }

        if ( userRepository.findByUsername(registerUser.getUsername()).isPresent() ) {
            throw new AppException("This user already exist");
        }
        if (!Objects.equals(registerUser.getPassword(), registerUser.getPasswordConfirmation())){
            throw new AppException("password be confirmed");
        }

        registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        User userFromDb = userRepository.save(ModelMapper.fromRegisterUserToUser(registerUser));
        return userFromDb.getUsername();
    }
}
