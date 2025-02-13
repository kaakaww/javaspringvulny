package hawk.hotel.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue()
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    @JoinTable(
            name = "building_staff",
            joinColumns = @JoinColumn(name = "building_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<Staff> staff;

    @Column(columnDefinition = "jsonb")
    private String blahs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public void setStaff(List<Staff> staff) {
        this.staff = staff;
    }

    public List<Integer> getBlahs() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.blahs, objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to numbers", e);
        }
    }

    public void setBlahs(List<Integer> blahs) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.blahs = objectMapper.writeValueAsString(blahs);
        } catch (Exception e) {
            throw new RuntimeException("Error converting numbers to JSON", e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
