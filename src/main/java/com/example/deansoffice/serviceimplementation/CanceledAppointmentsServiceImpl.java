package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.CanceledAppointmentsDAO;
import com.example.deansoffice.entity.CanceledAppointments;
import com.example.deansoffice.service.Manager.WorkDateIntervalsCanceledAppointmentsManager;
import org.springframework.stereotype.Service;

@Service
public class CanceledAppointmentsServiceImpl implements WorkDateIntervalsCanceledAppointmentsManager {
    private CanceledAppointmentsDAO canceledAppointmentsDAO;
    CanceledAppointmentsServiceImpl(CanceledAppointmentsDAO theCanceledAppointmentsDAO) {
        canceledAppointmentsDAO = theCanceledAppointmentsDAO;
    }

    @Override
    public void saveCanceledAppointment(CanceledAppointments canceledAppointment) {
        canceledAppointmentsDAO.save(canceledAppointment);
    }
}
