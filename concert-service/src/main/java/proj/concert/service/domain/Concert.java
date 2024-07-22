package proj.concert.service.domain;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

/**
 * Domain class represents the concert.
 */
@Entity
@Table(name = "CONCERTS")
public class Concert{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    @JsonProperty("id")
    private Long id;
    @Column(name = "TITLE")
    @JsonProperty("title")
    private String title;
    @Column(name = "IMAGE_NAME")
    @JsonProperty("imageName")
    private String imageName;
    @Column(name = "BLURB", columnDefinition = "TEXT")
    @JsonProperty("blurb")
    private String blrb;
    //use elementcollection annotation as the table for concertdates in sql,use fetchtype lazy to save loading time
    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="CONCERT_DATES", joinColumns = @JoinColumn(name="CONCERT_ID"))
    @Column(name="DATE")
    private Set<LocalDateTime> dates = new HashSet<>();
    /*etablish many-to-many relationship between concert and performers as a join table,
    also had lazy fetchtype to save loading and persist so can get the data for both entities.*/
    @ManyToMany(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name="CONCERT_PERFORMER", joinColumns = @JoinColumn(name="CONCERT_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name="PERFORMER_ID", nullable = false))
    @Fetch(FetchMode.SUBSELECT)
    private List<Performer> performers = new ArrayList<>();

    public Concert() {
    }

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blrb = blurb;
    }

    public Concert(String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return blrb;
    }

    public void setBlurb(String blrb) {
        this.blrb = blrb;
    }

    public Set<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Concert))
            return false;
        if (obj == this)
            return true;

        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
                append(id, rhs.id).
                append(title, rhs.title).
                append(imageName, rhs.imageName).
                append(blrb, rhs.blrb).
                append(dates, rhs.dates).
                append(performers, rhs.performers).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(imageName)
                .append(blrb)
                .append(dates)
                .append(performers)
                .toHashCode();
    }
}
