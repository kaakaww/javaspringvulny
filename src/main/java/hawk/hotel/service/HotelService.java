package hawk.hotel.service;

import hawk.hotel.domain.Continent;
import hawk.hotel.domain.Hotel;
import hawk.hotel.dao.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Sample service to demonstrate what the API would use to get things done
 */
@Service
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepository hotelRepository;

    public HotelService() {
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel getHotel(long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
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
}
