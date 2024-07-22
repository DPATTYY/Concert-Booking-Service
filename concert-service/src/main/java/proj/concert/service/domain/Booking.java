package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Domain class for representing a concert booking.
 * It will store the concert id and the seats are booked and the user who book this seat.
 */
@Entity
@Table(name="BOOKINGS")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long concertId;
    private LocalDateTime date;
    /*added annotation to create relation between booking and seat.
     */
    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private List<Seat> seats = new ArrayList<>();
    //added annotation to create relaiton between booking and user
    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

    public Booking() {
    }

    public Booking(User user, long concertId, LocalDateTime date) {
        this.user = user;
        this.concertId = concertId;
        this.date = date;
    }

    public Booking(long concertId, LocalDateTime date, List<Seat> seats) {
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Booking))
            return false;
        if (obj == this)
            return true;

        Booking rhs = (Booking) obj;
        return new EqualsBuilder().
                append(id, rhs.id).
                append(concertId, rhs.concertId).
                append(date, rhs.date).
                append(seats, rhs.seats).
                append(user, rhs.user).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(id).
                append(concertId).
                append(date).
                append(seats).
                append(user).
                hashCode();
    }

}
