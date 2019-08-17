package learning.appointmentapp.repositories;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import learning.appointmentapp.entities.Employee;

/**
* EmployeeRepositoryTest
*/
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository repo;

    public Employee seedEmployee() {
        Employee seeded = new Employee();
        seeded.setName("Spy Fox");
        seeded.setEmail("spy.fox@gmail.com");
        repo.save(seeded);
        return seeded;
    }

    @Test
    public void testFindAllEmpty() {
        // Given: there are no employees in the DB
        repo.deleteAll();

        // When: employeeRepository.findAll()
        List<Employee> result = repo.findAll();

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAllNotEmpty() {
        // Given: there are employees in the DB
        seedEmployee();

        // When: employeeRepository.findAll()
        List<Employee> result = repo.findAll();

        // Then: get an array of the employees
        assertEquals(1, result.size());
        Employee retrievedEmployee = result.get(0);
        assertEquals("Spy Fox", retrievedEmployee.getName());
        assertEquals("spy.fox@gmail.com", retrievedEmployee.getEmail());
    }

    @Test
    public void testFindByIdEmpty() {
        // Given: there are no employees in the DB
        repo.deleteAll();

        Long id = 1L;

        // When: employeeRepository.findAById(Long id)
        Employee result = repo.findById(id).orElse(null);

        // Then: get an empty array
        assertEquals(null, result);
    }

    @Test
    public void testFindByIdNotEmpty() {
        // Given: there are employees in the DB
        Employee employee = seedEmployee();


        // When: employeeRepository.findById(Long id)
        Employee result = repo.findById(employee.getId()).orElse(null);

        // Then: get an array of the employees
        assertEquals(employee.getId(), result.getId());
    }

    @Test
    public void testFindByEmailEmpty() {
        // Given: there are no employees in the DB
        repo.deleteAll();

        String email = "spy.fox@gmail.com";

        // When: employeeRepository.findAByEmail(String email)
        List<Employee> result = repo.findByEmail(email);

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByEmailNotEmpty() {
        // Given: there are employees in the DB
        seedEmployee();

        String email = "spy.fox@gmail.com";

        // When: employeeRepository.findAByEmail(String email)
        List<Employee> result = repo.findByEmail(email);

        // Then: get an array of the employees
        assertEquals(1, result.size());
        Employee retrievedEmployee = result.get(0);
        assertEquals("Spy Fox", retrievedEmployee.getName());
        assertEquals("spy.fox@gmail.com", retrievedEmployee.getEmail());
    }

    @Test
    public void testFindByNameEmpty() {
        // Given: there are no employees in the DB
        repo.deleteAll();

        String name = "Spy Fox";

        // When: employeeRepository.findAByName(String name)
        List<Employee> result = repo.findByName(name);

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByNameNotEmpty() {
        // Given: there are employees in the DB
        seedEmployee();

        String name = "Spy Fox";

        // When: employeeRepository.findByName(String name)
        List<Employee> result = repo.findByName(name);

        // Then: get an array of the employees
        assertEquals(1, result.size());
        Employee retrievedEmployee = result.get(0);
        assertEquals("Spy Fox", retrievedEmployee.getName());
        assertEquals("spy.fox@gmail.com", retrievedEmployee.getEmail());
    }

    @Test
    public void testFindAllSortingEmpty() {
        // Given: there are no employees in the DB
        repo.deleteAll();

        // When: employeeRepository.findAllByNameAsc()
        List<Employee> result = repo.findByOrderByNameAsc();

        // Then: get an empty array
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAllSortingNotEmpty() {
        // Given: there are employees in the DB
        Employee seeded1 = new Employee();
        seeded1.setName("Spy Fox");
        seeded1.setEmail("spy.fox@gmail.com");
        repo.save(seeded1);

        Employee seeded2 = new Employee();
        seeded2.setName("Pyjama Sam");
        seeded2.setEmail("pyjama.sam@gmail.com");
        repo.save(seeded2);

        Employee seeded3 = new Employee();
        seeded3.setName("Freddi Fish");
        seeded3.setEmail("freddi.fish@gmail.com");
        repo.save(seeded3);

        Employee seeded4 = new Employee();
        seeded4.setName("Putt Putt");
        seeded4.setEmail("putt.putt@gmail.com");
        repo.save(seeded4);

        // When: employeeRepository.findAllByNameAsc()
        List<Employee> result = repo.findByOrderByNameAsc();

        // Then: get an array of the employees
        assertEquals(4, result.size());

        assertEquals("Freddi Fish", result.get(0).getName());
        assertEquals("Putt Putt", result.get(1).getName());
        assertEquals("Pyjama Sam", result.get(2).getName());
        assertEquals("Spy Fox", result.get(3).getName());
    }
}