package com.booking.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.booking.model.Event;
import com.booking.model.Guest;
import com.booking.model.Room;

public class BookingDao extends AbstractDao {
	public static Event makeBooking(Guest guest, String from, String to) {
		System.out.println(guest.getName()+" "+guest.getEmail()+" "+guest.getContact()+" "+from+" "+to);
		addGuest(guest);
		
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date checkOut = null;
		Date checkIn = null;
		try {
			checkOut = formatter.parse(to);
			checkIn = formatter.parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Room room = getAvailableRoom(checkIn, checkOut);
		return room == null ? null : addAndReturnEvent(guest, checkIn, checkOut, room);

	}

	private static Event addAndReturnEvent(Guest guest, Date checkIn, Date checkOut, Room room) {
		Session session = null;
		Event event = new Event();
		try {
			session = factory.openSession();
			session.beginTransaction();
			event.setGuest(guest);
			event.setRoom(room);
			event.setCheckIn(checkIn);
			event.setCheckOut(checkOut);
			session.save(event);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return event;
	}

	private static Room getAvailableRoom(Date checkIn, Date checkOut) {
		Session session = null;
		List<Room> rooms = new ArrayList<Room>();
		try {
			session = factory.openSession();
			Criteria criteria = session.createCriteria(Room.class);
			List<Event> bookedEvents=getEvents(checkIn, checkOut);
			List<String> bookedRooms=new ArrayList<String>();
			bookedEvents.forEach(event ->bookedRooms.add(event.getRoom().getRoomNo()));
			System.out.println("-------------------------Booked Rooms--------------------------------");
			bookedEvents.forEach(System.out::println);
//			List<String> bookedRooms=getBookedRooms(checkOut, checkIn);
			criteria.add(Restrictions.not(Restrictions.in("roomNo", bookedRooms)));

			rooms = criteria.list();
//			System.out.println("-------------------------Available Rooms--------------------------------");
//			rooms.forEach(room -> System.out.println(room.getRoomNo()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return rooms.isEmpty()?null:rooms.get(0);

	}

	private static List<Event> getBookedRooms(Date checkOut, Date checkIn) {
		Session session = null;
		List roomNos = null;
		try {
			String hql = "From EVENT e  Where  (e.checkIn <= :checkIn and  e.checkOut >= :checkIn ) OR (e.checkIn <= :checkOut and e.checkOut >= :checkOut)";
			session = factory.openSession();
			Query query = session.createQuery(hql).setParameter("checkIn", checkIn).setParameter("checkOut", checkOut);
			System.out.println(query.toString());
			return query.list();
//			System.out.println("-------------------------Booked Rooms--------------------------------");
//			roomNos.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			session.close();
		}
		return  roomNos;
	}
	
	public static List<Event> getAllEvents() {
		Session session = null;
		List roomNos = null;
		try {
			String hql = "From EVENT ORDER BY date(checkIn) asc";
			session = factory.openSession();
			return session.createQuery(hql).list();
			
//			System.out.println("-------------------------Booked Rooms--------------------------------");
//			roomNos.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return  roomNos;
	}
	
//	private static List getBookedRooms(Date checkOut, Date checkIn) {
//		Session session = null;
//		List roomNos = null;
//		try {
//			String sql = "select e.roomno From EVENT e  Where  (e.checkIn <= :checkIn and  e.checkOut >= :checkIn ) OR (e.checkIn <= :checkOut and e.checkOut >= :checkOut)";
//			session = factory.openSession();
//			Query query = session.createSQLQuery(sql).setParameter("checkIn", checkIn).setParameter("checkOut", checkOut);
//			System.out.println(query.toString());
//			return query.list();
////			System.out.println("-------------------------Booked Rooms--------------------------------");
////			roomNos.forEach(System.out::println);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			session.close();
//		}
//		return  roomNos;
//	}

	private static void addGuest(Guest guest) {
		Session session = null;
		try {
			session = factory.openSession();
			session.beginTransaction();
			session.save(guest);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	


	public static void main(String[] args) {
		Guest guest = new Guest();
		guest.setName("sushmitha" + " " + "shenoy");
		guest.setEmail("myemail");
		guest.setContact("01234567");
		makeBooking(guest, "03/25/2019", "04/26/2019");
	}


}
