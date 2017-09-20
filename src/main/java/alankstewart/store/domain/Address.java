package alankstewart.store.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "seq", sequenceName = "address_seq")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Version
    private Integer version;

    public enum State { ACT, NSW, NT, QLD, TAS, VIC, WA}

    private String street;
    private String suburb;

    @Enumerated(EnumType.STRING)
    private State state;

    private String postcode;

    public Address(String street, String suburb, State state, String postcode) {
        Objects.requireNonNull(street, "Street must not be null");
        Objects.requireNonNull(suburb, "Suburb must not be null");
        Objects.requireNonNull(state, "State must not be null");
        Objects.requireNonNull(postcode, "Postcode must not be null");
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
    }

    protected Address() {
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public State getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (suburb != null ? !suburb.equals(address.suburb) : address.suburb != null) return false;
        if (state != address.state) return false;
        return postcode != null ? postcode.equals(address.postcode) : address.postcode == null;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (suburb != null ? suburb.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (postcode != null ? postcode.hashCode() : 0);
        return result;
    }
}
