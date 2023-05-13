package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.WorkerDAO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.*;
import com.example.deansoffice.exception.*;
import com.example.deansoffice.model.*;
import com.example.deansoffice.record.WorkDayIntervalsGetResponse;
import com.example.deansoffice.record.WorkerIntervalsToWorkDayPostRequest;
import com.example.deansoffice.record.WorkerWorkDayPostRequest;
import com.example.deansoffice.service.*;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.Fetcher.StudentFetcher;
import com.example.deansoffice.service.Manager.AdminWorkerManager;
import com.example.deansoffice.service.Manager.WorkerWorkDateIntervalsManager;
import com.example.deansoffice.service.Manager.WorkerWorkDateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService, AdminWorkerManager {
    private WorkerDAO workerDAO;

    private WorkerWorkDateManager workerWorkDateManager;

    private WorkerWorkDateIntervalsManager workerWorkDateIntervalsManager;

    private SpecializationFetcher specializationFetcher;
    private StudentFetcher studentFetcher;


    @Autowired
    public WorkerServiceImpl(WorkerDAO theWorkerDAO, WorkerWorkDateIntervalsManager theWorkerWorkDateIntervalsManager, WorkerWorkDateManager theWorkerWorkDateManager, SpecializationFetcher theSpecializationFetcher, StudentFetcher theStudentFetcher) {
        workerDAO = theWorkerDAO;
        specializationFetcher = theSpecializationFetcher;
        workerWorkDateManager = theWorkerWorkDateManager;
        workerWorkDateIntervalsManager = theWorkerWorkDateIntervalsManager;
        studentFetcher = theStudentFetcher;
    }

    public Optional<Worker> getWorkerById(int id) {
        return workerDAO.findById(id);
    }

    @Override
    public List<WorkerDTO> getWorkers() {
        try {
            List<Worker> workerEntities = workerDAO.findAll();
            return WorkerDTO.fromEntities(workerEntities);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get workers");
        }
    }

    @Override
    public void saveWorker(Worker worker) {
        workerDAO.save(worker);
    }

    @Override
    public ResponseEntity<Response> deleteSingleWorkDate(Integer workerId, Integer workdayId) {
        try {
            Optional<Worker> worker = workerDAO.findById(workerId);

            if(worker.isEmpty()) {
                throw new RecordNotFoundException("Worker not found");
            }
            return workerWorkDateManager.deleteSingleWorkDate(workerId, workdayId);
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work date");
        }
    }

    @Override
    public ResponseEntity<Response> deleteListOfWorkDates(Integer workerId, List<Integer> workDatesListId) {
        try {
            Optional<Worker> worker = workerDAO.findById(workerId);

            if(worker.isEmpty()) {
                throw new RecordNotFoundException("Worker not found");
            }
            return workerWorkDateManager.deleteListOfWorkDates(workerId, workDatesListId);
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work dates");
        }
    }

    @Override
    public ResponseEntity<Response> deleteSingleWorkDateInterval(Integer workerId, Integer workdateIntervalId) {
        try {
            Optional<Worker> worker = workerDAO.findById(workerId);

            if(worker.isEmpty()) {
                throw new RecordNotFoundException("Worker not found");
            }
            return workerWorkDateIntervalsManager.deleteSingleWorkDateInterval(workerId, workdateIntervalId);
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work date interval");
        }
    }

    @Override
    public ResponseEntity<Response> deleteListOfWorkDatesIntervals(Integer workerId, List<Integer> workDatesIntervalsListId) {
        try {
            Optional<Worker> worker = workerDAO.findById(workerId);

            if(worker.isEmpty()) {
                throw new RecordNotFoundException("Worker not found");
            }
            return workerWorkDateIntervalsManager.deleteListOfWorkDatesIntervals(workerId,workDatesIntervalsListId);
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work date intervals");
        }
    }


    @Override
    public Worker addNewSpecializationsToWorker(Worker worker, List<Integer> specializationsIdList) {
        try {
            specializationsIdList.forEach(specializationId -> {
                Optional<Specialization> specialization = specializationFetcher.getSpecializationById(specializationId);

                if (specialization.isPresent()) {
                    worker.getSpecializations().add(specialization.get());
                } else {
                    throw new RecordNotFoundException("Specialization not found");
                }
            });
            workerDAO.save(worker);
            return worker;
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to add specializations to worker");
        }
    }

    @Override
    public ResponseEntity<WorkDayIntervalsGetResponse> getWorkDayIntervals(int id, long date) {
        try {
            Optional<Worker> worker = workerDAO.findById(id);
            if (worker.isPresent()) {
                LocalDate convertedDate = Instant.ofEpochMilli(date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                Optional<WorkDate> workDate = worker.get().getWorkDates().stream()
                        .filter(d -> d.getDate().equals(convertedDate))
                        .findFirst();
                if (workDate.isPresent()) {
                    List<WorkDateIntervals> workDateIntervalsList = workDate.get().getWorkDateIntervals();
                    if (workDateIntervalsList != null) {
                        return ResponseEntity.status(HttpStatus.OK).body(new WorkDayIntervalsGetResponse(workDateIntervalsList.stream()
                                .filter(w -> !w.getTaken())
                                .map(w -> w.getStartInterval().toString() + " - " + w.getEndInterval().toString())
                                .collect(Collectors.toList())));
                    } else {
                        throw new RecordNotFoundException("Interval not found");
                    }
                } else {
                    throw new RecordNotFoundException("Work date not found");
                }
            } else {
                throw new RecordNotFoundException("Worker not found");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get work day intervals");
        }
    }
    @Override
    public ResponseEntity<Response> createNewWorkDayForWorkerWithIntervals(int workerId, String interval, WorkerWorkDayPostRequest request) {
        try {
            Optional<Worker> worker = workerDAO.findById(workerId);

            if (worker.isPresent()) {
                WorkDate workDate = workerWorkDateManager.newWorkDateForUser(worker.get(), request.date(), request.startTime(), request.endTime());
                workerWorkDateIntervalsManager.createIntervals(workDate, Integer.parseInt(interval));
                return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Worker work day with intervals created"));
            } else {
                throw new RecordNotFoundException("Worker not found");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create worker work day with intervals");
        }
    }

    @Override
    public ResponseEntity<Response> createNewWorkDayForWorkerWithoutIntervals(int id, WorkerWorkDayPostRequest request) {
        try {
            Optional<Worker> worker = workerDAO.findById(id);

            if (worker.isPresent()) {
                workerWorkDateManager.newWorkDateForUser(worker.get(), request.date(), request.startTime(), request.endTime());
                return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Worker work day without intervals created"));
            } else {
                throw new RecordNotFoundException("Worker not found");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create work day without intervals");
        }
    }

    @Override
    public ResponseEntity<Response> addIntervalsToWorkDay(int workerId, int workDayId, WorkerIntervalsToWorkDayPostRequest intervalsList) {
        try {
            Optional<Worker> worker = workerDAO.findById(workerId);

            if (worker.isPresent()) {
                Optional<WorkDate> workDate = workerWorkDateManager.findWorkDateById(workDayId);

                if (workDate.isPresent()) {
                    WorkDate workDateSave = workDate.get();
                    LocalTime workDateStart = workDateSave.getStart_time();
                    LocalTime workDateEnd = workDateSave.getEnd_time();
                    List<Pair<LocalTime, LocalTime>> existingIntervals = workerWorkDateIntervalsManager.getIntervalsByWorkerIdAndWorkDateId(workerId, workDayId);

                    for (Pair<String, String> interval : intervalsList.intervals()) {
                        LocalTime start = LocalTime.parse(interval.getFirst());
                        LocalTime end = LocalTime.parse(interval.getSecond());

                        for (Pair<LocalTime, LocalTime> existingInterval : existingIntervals) {
                            if ((start.isBefore(existingInterval.getSecond()) && existingInterval.getFirst().isBefore(end))) {
                                throw new BadRequestException("New interval overlaps with existing interval");
                            } else if ((start.isBefore(workDateStart) || end.isAfter(workDateEnd))) {
                                throw new BadRequestException("New intervals must be within the working hours of the start and end time");
                            }
                        }
                    }

                    for (Pair<String, String> interval : intervalsList.intervals()) {
                        LocalTime start = LocalTime.parse(interval.getFirst());
                        LocalTime end = LocalTime.parse(interval.getSecond());
                        WorkDateIntervals workDateInterval = new WorkDateIntervals();
                        workDateInterval.setStartInterval(start);
                        workDateInterval.setEndInterval(end);
                        workDateInterval.setStudent(null);
                        workDateInterval.setTaken(false);
                        workDateInterval.setDescription(null);
                        workDateInterval.setWorkDate(workDateSave);
                        workerWorkDateIntervalsManager.saveWorkDateInterval(workDateInterval);
                    }

                    return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Intervals added to worker work day"));
                } else {
                    throw new IllegalArgumentException("Work date not found");
                }
            } else {
                throw new IllegalArgumentException("Worker not found");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to add interval to worker work day");
        }
    }

    @Override
    public ResponseEntity<Response> makeAppointment(int workerId, long date, String time, int studentId, Map<String, String> descriptionBody) {
        try {
            Optional<Student> student = studentFetcher.getStudentById(studentId);

            if (student.isEmpty())
                throw new RecordNotFoundException("Student not found");

            Optional<Worker> worker = workerDAO.findById(workerId);

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

                        workerWorkDateIntervalsManager.saveWorkDateInterval(workDateInterval.get());
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
