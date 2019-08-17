package learning.appointmentapp.repositories;

import static org.junit.Assert.assertEquals;

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
import net.bytebuddy.asm.Advice.Local;

/**
* AppointmentRepositoryTest
*/
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AppointmentRepositoryTest {
 
    @Autowired
    AppointmentRepository repo;

    @Autowired
    EmployeeRepository employeeRepo;

    public Employee seedEmployee() {
        Employee seededEmployee = new Employee();
        seededEmployee.setName("Spy Fox");
        seededEmployee.setEmail("spy.fox@gmail.com");
        employeeRepo.save(seededEmployee);
        return seededEmployee;
    }

    public Appointment seedAppointment(LocalDateTime timeslot) {
        seedEmployee();
        Appointment seeded = new Appointment();

        seeded.setTimeslot(timeslot);
        // Long employeeId = seeded.getEmployee().getId();
        seeded.setEmployee(seedEmployee());
        repo.save(seeded);
        return seeded;
    }

    @Test
    public void testFindAllEmpty() {
        // Given: there are no appointments in the DB
        repo.deleteAll();

        // When: appointmentRepository.findAll()
        List<Appointment> result = repo.findAll();

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAllNotEmpty() {
        // Given: there are appointments in the DB
        // seedEmployee();
        LocalDateTime timeslot = LocalDateTime.of(2019, Month.AUGUST, 31, 10, 00, 00);
        seedAppointment(timeslot);

        // When: appointmentRepository.findAll()
        List<Appointment> result = repo.findAll();

        // Then: get an array of the appointments
        assertEquals(1, result.size());
        Appointment retrievedAppointment = result.get(0);
        assertEquals(timeslot, retrievedAppointment.getTimeslot());
        assertEquals(retrievedAppointment.getEmployee().getId(), retrievedAppointment.getEmployee().getId());
    }

    @Test
    public void testfindByTimeslotBetweenEmpty() {
        // Given: there are no appointments in the DB
        repo.deleteAll();

        // Given a start time and a end time
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(3);

        // Find all appointments within that time range
        List<Appointment> result = repo.findByTimeslotBetween(startDateTime, endDateTime);

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testfindByTimeslotBetweenPresentNotEmpty() {
        // Given a start time and a end time
        seedAppointment(LocalDateTime.now().plusMinutes(30));
        seedAppointment(LocalDateTime.now().plusMinutes(45));
        seedAppointment(LocalDateTime.now().plusHours(5));

        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(3);

        // When: appointmentRepository.findByTimeslotBetween(LocalDateTime startEndTime, LocalDateTime endDateTime)
        List<Appointment> result = repo.findByTimeslotBetween(startDateTime, endDateTime);

        // Then: get an array of the appointments
        assertEquals(2, result.size());
    }

    @Test
    public void testfindByEmployeeEmailContainsEmpty() {
        // Given: there are no appointments in the DB
        repo.deleteAll();

        String email = "spy.fox@gmail.com";

        // When: appointmentRepository.findByEmployeeEmailContains(email)
        List<Appointment> result = repo.findByEmployeeEmailContains(email);

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testfindByEmployeeEmailContainsnNotEmpty() {
        // Given: there are appointments in the DB
        LocalDateTime timeslot = LocalDateTime.of(2019, Month.AUGUST, 31, 10, 00, 00);
        seedAppointment(timeslot);

        String email = seedEmployee().getEmail();

        // When: appointmentRepository.findByEmployeeEmailContains(email)
        List<Appointment> result = repo.findByEmployeeEmailContains(email);

        // Then: get an array of the appointments
        assertEquals(1, result.size());
    }
}