package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.CanceledAppointmentsDAO;
import com.example.deansoffice.service.CanceledAppointmentsService;
import org.springframework.stereotype.Service;

@Service
public class CanceledAppointmentsServiceImpl implements CanceledAppointmentsService {
    private CanceledAppointmentsDAO canceledAppointmentsDAO;
    CanceledAppointmentsServiceImpl(CanceledAppointmentsDAO theCanceledAppointmentsDAO) {
        canceledAppointmentsDAO = theCanceledAppointmentsDAO;
    }
}
