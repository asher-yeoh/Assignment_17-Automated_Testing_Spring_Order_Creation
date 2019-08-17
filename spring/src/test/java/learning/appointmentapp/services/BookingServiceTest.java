package learning.appointmentapp.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import learning.appointmentapp.entities.Appointment;
import learning.appointmentapp.entities.Employee;
import learning.appointmentapp.repositories.AppointmentRepository;
import learning.appointmentapp.repositories.EmployeeRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookingServiceTest {

    @Autowired
    BookingService service;

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    AppointmentRepository appointmentRepo;


    @Test
    public void testCheckAppointment() {
        // Given
        // create an employee
        // create 2-3 appointments
        Employee employee = seedEmployee();
        seedAppointment(LocalDateTime.now().plusHours(1), employee);
        seedAppointment(LocalDateTime.now().plusHours(3), employee);
        seedAppointment(LocalDateTime.now().plusHours(5), employee);

        // When
        // run checkAppointment
        List<LocalDateTime> results = service.checkAppointment(employee);

        // Then
        // result should be an array of LocalDateTime
        assertEquals(3, results.size());
        // Todo: check that the 3 elements in the array match the appointments we created
    }

    public void testBookAppointmentSuccess() {
        // Given
        // create an employee
        // create 1 appointment

        LocalDateTime timeslot = LocalDateTime.of(2019, Month.AUGUST, 31, 10, 00, 00);
        
        Employee employee = seedEmployee();

        // When
        // run checkAppointment
        Appointment results = service.bookAppointment(timeslot, employee);

        // Then: how do we know it is successful?
        assertNotEquals(null, results);
        assertNotEquals(null, results.getId());
        assertEquals(employee.getId(), results.getEmployee().getId());
        // count how many appointments are there pre insertion vs post insertion
    }

    @Test
    public void testBookAppointmentFail() {
        Employee employee = seedEmployee();
        LocalDateTime timeslot = LocalDateTime.now().plusHours(1);
        // there must an existing timeslot that clashes
        LocalDateTime clashingTimeslot = LocalDateTime.now();
        seedAppointment(clashingTimeslot, employee);

        Appointment results = service.bookAppointment(timeslot, employee);

        // Then: how do we know it is successful?
        assertEquals(null, results);
    }

    Employee seedEmployee() {
        Employee employee = new Employee();
        employee.setName("Spy Fox");
        employee.setEmail("spy.fox@gmail.com");
        employeeRepo.save(employee);
        return employee;
    }

    Appointment seedAppointment(LocalDateTime timeslot, Employee employee) {
        Appointment appointment = new Appointment();
        appointment.setTimeslot(timeslot);
        appointment.setEmployee(employee);
        appointmentRepo.save(appointment);
        return appointment;
    }

}