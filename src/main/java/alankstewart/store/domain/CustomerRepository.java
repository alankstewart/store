package alankstewart.store.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByFirstNameAndLastName(String firstName, String lastName);
}
