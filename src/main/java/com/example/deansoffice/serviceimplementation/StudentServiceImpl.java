package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.StudentMapper;
import com.example.deansoffice.dao.StudentDAO;
import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.service.StudentService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.deansoffice.dao.LoginDAO;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private StudentDAO studentDAO;
    private LoginDAO loginDAO;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentDAO theStudentDAO, LoginDAO theLoginDAO) {
        studentDAO = theStudentDAO;
        loginDAO = theLoginDAO;
    }

    @Override
    public Login addStudent(Student student, String username, String password, Role role) {
        studentDAO.save(student);
        System.out.println(student);

        Login login = new Login();
        login.setUsername(username);
        login.setStudent(student);
        login.setRole(role);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        login.setPassword(hashedPassword);

        loginDAO.save(login);
        return login;
    }

    @Override
    public Optional<Student> getStudentById(int id) {
        return studentDAO.findById(id);
    }
}
