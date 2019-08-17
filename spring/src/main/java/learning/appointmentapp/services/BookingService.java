package learning.appointmentapp.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import learning.appointmentapp.entities.Appointment;
import learning.appointmentapp.entities.Employee;
import learning.appointmentapp.repositories.AppointmentRepository;
import learning.appointmentapp.repositories.EmployeeRepository;

@Service
public class BookingService {
    @Autowired
    AppointmentRepository appointmentRepo;

    @Autowired
    EmployeeRepository employeeRepo;

    public List<LocalDateTime> checkAppointment(Employee employee) {
        // get all the appointments for the employee
        // extract the timeslot from each appointment
        // return the list of timeslots

        // Set<Appointment> appointments = employee.getAppointments();
        List<Appointment> appointments = appointmentRepo.findByEmployee(employee);

        ArrayList<LocalDateTime> results = new ArrayList<LocalDateTime>();

        for (Appointment a: appointments) {  
        
            results.add(a.getTimeslot());

        }
      
        return results;
    }

    public Appointment bookAppointment(LocalDateTime timeslot, Employee employee) {
       
        LocalDateTime startDateTime = timeslot.minusHours(2);
        LocalDateTime endDateTime = timeslot.plusHours(2);

        List<Appointment> results = appointmentRepo.findByTimeslotBetween(startDateTime, endDateTime);

        System.out.println("RESULTS.SIZE(): " + results.size());
        if (results.size() == 0) {
            
            Appointment appointment = new Appointment();
            appointment.setTimeslot(timeslot);
            appointment.setEmployee(employee);
            appointmentRepo.save(appointment);

            return appointment;

        } else {

            return null;
        }
    }
}