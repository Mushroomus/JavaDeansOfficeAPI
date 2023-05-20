package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.StudentDAO;
import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.StudentDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.*;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.record.StudentAppointmentGetResponse;
import com.example.deansoffice.record.WorkDayIntervalsGetResponse;
import com.example.deansoffice.service.Fetcher.StudentFetcher;
import com.example.deansoffice.service.Fetcher.WorkDateFetcher;
import com.example.deansoffice.service.Fetcher.WorkerFetcher;
import com.example.deansoffice.service.Manager.StudentMajorDetailsManager;
import com.example.deansoffice.service.Manager.StudentWorkDateIntervalsManager;
import com.example.deansoffice.service.Manager.StudentWorkerManager;
import com.example.deansoffice.service.StudentService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.deansoffice.dao.LoginDAO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService, StudentFetcher {
    private StudentDAO studentDAO;
    private LoginDAO loginDAO;
    private StudentWorkDateIntervalsManager studentWorkDateIntervalsManager;
    private StudentMajorDetailsManager studentMajorDetailsManager;
    private StudentWorkerManager studentWorkerManager;
    private WorkDateFetcher workDateFetcher;
    private WorkerFetcher workerFetcher;


    @Autowired
    public StudentServiceImpl(StudentDAO theStudentDAO, LoginDAO theLoginDAO, StudentWorkDateIntervalsManager theStudentWorkDateIntervalsManager,
                              StudentMajorDetailsManager theStudentMajorDetailsManager, WorkDateFetcher theWorkDateFetcher, WorkerFetcher theWorkerFetcher) {
        studentDAO = theStudentDAO;
        loginDAO = theLoginDAO;
        studentWorkDateIntervalsManager = theStudentWorkDateIntervalsManager;
        studentMajorDetailsManager = theStudentMajorDetailsManager;
        workDateFetcher = theWorkDateFetcher;
        workerFetcher = theWorkerFetcher;
    }

    @Autowired
    public void setStudentWorkerManager(StudentWorkerManager theStudentWorkerManager) {
        studentWorkerManager = theStudentWorkerManager;
    }

    @Override
    public StudentDTO getStudent(Integer studentId) {
        try {
            Optional<Student> studentEntity = studentDAO.findById(studentId);
            if (studentEntity.isEmpty()) {
                throw new RecordNotFoundException("Student not found");
            }
            Student studentExists = studentEntity.get();
            return new StudentDTO(studentExists.getId(), studentExists.getName(), studentExists.getSurname());
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get student");
        }
    }

    @Override
    public Login addStudent(Student student, String username, String password, Role role) {
        studentDAO.save(student);

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
    public ResponseEntity<Response> updateStudent(Integer studentId, Student student) {
        try {
            Optional<Student> existingStudent = studentDAO.findById(studentId);
            if (existingStudent.isEmpty()) {
                throw new RecordNotFoundException("Student not found");
            }

            Student updateStudent = existingStudent.get();
            updateStudent.setName(student.getName());
            updateStudent.setSurname(student.getSurname());
            studentDAO.save(updateStudent);

            return ResponseEntity.status(HttpStatus.OK).body(new Response("Student updated"));
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update student");
        }
    }


    @Override
    public Optional<Student> getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public ResponseEntity<Response> cancelAppointment(int studentId, int appointmentId) {
        return studentWorkDateIntervalsManager.cancelAppointment(null, studentId, appointmentId);
    }
    @Override
    public ResponseEntity<List<StudentAppointmentGetResponse>> getAppointments(int studentId, String startInterval, String endInterval, LocalDate startDate, LocalDate endDate, Integer workerId) {
        return studentWorkDateIntervalsManager.findByStudentIdAndStartInvervalAndEndInterval(studentId, startInterval, endInterval, startDate, endDate, workerId);
    }

    @Override
    public ResponseEntity<Response> addMajorDetails(Integer studentId, Integer specializationMajorYearId) {

        try {
            Optional<Student> studentOptional = studentDAO.findById(studentId);

            if (studentOptional.isPresent()) {
                return studentMajorDetailsManager.addStudentMajorDetails(studentOptional.get(), specializationMajorYearId);
            } else {
                throw new RecordNotFoundException("Student not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to add major details to student");
        }
    }

    @Override
    public ResponseEntity<Response> deleteMajorDetails(Integer studentId, Integer majorDetailsId) {

        try {
            Optional<Student> studentOptional = studentDAO.findById(studentId);

            if (studentOptional.isPresent()) {
                return studentMajorDetailsManager.deleteStudentMajorDetails(studentOptional.get(), majorDetailsId);
            } else {
                throw new RecordNotFoundException("Student not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete major details from student");
        }
    }

    @Override
    public ResponseEntity<List<SpecializationMajorYearDTO>> getStudentSpecializationMajorYear(int studentId) {
        try {
            Optional<Student> studentOptional = studentDAO.findById(studentId);

            if (studentOptional.isEmpty()) {
                throw new RecordNotFoundException("Student not found");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(SpecializationMajorYearDTO.fromEntities(studentOptional.get().getSpecializationMajorYears()));
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to get student specialization major years");
        }
    }

    @Override
    public ResponseEntity<Response> editMajorDetails(Integer studentId, Integer studentMajorDetailsId, Integer requestBodyId) {
        try {
            Optional<Student> studentOptional = studentDAO.findById(studentId);

            if (studentOptional.isEmpty()) {
                throw new RecordNotFoundException("Student not found");
            } else {
                return studentMajorDetailsManager.editStudentMajorDetails(studentOptional.get(), studentMajorDetailsId, requestBodyId);
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to edit student major details");
        }
    }

    @Override
    public List<WorkerDTO> getWorkersWithOptionalMatch(Integer studentId, Boolean matchSpecializations) {
        try {
            if (matchSpecializations) {
                List<Integer> studentSpecializationIdList = studentDAO.getStudentSpecializationIdList(studentId);
                return studentWorkerManager.getWorkersBySpecializations(studentSpecializationIdList);
            } else {
                return studentWorkerManager.getWorkers();
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get workers");
        }
    }

    @Override
    public ResponseEntity<WorkDayIntervalsGetResponse> getWorkerWorkDayIntervals(Integer workerID, long date) {
        return studentWorkerManager.getWorkDayIntervals(workerID, date);
    }

    @Override
    public ResponseEntity<List<LocalDate>> getWorkDays(Integer workerId) {
        return workDateFetcher.getWorkDates(workerId);
    }


    @Override
    public ResponseEntity<Response> makeAppointment(int workerId, long date, String time, int studentId, Map<String, String> descriptionBody) {
        try {
            Optional<Student> student = studentDAO.findById(studentId);

            if (student.isEmpty())
                throw new RecordNotFoundException("Student not found");

            Optional<Worker> worker = workerFetcher.getWorkerById(workerId);

            if (worker.isPresent()) {

                LocalDate appointmentDate = Instant.ofEpochMilli(date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                LocalTime appointmentTime = LocalTime.parse(time);

                Optional<WorkDate> workDate = worker.get().getWorkDates().stream()
                        .filter(d -> d.getDate().equals(appointmentDate))
                        .findFirst();

                if (workDate.isPresent()) {
                    Optional<WorkDateIntervals> workDateInterval = workDate.get().getWorkDateIntervals().stream()
                            .filter(i -> i.getStartInterval().equals(appointmentTime))
                            .findFirst();

                    if (workDateInterval.isPresent()) {
                        WorkDateIntervals interval = workDateInterval.get();

                        interval.setTaken(true);
                        interval.setStudent(student.get());

                        if (descriptionBody != null)
                            interval.setDescription(descriptionBody.get("description"));

                        studentWorkDateIntervalsManager.saveWorkDateInterval(workDateInterval.get());
                        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Appointment created"));
                    } else {
                        throw new RecordNotFoundException("Interval not found");
                    }
                } else {
                    throw new RecordNotFoundException("Work date not found");
                }
            } else {
                throw new RecordNotFoundException("Worker not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to make an appointment");
        }
    }
}
