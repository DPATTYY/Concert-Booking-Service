package proj.concert.service.domain;

import java.util.Set;
import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.types.Genre;


import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Domain class represents the performers.
 */
@Entity
@Table(name="PERFORMERS")
public class Performer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    @JsonProperty("id")
    private Long id;
    @Column(name = "NAME")
    @JsonProperty("name")
    private String name;
    @Column(name = "IMAGE_NAME")
    @JsonProperty("imageName")
    private String imageName;
    @Enumerated(EnumType.STRING)
    @Column(name = "GENRE")
    @JsonProperty("genre")
    private Genre genre;
    @Column(name = "BLURB", columnDefinition = "TEXT")
    @JsonProperty("blurb")
    private String blurb;

    public Performer() {
    }

    public Performer(Long id, String name, String imageName, Genre genre, String blurb) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.genre = genre;
        this.blurb = blurb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Performer))
            return false;
        if (obj == this)
            return true;

        Performer rhs = (Performer) obj;
        return new EqualsBuilder().
                append(id, rhs.id).
                append(name, rhs.name).
                append(imageName, rhs.imageName).
                append(genre, rhs.genre).
                append(blurb, rhs.blurb).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(imageName)
                .append(genre)
                .append(blurb)
                .toHashCode();
    }
}
