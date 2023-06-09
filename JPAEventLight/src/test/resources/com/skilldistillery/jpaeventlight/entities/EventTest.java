package com.skilldistillery.jpaeventlight.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventTest {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Event event;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("JPAEventLight");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emf.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		em = emf.createEntityManager();
		event = em.find(Event.class, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		em.close();
		event = null;
	}

	@Test
	void test_Event_basic_mapping() {
		assertNotNull(event);
		assertEquals(Month.JULY, event.getEventDate().getMonth());
		assertTrue(event.getUsersFavorited().contains(em.find(User.class, 1)));
	}
	@Test
	void test_Event_to_Venue_ManyToOne() {
		assertNotNull(event);
		assertEquals("FreeBird", event.getVenue().getName());
	}
	
	@Test
	void test_Event_ManyToMany() {
		assertNotNull(event);
		assertNotNull(event.getBands());
		assertTrue(event.getBands().size()  == 1);
	}
	
	@Test
	void test_User_to_EventRatings_OnetoMany() {
		assertNotNull(event);
		assertTrue(event.getEventRatings().size() > 0);

	}

}
