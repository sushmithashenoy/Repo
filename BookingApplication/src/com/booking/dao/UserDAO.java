package com.booking.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.booking.model.Event;
import com.booking.model.Guest;
import com.booking.model.Room;
import com.booking.model.User;

public class UserDAO {

	static SessionFactory factory = new Configuration().configure().buildSessionFactory();

	public static boolean login(User user) {
		Session session = factory.openSession();
		session.beginTransaction();
		return user.equals(session.get(User.class, user.getUsername()));
	}

	public static boolean addNewUser(String userName, String password) {
		Session session = factory.openSession();
		session.beginTransaction();
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		session.save(user);
		session.getTransaction().commit();
		session.close();
		return true;

	}

//	public static String makeBooking(String firstName, String lastName, String email, String contact, Date checkIn,
//			Date checkOut) {
//		Session session = factory.openSession();
//		session.beginTransaction();
//		Guest guest = new Guest();
//		guest.setName(firstName + " " + lastName);
//		guest.setEmail(email);
//		guest.setContact(contact);
//		session.save(guest);
//		session.getTransaction().commit();
////		session.close();
//
//		String hql = "" + "from Event event " + "join event.ReservationID reservation " + "join event.roomNo room "
//				+ "join room.roomNo Room " + "where (event.roomNo = room.roomNo)and (event.checkIn >= :checkIn)\r\n"
//				+ "and (event.checkOut <= :checkOut)";
//		List rooms = session.createQuery(hql).setParameter("checkIn", checkIn).setParameter("checkOut", checkOut)
//				.list();
//		rooms.forEach(System.out::println);
//		return (String) rooms.get(0);
//
//	}

	public static Event makeBooking(Guest guest, String from, String to) {
		try {
			addGuest(guest);
		} catch (Exception e) {
			System.out.println("Message " + e.getMessage());
		}
		System.out.println("------------saved---------------to" + to);
		System.out.println("------------saved---------------to" + from);

		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date checkOut = null;
		Date checkIn = null;
		try {
			checkOut = formatter.parse(to);
			checkIn = formatter.parse(from);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getAvailableRoom(checkIn, checkOut);

		Session session = factory.openSession();
		session.beginTransaction();

		Event event = new Event();
		event.setGuest(guest);
		event.setRoom(getAvailableRoom(checkIn, checkOut));
		event.setCheckIn(checkIn);
		event.setCheckOut(checkOut);
		session.save(event);
		session.getTransaction().commit();
		session.close();
		return event;

	}

	private static Room getAvailableRoom(Date checkIn, Date checkOut) {
		Session session = factory.openSession();
		session.createCriteria(Room.class)

				.add(Restrictions.not(Restrictions.in("room.roomNo", getBookedRooms(checkOut, checkIn))));

		Criteria criteria = session.createCriteria(Room.class);
		criteria.add(Restrictions.not(Restrictions.in("roomNo", getBookedRooms(checkOut, checkIn))));

		List<Room> list = criteria.list();
		System.out.println("-------------------------Available Rooms--------------------------------");
		list.forEach(room -> System.out.println(room.getRoomNo()));
		return list.get(0);

	}

//	private static List<Room> getRooms() {
//		Session session = null;
//		List<Room> rooms = new ArrayList<Room>();
//		try {
//			String hql = "From Room";
//			session = factory.openSession();
//			Query query = session.createQuery(hql);
//			System.out.println(query.toString());
//			rooms = query.getResultList();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			session.close();
//		}
//		return rooms;
//	}

	private static List<String> getBookedRooms(Date checkOut, Date checkIn) {
		Session session = null;
		List<String> roomNos = new ArrayList<String>();
		try {
			String hql = "select e.room.roomNo From Event e  Where  (e.checkIn <= :checkIn and  e.checkOut >= :checkIn ) OR (e.checkIn <= :checkOut and e.checkOut >= :checkOut)";
			session = factory.openSession();
			Query query = session.createQuery(hql).setParameter("checkIn", checkIn).setParameter("checkOut", checkOut);
			System.out.println(query.toString());
			roomNos = query.getResultList();
			System.out.println("-------------------------Booked Rooms--------------------------------");
			roomNos.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return roomNos;
	}

//	private static List<Event> getEvents(Date checkOut, Date checkIn) {
//		Session session = null;
//		List<Event> events=new ArrayList<Event>();
//		try {
////		String hql =  "From Event  Where  (checkin <= :checkIn and :checkIn <= checkout ) OR (checkin <= :checkOut and checkout >= :checkOut)";
////		Criteria cr = session.createCriteria(Room.class); 
////		cr.add(Restrictions.not(Restrictions.in("YourCondition", "from Event event where (event.roomNo = room.roomNo) and (event.checkIn >= :checkIn)\r\n" + 
////                "and (event.checkOut <= :checkOut)")));
////		List list = cr.list(); 
//session=factory.openSession();
////		Query query = session.createQuery(hql)
////		.setParameter("checkIn",checkIn).setParameter("checkOut", checkOut);
////		System.out.println(query.toString());
////		events=query.getResultList();
//events=session.createCriteria(Event.class).add(Restrictions.between("checkin", checkIn, checkOut)).add(Restrictions.between("checkOut", checkIn, checkOut)).list();
//		events.forEach(System.out::println);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			session.close();
//		}
//		return events;
//	}

	private static void addGuest(Guest guest) {
		Session session = factory.openSession();
		session.beginTransaction();

		session.save(guest);
		session.getTransaction().commit();
		session.close();
	}

	public static void addRooms() {

		for (int i = 0; i <= 10; i++) {
			Room room = new Room();
			Session session = factory.openSession();
			session.beginTransaction();
			room.setRoomNo("R" + i);
			session.save(room);
			session.getTransaction().commit();
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
