package com.example.deansoffice.service;

import com.example.deansoffice.dao.TokenDAO;
import com.example.deansoffice.entity.*;
import com.example.deansoffice.model.AuthenticationResponse;
import com.example.deansoffice.model.RegisterStudent;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.model.TokenType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.deansoffice.model.AuthenticationRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final StudentService studentService;
  private final LoginService loginService;
  private final SpecializationService specializationService;
  private final SpecializationMajorYearService specializationMajorYearService;
  private final StudentMajorDetailsService studentMajorDetailsService;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthenticationResponse register(RegisterStudent registerStudent) {
    Student student = new Student();
    student.setName(registerStudent.getName());
    student.setSurname(registerStudent.getSurname());

    Login login = studentService.addStudent(student, registerStudent.getUsername(), registerStudent.getPassword(), Role.USER);

    Integer specializationId = specializationService.findBySpecializationByNameAndCourse(registerStudent.getMajor(), registerStudent.getSpecialization()).getId();

    SpecializationMajorYear specializationMajorYear = specializationMajorYearService.findSpecializationMajorYearByMajorYearIdAndSpecializationId(registerStudent.getYear(), specializationId);//specializationId);

    StudentMajorDetails studentMajorDetails = new StudentMajorDetails();
    studentMajorDetails.setStudent(student);
    studentMajorDetails.setSpecializationMajorYear(specializationMajorYear);

    studentMajorDetailsService.addStudentMajorDetails(studentMajorDetails);

    var jwtToken = jwtService.generateToken(login);
    saveLoginToken(login, jwtToken);

    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var login = loginService.findByUsername(request.getUsername())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(login);
    revokeAllLoginTokens(login);
    saveLoginToken(login, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  private void saveLoginToken(Login login, String jwtToken) {
    var token = Token.builder()
        .login(login)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenService.saveToken(token);
  }

  private void revokeAllLoginTokens(Login login) {
    var validUserTokens = tokenService.findAllValidTokenByUser(login.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenService.saveAllTokens(validUserTokens);
  }
}