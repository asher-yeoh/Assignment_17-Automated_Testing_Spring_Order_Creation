package learning.appointmentapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import learning.appointmentapp.entities.LineItem;

/**
 * LineItemRepository
 */
public interface LineItemRepository extends JpaRepository<LineItem, Long> {

    
}