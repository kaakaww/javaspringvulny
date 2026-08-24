package hawk.hotel.service;

import hawk.hotel.dao.HotelRepository;
import hawk.hotel.domain.Continent;
import hawk.hotel.domain.Hotel;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/*
 * Sample service to demonstrate what the API would use to get things done
 */
@Service
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepository hotelRepository;

    @PersistenceContext
    public EntityManager entityManager;

    public HotelService() {
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel getHotel(long id) {
        // Use query instead of findById to ensure tenant filter is applied
        List<Hotel> results = entityManager.createQuery("SELECT h FROM Hotel h WHERE h.id = :id", Hotel.class)
                .setParameter("id", id)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        // First check if the hotel exists and belongs to current tenant
        Hotel hotel = getHotel(id);
        if (hotel != null) {
            hotelRepository.deleteById(id);
        }
    }

    public Page<Hotel> getAllHotels(Integer page, Integer size) {
        return hotelRepository.findAll(PageRequest.of(page, size));
    }

    public Hotel randomHotel() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        List<Hotel> hotels = new ArrayList<>(hotelRepository.findAll(pageable).getContent());
        Collections.shuffle(hotels);
        return hotels.isEmpty() ? null : hotels.get(0);
    }

    public Map<Continent, List<Hotel>> hotelsByLocation() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
        List<Hotel> hotels = new ArrayList<>(hotelRepository.findAll(pageable).getContent());
        Collections.shuffle(hotels);

        Map<Continent, List<Hotel>> hotelsByLocation = new HashMap<>();

        for (Hotel hotel : hotels) {
            Continent[] continents = Continent.values();
            String rnd = hotel.getName() + hotel.getCity() + hotel.getDescription() + hotel.getId();
            int i = rnd.length() % continents.length;
            Continent continent = continents[i];

            hotelsByLocation.computeIfAbsent(continent, k -> new ArrayList<>());
            hotelsByLocation.get(continent).add(hotel);
        }
        return hotelsByLocation;
    }

    /**
     * VULNERABLE: This method bypasses tenant filtering and returns ALL hotels across ALL tenants
     * This is a Broken Object Level Authorization (BOLA) vulnerability
     * Users should only see hotels from their own tenant
     */
    public List<Hotel> getAllHotelsUnfiltered() {
        log.warn("VULNERABILITY: Fetching all hotels without tenant filtering!");
        // Deliberately NOT enabling the tenant filter here
        // This allows any authenticated user to see hotels from ALL tenants
        return entityManager.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();
    }

    /**
     * VULNERABLE: This method bypasses tenant filtering and returns ANY hotel by ID regardless of tenant
     * This is a Broken Object Level Authorization (BOLA) vulnerability
     * Users should only be able to access hotels from their own tenant
     */
    public Hotel getHotelByIdUnfiltered(Long id) {
        log.warn("VULNERABILITY: Fetching hotel ID {} without tenant filtering!", id);
        // Deliberately NOT enabling the tenant filter here
        // This allows any authenticated user to access hotels from ANY tenant by guessing/enumerating IDs
        return entityManager.createQuery("SELECT h FROM Hotel h WHERE h.id = :id", Hotel.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
