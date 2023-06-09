package com.skilldistillery.jpaeventlight.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.skilldistillery.jpaeventlight.data.UserDAO;
import com.skilldistillery.jpaeventlight.entities.Address;
import com.skilldistillery.jpaeventlight.entities.Artist;
import com.skilldistillery.jpaeventlight.entities.Band;
import com.skilldistillery.jpaeventlight.entities.Event;
import com.skilldistillery.jpaeventlight.entities.User;
import com.skilldistillery.jpaeventlight.entities.Venue;

@Controller
public class UserController {

	@Autowired
	private UserDAO userDao;
	
	
	@RequestMapping(path = {"/", "index.do"})
	public String index(Model model, HttpSession session) {
		List<Event> events = userDao.findAllEvents();
		model.addAttribute("eventList", events);
		
		User user = (User) session.getAttribute("user");
		
		if (user != null) {
			if (user.getRole().equals("att")) {
				return "User-att-home";
			} else if (user.getRole().equals("vo")) {
				return "User-vo-home";
			}
		}
			return "index";
	}
	
	 @GetMapping(path = "mainIndex.do")
	    public String mainIndex(Model model, HttpSession session) {
		 	List<Event> events = userDao.findAllEvents();
			model.addAttribute("eventList", events);
	        return "index";
	    }

	@GetMapping(path = "home.do")
	private String home(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<Event> events = userDao.findAllEvents();
		model.addAttribute("eventList", events);
		
		if (user != null) {
			if (user.getRole().equals("att")) {
				return "User-att-home";
			} else if (user.getRole().equals("vo")) {
				return "User-vo-home";
			}
		}
			return "index";
	}


	@GetMapping(path = "listAll.do")
	public String listAllEvents(Model model) {
		List<Event> events = userDao.findAllEvents();
		model.addAttribute("eventList", events);
		return "listAllEvents";
	}

	@RequestMapping(path ="login.do", method = RequestMethod.POST)
	public String userLogin(Model model, HttpSession session, @RequestParam("username") String userName,
			@RequestParam("password") String password) {
		User user = userDao.findByUsernameAndPassword(userName, password);
		session.setAttribute("user", user);

		if (user != null) {
			if (user.getRole().equals("att")) {
				return "User-att-home";
			} else if (user.getRole().equals("vo")) {
				return "User-vo-home";
			}
		}
		return "SignInFailiure";

	}

	@RequestMapping(path = "loginpage.do")
	public String signInPage() {
		return "SignIn";
	}
	
	@RequestMapping(path = "aboutUs.do")
	public String aboutUs() {
		return "aboutUs";
	}
	
	@RequestMapping(path = "contactUs.do")
	public String contactUs(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");		
		return "contactUs";
	}
	


	@RequestMapping(path = "updateVenuePage.do")
	public String updateVenuePage(HttpSession session) {
		User user = (User) session.getAttribute("user");
		Venue venue = user.getVenue();
		session.setAttribute("venue", venue);
		return "UpdateVenue";
	}

	@RequestMapping(path = "goHome.do")
	public String goToHome(HttpSession session) {

		User user = (User) session.getAttribute("user");
		if (user != null) {
			user = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
			session.setAttribute("user", user);
			if (user.getRole().equals("att")) {
				return "User-att-home";
			} else if (user.getRole().equals("vo")) {
				return "User-vo-home";
			}
		}
		return "home";
	}

	 @GetMapping(path = "logout.do")
	    public RedirectView userLogOut(HttpSession session) {
	        session.removeAttribute("user");
	        return new RedirectView("index.do");
	    }

	@RequestMapping(path = "SignUpPage.do")
	public String signUpPage() {
		return "SignUp";
	}

