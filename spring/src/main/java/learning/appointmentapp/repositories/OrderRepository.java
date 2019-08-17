package learning.appointmentapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import learning.appointmentapp.entities.Order;

/**
 * OrderRepository
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    
}