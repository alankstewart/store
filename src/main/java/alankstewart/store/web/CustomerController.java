package alankstewart.store.web;

import alankstewart.store.domain.Customer;
import alankstewart.store.domain.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Customer getCustomer(@PathVariable("id") Long id) {
        return customerRepository.findOne(id);
    }

    @GetMapping
    @ResponseBody
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/find")
    @ResponseBody
    public Customer findCustomer(@RequestParam String firstName, @RequestParam String lastName) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping
    @ResponseBody
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerRepository.delete(id);
    }

}
