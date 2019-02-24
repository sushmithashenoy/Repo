package com.booking.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.booking.model.Event;

public class AbstractDao {
	public static SessionFactory factory = new Configuration().configure().buildSessionFactory();
//	public static void addRooms() {
//
//		for (int i = 0; i <= 10; i++) {
//			Room room = new Room();
//			Session session = factory.openSession();
//			session.beginTransaction();
//			room.setRoomNo("R" + i);
//			session.save(room);
//			session.getTransaction().commit();
//			session.close();
//		}
//	}
	
	protected static List<Event> getEvents( Date checkIn ,Date checkOut) {
	Session session = null;
	List<Event> events=new ArrayList<Event>();
	try {
//	String hql =  "From Event  Where  (checkin <= :checkIn and :checkIn <= checkout ) OR (checkin <= :checkOut and checkout >= :checkOut)";
//	Criteria cr = session.createCriteria(Room.class); 
//	cr.add(Restrictions.not(Restrictions.in("YourCondition", "from Event event where (event.roomNo = room.roomNo) and (event.checkIn >= :checkIn)\r\n" + 
//            "and (event.checkOut <= :checkOut)")));
//	List list = cr.list(); 
session=factory.openSession();
//	Query query = session.createQuery(hql)
//	.setParameter("checkIn",checkIn).setParameter("checkOut", checkOut);
//	System.out.println(query.toString());
//	events=query.getResultList();
events=session.createCriteria(Event.class).add(Restrictions.between("checkIn", checkIn, checkOut)).add(Restrictions.between("checkOut", checkIn, checkOut)).list();
for(Event event:events) {
	System.out.println("eveent room"+event.getRoom().getRoomNo());
	
}
//	events.forEach(System.out::println);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		session.close();
	}
	return events;
}
}
