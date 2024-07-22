
package proj.concert.service.domain;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.ws.rs.core.Cookie;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Domain class represent the user of concert
 */
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name = "USERNAME")
    @JsonProperty("username")
    private String username;
    @Column(name = "PASSWORD")
    @JsonProperty("password")
    private String password;
    @Column(name = "VERSION")
    @JsonProperty("version")
    private Long version;
    //stored the cookie value which is an uuid type

    @Column(name = "TOKEN")
    private String userToken;

    private UUID uuid;
    /*established a one to many relationship using the annotation, use to persist cascade type to get all the related
    attributes.
     */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
    private List<Booking> bookings = new ArrayList<>();



    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password, Long version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addUserBooking(Booking booking){
        this.bookings.add(booking);
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void setToken(Cookie token){ userToken = token.getValue(); }

    public boolean hasBooking(Booking booking) { return bookings.contains(booking); }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User rhs = (User) obj;
        return new EqualsBuilder().
                append(id, rhs.id).
                append(username, rhs.username).
                append(password, rhs.password).
                append(version, rhs.version).
                append(uuid, rhs.uuid).
                append(bookings, rhs.bookings).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .append(password)
                .append(version)
                .append(uuid)
                .append(bookings)
                .toHashCode();
    }
}