	@RequestMapping(path = "signUp.do")
	public String signUp(HttpSession session, Model model, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("role") String role,
			@RequestParam("firstName") String fName, @RequestParam("lastName") String lName,
			@RequestParam("profilePicture") String profilePicture, @RequestParam("aboutMe") String aboutMe,
			@RequestParam("street") String street, @RequestParam("city") String city,
			@RequestParam("state") String state, @RequestParam("zip") String zip) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setRole(role);
		user.setFirstName(fName);
		user.setLastName(lName);
		user.setProfilePicture(profilePicture);
		user.setAboutMe(aboutMe);
		user.setCreatedAt(LocalDateTime.now());
		user.setEnabled(true);

		Address address = new Address();
		address.setCity(city);
		address.setState(state);
		address.setStreet(street);
		address.setZip(zip);

		user.setAddress(address);

		user = userDao.signUp(user, address);


		return "SignIn";
	}

	@PostMapping(path = "createVenue.do")
	public String createVenue(HttpSession session, @RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("picture") String picture, @RequestParam("logo") String logo,
			@RequestParam("street") String street, @RequestParam("city") String city,
			@RequestParam("state") String state, @RequestParam("zip") String zip) {

		Venue venue = new Venue();
		venue.setName(name);
		venue.setDescription(description);
		venue.setPhoneNumber(phoneNumber);
		venue.setPicture(picture);
		venue.setPictureLogo(logo);

		Address address = new Address();
		address.setStreet(street);
		address.setCity(city);
		address.setState(state);
		address.setZip(zip);

		venue.setAddress(address);

		User user = (User) session.getAttribute("user");
		venue.setUser(user);

		venue = userDao.createVenue(venue, address);
		
		user.setVenue(venue);

		session.setAttribute("venue", venue);
		return "User-vo-home";
	}

	@PostMapping(path = "updateVenue.do")
	public String updateVenue(HttpSession session, @RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("picture") String picture, @RequestParam("logo") String logo,
			@RequestParam("street") String street, @RequestParam("city") String city,
			@RequestParam("state") String state, @RequestParam("zip") String zip) {
		Venue existingVenue = (Venue) session.getAttribute("venue");
		existingVenue.setName(name);
		existingVenue.setDescription(description);
		existingVenue.setPhoneNumber(phoneNumber);
		existingVenue.setPicture(picture);
		existingVenue.setPictureLogo(logo);

		Address existingAddress = existingVenue.getAddress();
		existingAddress.setStreet(street);
		existingAddress.setCity(city);
		existingAddress.setState(state);
		existingAddress.setZip(zip);

		userDao.updateAddress(existingAddress);
		userDao.updateVenue(existingVenue);
		return "User-vo-home";
	}

	@GetMapping(path = "SeeMyEvents.do")
	public String seeEventsByVenue(HttpSession session) {
		User user = (User) session.getAttribute("user");
		Venue venue = user.getVenue();
		int id = venue.getId();
		List<Event> eventsById = userDao.findEventsByVenueId(id);
		session.setAttribute("eventsById", eventsById);
		return "listbyvenueid";
	}


	@GetMapping(path = "favoriteEvents.do")
	public String addEventFavorites(HttpSession session, Model model,
			@RequestParam(name="eventId") int eventId) {
		User user = (User) session.getAttribute("user");
		
		 if (user == null) {
		        return "SignIn"; 
		    }
		
		user = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
		session.setAttribute("user", user);
		
		if (user != null) {
			List<Event> favEvents = userDao.addToFavoriteEvents(eventId, user.getId());
			favEvents.size();
			model.addAttribute("events", favEvents);
		return "User-att-home";
		} else {
			return "SignIn";
		}
	}
	
	
	@GetMapping(path = "removeFavorite.do")
	public String removeEventFavorite(HttpSession session, Model model,
	@RequestParam(name="eventId") int eventId){
		User user = (User) session.getAttribute("user");
		user = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
		session.setAttribute("user", user);
		List<Event> favEvents = userDao.removeFavoriteEvent(eventId, user.getId());
		favEvents.size();
		model.addAttribute("events", favEvents);
		return "User-att-home";
		
		
	}
	
	
	@GetMapping(path = "createArtistPage.do")
	public String createArtist() {
		return "CreateArtist";
	}

	@PostMapping(path = "createArtist.do")
	public String createArtist(HttpSession session,
			@RequestParam("firstName") String fName,
			@RequestParam("lastName") String lName,
			@RequestParam("stageName") String stageName,
			@RequestParam("instrument") String instrument,
			@RequestParam("image") String image,
			@RequestParam("description") String description) {
		
		Artist artist = new Artist();
		artist.setFirstName(fName);
		artist.setLastName(lName);
		artist.setStageName(stageName);
		artist.setInstrument(instrument);
		artist.setImage(image);
		artist.setDescription(description);
		
		artist = userDao.createArtist(artist);
		
		List<Artist> artists = userDao.findAllArtists();
		List<Artist> artists2 = new ArrayList<>(artists);
		
		for (Artist a : artists) {
			if (a.getBandArtist() != null) {
				artists2.remove(a);
			}
			
			artists = artists2;
		}
		
		session.setAttribute("allArtists", artists);

		return "SelectArtists";
	}
	
	
	
	@GetMapping(path = "createBandPage.do")
	public String createBandPage(HttpSession session) {
		return "CreateBand";
	}

	@PostMapping(path = "createBand.do")
	public String createBand(HttpSession session, Model model,
			@RequestParam("name") String name,
			@RequestParam("genre") String genre,
			@RequestParam("image") String image,
			@RequestParam("description") String description) {
		Band band = new Band();
		band.setName(name);
		band.setGenre(genre);
		band.setImage(image);
		band.setDescription(description);
		band.setArtists(null);
		
		Event event = (Event) session.getAttribute("event");
		band = userDao.createBand(band);
		session.setAttribute("band", band);
		
		List<Artist> artists = new ArrayList<>();
		artists = userDao.findAllArtists();
		List<Artist> artists2 = new ArrayList<>(artists);
		for (Artist a : artists) {
			if (a.getBandArtist() != null) {
				artists2.remove(a);
			}
			artists = artists2;
		}
		session.setAttribute("allArtists", artists);
		model.addAttribute("allArtists", artists);
		

		return "SelectArtists";
	}
	@PostMapping(path="addArtistsToBand.do")
	public String addArtistsToBand(HttpSession session, Model model,
			@RequestParam("artistSelected") String artists) {
		List<String> ids = Arrays.asList(artists.split(","));
		Band band = (Band) session.getAttribute("band");
		List<Artist> artistsToAdd = new ArrayList<>();
		
		for (String id  : ids) {
			int parsedId = Integer.parseInt(id);
			Artist artist = userDao.findArtistById(parsedId);
			artist.setBandArtist(band);
			artistsToAdd.add(artist);
		}
		
		band.setArtists(artistsToAdd);
		session.setAttribute("allBands", userDao.listAllBands());
		
		return "SelectBands";
	}
		
	
	@GetMapping(path = "createEventPage.do")
	public String createEventPage(HttpSession session) {
		return "CreateEvent";
	}
	@GetMapping(path = "createEvent.do")
	public String createEvent(HttpSession session, Model model,
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("date") String date,
			@RequestParam("start") String start,
			@RequestParam("end") String end,
			@RequestParam("image") String image) {
		User user = (User) session.getAttribute("user");
		Venue venue = user.getVenue();
		Event event = new Event();
		event.setTitle(name);
		event.setDescription(description);
		LocalDate parsedDate = LocalDate.parse(date);
		event.setEventDate(parsedDate);
		LocalTime parsedStart = LocalTime.parse(start);
		event.setStartTime(parsedStart);
		LocalTime parsedEnd = LocalTime.parse(end);
		event.setEndTime(parsedEnd);
		event.setImage(image);
		
		event.setVenue(venue);
		event.setEnabled(true);
		
		event = userDao.createEvent(event);
		session.setAttribute("event", event);
		model.addAttribute("event", event);
		List<Band> allBands = userDao.listAllBands();
		model.addAttribute("allBands", allBands);
		return "SelectBands";
	}
	
	@GetMapping(path="selectBandsPage.do")
	public String selectBandsPage(HttpSession session) {
		Event event = (Event) session.getAttribute("event");
		session.setAttribute("event", event);
		List<Band> bands = userDao.listAllBands();
		session.setAttribute("allBands", bands);
		return "SelectBands";
	}
	
	@PostMapping(path="addBandsToEvent.do")
	public String addBandsToEvent(HttpSession session, Model model,
			@RequestParam("bandsSelected") String bandsSelected) {
		List<String> bandList = Arrays.asList(bandsSelected.split(","));
		Event event = (Event) session.getAttribute("event");
		List<Band> bands = new ArrayList<>();
		event.setBands(bands);
		List<Band> allBands = userDao.listAllBands();
		for (Band band : allBands)
		if (band.getEvents().contains(event)) {
			List<Event> events = band.getEvents();
			events.remove(event);
			band.setEvents(events);
		}
		for (String bandName : bandList) {
			Band band = userDao.findBandByName(bandName);
			List<Event> events = new ArrayList<>();
			if (band.getEvents() != null) {
				List<Event> bandsEvents = band.getEvents();
				for (Event e : bandsEvents) {
					events.add(e);
				}
			}
			bands.add(band);
			events.add(event);
			event.setBands(bands);
			band.setEvents(events);
			userDao.addBandToEvent(band, event);
		}
		return "User-vo-home";
	}


	@RequestMapping(path = "updateEventPage.do")
	public String updateEventPage(HttpSession session, @RequestParam("id") int id) {
		Event event = userDao.findEventById(id);
		session.setAttribute("event", event);
		return "UpdateEvent";
	}

	@PostMapping(path = "updateEvent.do")
	public String updateEvent(HttpSession session, 
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("eventDate") String stringEventDate,
			@RequestParam("startTime") String stringStartTime, 
			@RequestParam("endTime") String stringEndTime,
			@RequestParam("image") String image) {
		Event event = (Event) session.getAttribute("event");
		event.setTitle(title);
		event.setDescription(description);
		LocalDate eventDate = LocalDate.parse(stringEventDate);
		event.setEventDate(eventDate);
		LocalTime startTime = LocalTime.parse(stringStartTime);
		event.setStartTime(startTime);
		LocalTime endTime = LocalTime.parse(stringEndTime);
		event.setEndTime(endTime);
		event.setImage(image);
		event = userDao.updateEvent(event);
		session.setAttribute("event", event);
		return "User-vo-home";
	}

	
	@RequestMapping(path = "deleteEventForm.do", method = RequestMethod.POST)
	public String deleteEventForm(HttpSession session, @RequestParam("eventId") int eventId) {
		Event existingEvent = userDao.findEventById(eventId);

		if (existingEvent != null) {
			userDao.deleteEvent(eventId);
		}

		return "User-vo-home";
	}

	@GetMapping(path = "filter.do")
	public ModelAndView filterBy() {
		return null;
	}
	
	@GetMapping(path="ViewVenuePage.do")
	public String viewVenue(HttpSession session, @RequestParam("venueId") int venueId) {
		Venue venue = userDao.findVenueById(venueId);
		session.setAttribute("venue", venue);
		return "ViewVenue";
	}

	@GetMapping(path="ViewBandPage.do")
	public String viewBand(HttpSession session, @RequestParam("bandId") int bandId) {
		Band band = userDao.findBandById(bandId);
		session.setAttribute("band", band);
		return "ViewBand";
	}
	@GetMapping(path="ViewArtistPage.do")
	public String viewArtist(HttpSession session, @RequestParam("artistId") int artistId) {
		Artist artist = userDao.findArtistById(artistId);
		session.setAttribute("artist", artist);
		return "ViewArtist";
	}
	@GetMapping(path="ViewEventPage.do")
	public String viewEvent(HttpSession session, @RequestParam("eventId") int eventId) {
		Event event = userDao.findEventById(eventId);
		session.setAttribute("event", event);
		return "ViewEvent";
	}
}
