package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.CanceledAppointments;
import org.springframework.stereotype.Service;

@Service
public interface WorkDateIntervalsCanceledAppointmentsManager {
    void saveCanceledAppointment(CanceledAppointments canceledAppointment);
}
