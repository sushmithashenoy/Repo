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

public class UserDAO extends AbstractDao {

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
}
