package hawk.hotel.domain;

import hawk.entity.TenantSupport;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;

/*
 * a simple domain entity doubling as a DTO
 */
@Entity
@Table(name = "hotel")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Hotel implements TenantSupport, Comparable<Hotel> {

    @Column()
    String city;
    @Id
    @GeneratedValue()
    private long id;
    @Column(nullable = false)
    private String name;
    @Column()
    private String description;
    @Column()
    private int rating;

    @Column()
    @Enumerated(EnumType.STRING)
    private Continent continent;
    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(name = "tenant_id")
    private String tenantId;

    public Hotel() {
    }

    public Hotel(String name, String description, int rating) {
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    public Hotel(String name, String description, int rating, String city, Continent continent, String tenantId) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.city = city;
        this.continent = continent;
        this.tenantId = tenantId;
    }

    public long getId() {
        return this.id;
    }

    // for tests ONLY
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Hotel {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public int compareTo(Hotel other) {
        Comparator<Hotel> comparator = Comparator.comparing(Hotel::getName)
                .thenComparing(Hotel::getContinent)
                .thenComparing(Hotel::getCity)
                .thenComparing(Hotel::getRating);
        return comparator.compare(this, other);
    }
}
