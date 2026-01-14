package Controller;

import java.io.IOException;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.customerDAO;

@WebServlet("/SendOTPViaForgetPasswordServlet")
public class SendOTPViaForgetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Lấy giá trị tourID từ yêu cầu
		
		String username = request.getParameter("username");
		String email = customerDAO.getIntance().selectEmailByUsername(username);
		if(email != null && !email.isEmpty()) { 
		// Sinh mã OTP
		String otp = generateOTP();

		// Lưu OTP vào session
		HttpSession session = request.getSession();
		session.setAttribute("otpPassword", otp);
		session.setAttribute("email", email);
		session.setAttribute("username", username);

		// Gửi OTP đến địa chỉ email
		sendOTPEmail(email, otp);

		// Chuyển hướng đến trang verifyOTP.jsp
		response.sendRedirect("verifyOTPForgetPassword.jsp");
	} else {
		response.sendRedirect("forgetPasswordRegain.jsp");
		
	}
	}
	private String generateOTP() {
		// Logic sinh mã OTP ở đây (ví dụ: mã ngẫu nhiên)
		return String.valueOf((int) (Math.random() * 9000) + 1000);
	}

	private void sendOTPEmail(String toEmail, String otp) {
		final String username = "vanluan0903@gmail.com";
		final String password = "hgxt eszi yqcs uhzb";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Mã xác thực");
			message.setText("Mã xác thực của bạn là: " + otp);

			Transport.send(message);

			System.out.println("OTP Email sent successfully!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
