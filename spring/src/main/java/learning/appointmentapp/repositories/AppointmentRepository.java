package learning.appointmentapp.repositories;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import learning.appointmentapp.entities.Appointment;
import learning.appointmentapp.entities.Employee;;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByTimeslotBetween(LocalDateTime startEndTime, LocalDateTime endDateTime);

    // @Query("SELECT a FROM Appointment a JOIN a.employee e WHERE e.email = ?1")
    // List<Appointment> findByEmployeeEmailContains (String email);

    List<Appointment> findByEmployeeEmailContains (String email);

    List<Appointment> findByEmployee (Employee employee);
}