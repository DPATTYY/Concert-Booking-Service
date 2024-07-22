package proj.concert.service.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Domain class represent the seat in the concert.
 */
@Entity
public class Seat {
	@Id
	@GeneratedValue
	private long id;
	@JsonProperty("label")
	private String label;
	private boolean isBooked;
	private LocalDateTime date;
	private BigDecimal cost;

	public Seat() {
	}

	public Seat(String label, BigDecimal cost) {
		this.label = label;
		this.cost = cost;
	}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal cost) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.cost = cost;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean seatIsBooked() {
		return isBooked;
	}

	public void setIsBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Seat))
			return false;
		if (obj == this)
			return true;

		Seat rhs = (Seat) obj;
		return new EqualsBuilder().
				append(id, rhs.id).
				append(label, rhs.label).
				append(isBooked, rhs.isBooked).
				append(date, rhs.date).
				append(cost, rhs.cost).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
				append(id).
				append(label).
				append(isBooked).
				append(date).
				append(cost).
				hashCode();
	}
}
