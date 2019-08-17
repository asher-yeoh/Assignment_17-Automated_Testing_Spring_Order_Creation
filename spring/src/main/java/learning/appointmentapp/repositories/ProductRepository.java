package learning.appointmentapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import learning.appointmentapp.entities.Product;

/**
 * ProductRepository
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    
}