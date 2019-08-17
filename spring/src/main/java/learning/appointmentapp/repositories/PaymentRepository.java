package learning.appointmentapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import learning.appointmentapp.entities.Payment;

/**
 * PaymentRepository
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    
}