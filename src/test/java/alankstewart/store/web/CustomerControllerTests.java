package alankstewart.store.web;

import alankstewart.store.StoreApplication;
import alankstewart.store.domain.Address;
import alankstewart.store.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static alankstewart.store.domain.Address.State.NSW;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = RANDOM_PORT)
public class CustomerControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCreateCustomer() throws Exception {
        Customer customer = new Customer("Alan", "Stewart");
        customer.addAddress(new Address("Wahroonga Park", "Wahroonga", NSW, "2076"));
        ResponseEntity<Customer> response = restTemplate.postForEntity("/customer", customer, Customer.class);
        assertThat(response.getStatusCode(), equalTo(CREATED));
        assertThat(response.hasBody(), is(true));
        Customer created = response.getBody();
        assertThat(created.getId(), is(notNullValue()));
    }

    @Test
    public void shouldUpdateCustomer() throws Exception {
        Customer customer = new Customer("Alan", "Stewart");
        Address address = new Address("Wahroonga Park", "Wahroonga", NSW, "2076");
        customer.addAddress(address);
        ResponseEntity<Customer> response = restTemplate.postForEntity("/customer", customer, Customer.class);
        assertThat(response.getStatusCode(), equalTo(CREATED));
        assertThat(response.hasBody(), is(true));

        Customer created = response.getBody();
        assertThat(created.getId(), is(notNullValue()));
        created.setFirstName("Foo");
        created.setLastName("Bar");
        assertTrue(created.removeAddress(address));

        restTemplate.put("/customer", created, Customer.class);

        ResponseEntity<Customer> response2 = restTemplate.getForEntity("/customer/" + created.getId(), Customer.class);
        assertThat(response2.getStatusCode(), equalTo(OK));
        assertThat(response2.hasBody(), is(true));
        Customer updated = response2.getBody();
        assertThat(updated.getFirstName(), is("Foo"));
        assertThat(updated.getLastName(), is("Bar"));
        assertThat(updated.getAddresses().size(), is(0));
    }

    @Test
    public void shouldDeleteCustomer() throws Exception {
        Customer customer = new Customer("Alan", "Stewart");
        Address address = new Address("Wahroonga Park", "Wahroonga", NSW, "2076");
        customer.addAddress(address);
        ResponseEntity<Customer> response = restTemplate.postForEntity("/customer", customer, Customer.class);
        assertThat(response.getStatusCode(), equalTo(CREATED));
        assertThat(response.hasBody(), is(true));
        Customer created = response.getBody();
        assertThat(created.getId(), is(notNullValue()));

        restTemplate.delete("/customer/" + created.getId());
        ResponseEntity<Customer> response2 = restTemplate.getForEntity("/customer/" + created.getId(), Customer.class);
        assertThat(response2.hasBody(), is(false));
    }

    @Test
    public void shouldListAllCustomers() throws Exception {
        Customer customer1 = new Customer("Alan", "Stewart");
        customer1.addAddress(new Address("Wahroonga Park", "Wahroonga", NSW, "2076"));
        ResponseEntity<Customer> response = restTemplate.postForEntity("/customer", customer1, Customer.class);
        assertThat(response.getStatusCode(), equalTo(CREATED));
        assertThat(response.hasBody(), is(true));

        ResponseEntity<List<Customer>> response2 = restTemplate.exchange("/customer",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {
                });
        assertThat(response2.getStatusCode(), equalTo(OK));
        assertThat(response2.hasBody(), is(true));
        List<Customer> customers = response2.getBody();
        assertFalse(customers.isEmpty());
    }

    @Test
    public void shouldFindCustomerByFirstNameAndLastName() throws Exception {
        Customer customer1 = new Customer("Alan", "Stewart");
        customer1.addAddress(new Address("Wahroonga Park", "Wahroonga", NSW, "2076"));
        ResponseEntity<Customer> response = restTemplate.postForEntity("/customer", customer1, Customer.class);
        assertThat(response.getStatusCode(), equalTo(CREATED));
        assertThat(response.hasBody(), is(true));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/customer/find")
                .queryParam("firstName", customer1.getFirstName())
                .queryParam("lastName", customer1.getLastName());

        ResponseEntity<Customer> response2 = restTemplate.getForEntity(builder.buildAndExpand().toUri(), Customer.class);
        assertThat(response2.getStatusCode(), equalTo(OK));
        assertThat(response2.hasBody(), is(true));
    }
}
