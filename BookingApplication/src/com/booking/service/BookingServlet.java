package com.booking.service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.booking.dao.BookingDao;
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
			Event event = BookingDao.makeBooking(guest, request.getParameter("from"), request.getParameter("to"));
			if(event!=null) {
			request.setAttribute("name", guest.getName());
			request.setAttribute("room", event.getRoom().getRoomNo());
			request.setAttribute("booking", event.getReservationID());
			request.setAttribute("from", event.getCheckIn());
			request.setAttribute("to", event.getCheckOut());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/BookingConfirmation.jsp");
			dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			
//			Guest guest = new Guest();
//			guest.setName(request.getParameter("fn") + " " + request.getParameter("ln"));
//			guest.setEmail(request.getParameter("txtEmail"));
//			guest.setContact(request.getParameter("contact"));
//			request.setAttribute("from", request.getParameter("from"));
//			request.setAttribute("to", request.getParameter("to"));
//			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("BookingConfirmation.jsp");
//			dispatcher.forward(request, response);
		response.sendRedirect("BookingError.jsp"); // error page
		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
