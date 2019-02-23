package com.booking.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
	
	public static String makeBooking(String firstName, String lastName,String email,String contact,Date checkIn,Date checkOut) {
		Session session = factory.openSession();
		session.beginTransaction();
		Guest guest = new Guest();
		guest.setName(firstName+" "+lastName);
		guest.setEmail(email);
		guest.setContact(contact);
		session.save(guest);
		session.getTransaction().commit();
		session.close();
		
		String hql = "" +
                "from Event event " +
                "join event.ReservationID reservation " +
                "join event.roomNo room " +
                "join room.roomNo Room " +
                "where (event.roomNo = room.roomNo)and (event.checkIn >= :checkIn)\r\n" + 
                "and (event.checkOut <= :checkOut)";
		List rooms = session.createQuery(hql)
		.setParameter("checkIn",checkIn).setParameter("checkOut", checkOut)
		.list();
		rooms.forEach(System.out::println);
		return (String) rooms.get(0);

	}
	
	public static Event makeBooking(Guest guest,String from,String to) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		session.save(guest);
		session.getTransaction().commit();
		session.close();
		System.out.println("------------saved---------------to"+to);
		System.out.println("------------saved---------------to"+from);
		
		
		DateFormat formatter = new SimpleDateFormat("MM/DD/yyyy");
		Date checkOut=null;
		Date checkIn=null;
		try {
			checkOut = formatter.parse(to);
			checkIn = formatter.parse(from);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(checkOut);
		System.out.println(checkIn);
		
		String hql = "" +
                "from Event event " +
                "join event.ReservationID reservation " +
                "join event.roomNo room " +
                "join room.roomNo Room " +
                "where (event.roomNo = room.roomNo)and (event.checkIn >= :checkIn)\r\n" + 
                "and (event.checkOut <= :checkOut)";


		Query query = session.createQuery(hql)
		.setParameter("checkIn",checkIn).setParameter("checkOut", checkOut);
		System.out.println(query.toString());
		List<Room> rooms=query.getResultList();
		rooms.forEach(System.out::println);
		
		
		Event event=new Event();
		event.setGuest(guest);
		event.setRoom(rooms.get(0));
		event.setCheckIn(checkIn);
		event.setCheckOut(checkOut);
		return event;

	}
	
	public static void addRooms() {
	
	
		for(int i=10;i<=10;i++) {
			Room room = new Room();
			Session session = factory.openSession();
		session.beginTransaction();
		room.setRoomNo("R"+i);
		session.save(room);
		session.getTransaction().commit();
		session.close();
		}
	}

	public static void main(String[] args) {
		Guest guest = new Guest();
		guest.setName("firstname" + " " + "ln");
		guest.setEmail("txtEmail");
		guest.setContact("contact");
		Session session = factory.openSession();
		session.beginTransaction();
		
		session.save(guest);
		session.getTransaction().commit();
		session.close();
	}
}
