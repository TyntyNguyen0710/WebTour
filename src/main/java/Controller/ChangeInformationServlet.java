package Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.customerDAO;

@WebServlet("/ChangeInformation")
public class ChangeInformationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Xử lý yêu cầu POST
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		customerDAO.getIntance().update(name, phone, address, username);
		response.sendRedirect("information.jsp");
	}

}
