package hawk.hotel.rest;

import java.util.List;
import java.util.Map;

import hawk.hotel.domain.Continent;

import hawk.hotel.domain.Hotel;
import hawk.hotel.exception.DataFormatException;
import hawk.hotel.service.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/hotels")
public class HotelController extends AbstractRestHandler {

    @Autowired
    private HotelService hotelService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createHotel(@RequestBody Hotel hotel,
                                 HttpServletRequest request, HttpServletResponse response) {
        Hotel createdHotel = this.hotelService.createHotel(hotel);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdHotel.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<Hotel> getAllHotel(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.hotelService.getAllHotels(page, size);
    }

    @RequestMapping(value = "/upper-bounded-list",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<? extends Hotel> upperBoundedCollectionOfHotels(
                            @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                            @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                            HttpServletRequest request, HttpServletResponse response) {
        return this.hotelService.getAllHotels(page, size);
    }

    @RequestMapping(value = "/lower-bounded-list",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<? super Hotel> lowerBoundedCollectionOfHotels(
                                                         @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,

                                                         @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                                         HttpServletRequest request, HttpServletResponse response) {
        return this.hotelService.getAllHotels(page, size);
    }

    @RequestMapping(value = "/unbounded-list",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<?> unboundedCollectionOfHotels(
                                                       @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,

                                                       @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                                       HttpServletRequest request, HttpServletResponse response) {
        return this.hotelService.getAllHotels(page, size);
    }

    @RequestMapping(value = "/random",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Hotel> getRandomHotel(
            @RequestParam(required = false, defaultValue = "null") Continent continent,
            HttpServletRequest request, HttpServletResponse response
    ) {
        System.out.println("Received a continent to filter by... " + continent.name() + " ...ignoring...");
        return ResponseEntity.ok(this.hotelService.randomHotel());
    }

    @RequestMapping(value = "/locations",
    method = RequestMethod.GET,
    produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map<Continent, List<Hotel>> getHotelsByLocation(HttpServletRequest request, HttpServletResponse response) {
        return this.hotelService.hotelsByLocation();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Hotel getHotel(
        @PathVariable("id") Long id,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        Hotel hotel = this.hotelService.getHotel(id);
        checkResourceFound(hotel);
        //todo: http://goo.gl/6iNAkz
        return hotel;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateHotel(
                                 @PathVariable("id") Long id, @RequestBody Hotel hotel,
                                 HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.hotelService.getHotel(id));
        if (id != hotel.getId()) throw new DataFormatException("ID doesn't match!");
        this.hotelService.updateHotel(hotel);
    }

    //todo: @ApiImplicitParams, @ApiResponses
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(
            @PathVariable("id") Long id, HttpServletRequest request,
            HttpServletResponse response
    ) {
        checkResourceFound(this.hotelService.getHotel(id));
        this.hotelService.deleteHotel(id);
    }
}
