package com.booking.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.booking.dao.UserDAO;
import com.booking.model.Event;
import com.booking.model.Guest;

/**
 * Servlet implementation class LoginServlet
 */
public class BookingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		try {

			Guest guest = new Guest();
			guest.setName(request.getParameter("fn") + " " + request.getParameter("ln"));
			guest.setEmail(request.getParameter("txtEmail"));
			guest.setContact(request.getParameter("contact"));
			System.out.println("in booking servlet");
			Event event = UserDAO.makeBooking(guest, request.getParameter("from"), request.getParameter("to"));
			response.sendRedirect("BookingConfirmation.jsp"); // logged-in page
		} catch (Exception e) {
			response.sendRedirect("Error.jsp"); // error page
		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
