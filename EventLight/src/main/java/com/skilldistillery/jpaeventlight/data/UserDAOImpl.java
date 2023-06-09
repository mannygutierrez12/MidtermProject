package com.skilldistillery.jpaeventlight.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.skilldistillery.jpaeventlight.entities.Address;
import com.skilldistillery.jpaeventlight.entities.Artist;
import com.skilldistillery.jpaeventlight.entities.Band;
import com.skilldistillery.jpaeventlight.entities.Event;
import com.skilldistillery.jpaeventlight.entities.User;
import com.skilldistillery.jpaeventlight.entities.Venue;

@Service
@Transactional
public class UserDAOImpl implements UserDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		User user = null;
		String jpql = "SELECT u FROM User u WHERE u.username = :un AND u.password = :pw AND u.enabled = true";
		try {
			user = em.createQuery(jpql, User.class).setParameter("un", username).setParameter("pw", password)
					.getSingleResult();
			return user;
		} catch (Exception e) {
			System.err.println("Invalid login");
			return user;
		}
	}

//	@Override
//	public void loginService() {
//		//todo
//	}

	@Override
	public User signUp(User user, Address address) {
		em.persist(address);
		em.persist(user);
		// em.flush();
		return user;
	}

//	@Override
//	public TVShow add(TVShow show) {
//		em.persist(show);
//		em.flush();
//		return show;
//	}

//	@Override
//	public void showFavorites() {
//		//todo
//	}

	@Override
	public Artist createArtist(Artist artist) {
		em.persist(artist);
		return artist;
	}

	@Override
	public Band createBand(Band band) {
		em.persist(band);
		return band;

	}

	@Override
	public Venue createVenue(Venue venue, Address address) {
		em.persist(address);
		em.persist(venue);
		return venue;
	}

	@Override
	public Event createEvent(Event event) {
		em.persist(event);
		return event;

	}

//	@Override
//	public void updateArtist() {
//		
//	}
//	
//	@Override
//	public void updateBand() {
//		
//	}

	@Override
	public Venue updateVenue(Venue updatedVenue) {
		return em.merge(updatedVenue);
	}

	@Override
	public Address updateAddress(Address updatedAddress) {
		return em.merge(updatedAddress);
	}

	@Override
	public Event updateEvent(Event updatedEvent) {
		return em.merge(updatedEvent);

	}

	@Override
	public boolean deleteArtist(int artistId) {
		Artist artist = em.find(Artist.class, artistId);
		em.remove(artist);
		if (artist == null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean deleteBand(int bandId) {
		Band band = em.find(Band.class, bandId);
		em.remove(band);
		if (band == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteVenue(int venueId) {
		Venue venue = em.find(Venue.class, venueId);
		em.remove(venue);
		if (venue == null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean deleteEvent(int eventId) {
		Event event = em.find(Event.class, eventId);
		if (event == null) {
			return false;
		}
		event.setEnabled(false);

		return true;
	}

	@Override
	public boolean deleteUser(int userId) {
		User user = em.find(User.class, userId);
		em.remove(user);
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}
//	@Override
//	public void filter() {
//		
//	}

	@Override
	public List<Event> findAllEvents() {
		String jpql = "SELECT e FROM Event e WHERE e.enabled = true";
		List<Event> events = em.createQuery(jpql, Event.class).getResultList();
		Collections.sort(events, new Comparator<Event>() {
			  public int compare(Event o1, Event o2) {
			      return o1.getEventDate().compareTo(o2.getEventDate());
			  }
			});
				return events;
	}

	@Override
	public List<Event> findEventsByVenueId(int venueId) {
		String jpql = "SELECT e FROM Event e WHERE e.venue.id = :venueId and e.enabled = true";
		List<Event> eventsById = em.createQuery(jpql, Event.class).setParameter("venueId", venueId).getResultList();
		Collections.sort(eventsById, new Comparator<Event>() {
			  public int compare(Event o1, Event o2) {
			      return o1.getEventDate().compareTo(o2.getEventDate());
			  }
			});
		return eventsById;

	}

	@Override
	public Event findEventById(int eventId) {
		Event event = em.find(Event.class, eventId);
		if (!event.isEnabled()) {
			event = null;
		}
		return event;
	}
	
	@Override

	public Venue findVenueById(int venueId) {
		Venue venue = em.find(Venue.class, venueId);
		return venue;
	}
	@Override
	public Band findBandById(int bandId) {
		Band band = em.find(Band.class, bandId);
		return band;
	}
	@Override
	public Artist findArtistById(int artistId) {
		Artist artist = em.find(Artist.class, artistId);
		return artist;
	}
	
	@Override
	public List<Event> addToFavoriteEvents(int eventId, int userId){
		Event event = em.find(Event.class, eventId);
		User user = em.find(User.class, userId);
		List<Event> favorites = user.getFavoriteEvents();
		if (!favorites.contains(event)) {
			favorites.add(event);
			user.setFavoriteEvents(favorites);
		}
		return favorites;
	}
	@Override
	public List<Event> removeFavoriteEvent(int eventId, int userId){
		Event event = em.find(Event.class, eventId);
		User user = em.find(User.class, userId);
		List<Event> favorites = user.getFavoriteEvents();
		if (favorites.contains(event)) {
			favorites.remove(event);
			user.setFavoriteEvents(favorites);
		}
		return favorites;
	}
	

	public List<Band> listAllBands(){
		String jpql = "SELECT b FROM Band b";
		return em.createQuery(jpql, Band.class).getResultList();
	}
	
	@Override 
	public Band findBandByName(String bandName) {
		String jpql = "Select b From Band b where b.name = :bandName ";
		return em.createQuery(jpql, Band.class).setParameter("bandName", bandName).getSingleResult();
	}
	
	@Override
	public List<Artist> findAllArtists() {
		String jpql = "Select a From Artist a";
		return em.createQuery(jpql, Artist.class).getResultList();
	}
	
	@Override
	public void addBandToEvent(Band band, Event event) {
		em.merge(band);
		em.merge(event);
	}
	

}
