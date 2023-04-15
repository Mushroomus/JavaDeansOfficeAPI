package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.WorkerMapper;
import com.example.deansoffice.dao.WorkerDAO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.*;
import com.example.deansoffice.exception.*;
import com.example.deansoffice.model.NewWorkDayRequest;
import com.example.deansoffice.model.Pair;
import com.example.deansoffice.service.*;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.Fetcher.StudentFetcher;
import com.example.deansoffice.service.Manager.AdminWorkerManager;
import com.example.deansoffice.service.Manager.WorkerWorkDateIntervalsManager;
import com.example.deansoffice.service.Manager.WorkerWorkDateManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    private WorkerMapper workerMapper;

    @Autowired
    public WorkerServiceImpl(WorkerDAO theWorkerDAO, WorkerWorkDateIntervalsManager theWorkerWorkDateIntervalsManager, WorkerWorkDateManager theWorkerWorkDateManager, SpecializationFetcher theSpecializationFetcher, StudentFetcher theStudentFetcher) {
        workerDAO = theWorkerDAO;
        specializationFetcher = theSpecializationFetcher;
        workerWorkDateManager = theWorkerWorkDateManager;
        workerWorkDateIntervalsManager = theWorkerWorkDateIntervalsManager;
        studentFetcher = theStudentFetcher;
    }

    public List<WorkerDTO> getWorkers() {
        return workerMapper.toDTOList(workerDAO.findAll());
    }

    public Optional<Worker> getWorkerById(int id) {
        return workerDAO.findById(id);
    }

    @Override
    public void saveWorker(Worker worker) {
        workerDAO.save(worker);
    }

    @Override
    public Worker addNewSpecializationsToWorker(Worker worker, List<Integer> specializationsIdList) {
        specializationsIdList.forEach(specializationId -> {
            Optional<Specialization> specialization = specializationFetcher.getSpecializationById(specializationId);

            if(specialization.isPresent()) {
                worker.getSpecializations().add(specialization.get());
            } else {
                throw new SpecializationNotFoundException();
            }
        });
        workerDAO.save(worker);
        return worker;
    }

    @Override
    public List<String> getWorkDayIntervals(int id, long date) {
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
                    return workDateIntervalsList.stream()
                            .filter(w -> !w.getTaken())
                            .map(w -> w.getStartInterval().toString() + " - " + w.getEndInterval().toString())
                            .collect(Collectors.toList());
                    } else {
                        throw new IntervalNotFoundException();
                    }
                } else {
                    throw new WorkDateNotFoundException();
                }
            } else {
                throw new WorkerNotFoundException();
            }
        }

    public void createNewWorkDayForWorkerWithIntervals(int workerId, String interval, NewWorkDayRequest request) {
        Optional<Worker> worker = workerDAO.findById(workerId);

        if(worker.isPresent()) {
            WorkDate workDate = workerWorkDateManager.newWorkDateForUser(worker.get(), request.getDate(), request.getStartTime(), request.getEndTime());
            workerWorkDateIntervalsManager.createIntervals(workDate, Integer.parseInt(interval));
        } else {
            throw new WorkerNotFoundException();
        }
    }

    @Override
    public void createNewWorkDayForWorkerWithoutIntervals(int id, NewWorkDayRequest request) {
        Optional<Worker> worker = workerDAO.findById(id);

        if(worker.isPresent()) {
            workerWorkDateManager.newWorkDateForUser(worker.get(), request.getDate(), request.getStartTime(), request.getEndTime());
        } else {
            throw new WorkerNotFoundException();
        }
    }

    @Override
    public void addIntervalsToWorkDay(int workerId, int workDayId, List<Pair<String,String>> intervals) {
        Optional<Worker> worker = workerDAO.findById(workerId);

        if(worker.isPresent()) {
            Optional<WorkDate> workDate = workerWorkDateManager.findWorkDateById(workDayId);

            if(workDate.isPresent()) {
                WorkDate workDateSave = workDate.get();
                LocalTime workDateStart = workDateSave.getStart_time();
                LocalTime workDateEnd = workDateSave.getEnd_time();
                List<Pair<LocalTime,LocalTime>> existingIntervals = workerWorkDateIntervalsManager.getIntervalsByWorkerIdAndWorkDateId(workerId, workDayId);

                for(Pair<String,String> interval : intervals) {
                    LocalTime start = LocalTime.parse(interval.getFirst());
                    LocalTime end = LocalTime.parse(interval.getSecond());

                    for (Pair<LocalTime,LocalTime> existingInterval : existingIntervals) {
                        if ( (start.isBefore(existingInterval.getSecond()) && existingInterval.getFirst().isBefore(end))) {
                            throw new IllegalArgumentException("New interval overlaps with existing interval");
                        } else if((start.isBefore(workDateStart) || end.isAfter(workDateEnd))) {
                            throw new IllegalArgumentException("New intervals must be within the working hours of the start and end time");
                        }
                    }
                }

                for (Pair<String,String> interval : intervals) {
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
            } else {
                throw new IllegalArgumentException("Work date not found");
            }
        } else {
            throw new IllegalArgumentException("Worker not found");
        }
    }

    public void makeAppointment(int workerId, long date, String time, int studentId, Map<String, String> descriptionBody) {

        Optional<Student> student = studentFetcher.getStudentById(studentId);

        if(!student.isPresent())
            throw new StudentNotFoundException();

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

                    if(descriptionBody != null)
                        interval.setDescription(descriptionBody.get("description"));

                    workerWorkDateIntervalsManager.saveWorkDateInterval(workDateInterval.get());
                } else {
                    throw new IntervalNotFoundException();
                }
            } else {
                throw new WorkDateNotFoundException();
            }
        } else {
            throw new WorkerNotFoundException();
        }
    }
}
