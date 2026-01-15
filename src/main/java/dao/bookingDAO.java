package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DataBase.JDBCUltil;
import model.Booking;
import model.Customer;

public class bookingDAO implements DAOInterface<Booking> {
	public static bookingDAO getIntance() {
		return new bookingDAO();
	}

	@Override
	public int insert(Booking booking) throws ClassNotFoundException {
		int result = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Get a connection (you need to implement JDBCUltil.getConnection() or similar)
			connection = JDBCUltil.getConnection();
			// Define the SQL query for insertion
			String sql = "INSERT INTO Booking (departureDate, noAdults, noChildren, email ,tourId, customerId) VALUES (? ,?, ?, ?, ?, ?)";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);

			// Set the parameters for the PreparedStatement
			preparedStatement.setDate(1, new java.sql.Date(booking.getDepartureDate().getTime()));
			preparedStatement.setInt(2, booking.getNoAdults());
			preparedStatement.setInt(3, booking.getNoChildren());
			preparedStatement.setString(4, booking.getEmail());
			preparedStatement.setInt(5, booking.getTourID());
			preparedStatement.setInt(6, booking.getCustomerID());

			// Execute the insertion query
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} finally {
			// Close resources in the reverse order of their creation
			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return result;
	}

	public int insertNoLogin(Booking booking) throws ClassNotFoundException {
		int result = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Get a connection (you need to implement JDBCUltil.getConnection() or similar)
			connection = JDBCUltil.getConnection();

			// Define the SQL query for insertion
			String sql = "INSERT INTO Booking (departureDate, noAdults,noChildren,email ,tourId) VALUES (?, ?, ?,?, ?)";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);

			// Set the parameters for the PreparedStatement
			preparedStatement.setDate(1, new java.sql.Date(booking.getDepartureDate().getTime()));
			preparedStatement.setInt(2, booking.getNoAdults());
			preparedStatement.setInt(3, booking.getNoChildren());
			preparedStatement.setString(4, booking.getEmail());

			preparedStatement.setInt(5, booking.getTourID());

			// Execute the insertion query
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} finally {
			// Close resources in the reverse order of their creation
			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return result;
	}

	@Override
	public int update(Booking booking) {
		int result = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Assuming you have a Connection object named 'connection'
			connection = JDBCUltil.getConnection();

			// Define the SQL query for updating the Booking record
			String sql = "UPDATE Booking SET departureDate=?, noAdults=?, noChildren=?, tourId=?, customerId=? WHERE id=?";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);

			// Set the parameters for the PreparedStatement
			preparedStatement.setDate(1, new java.sql.Date(booking.getDepartureDate().getTime()));
			preparedStatement.setInt(2, booking.getNoAdults());
			preparedStatement.setInt(3, booking.getNoChildren());
			preparedStatement.setInt(4, booking.getTourID());
			preparedStatement.setInt(5,
					customerDAO.getIntance().selectByID(booking.getCustomer().getUser().getUsername()).getID());
			preparedStatement.setLong(6, booking.getId());

			// Execute the update query
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Close resources in the reverse order of their creation
			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return result;
	}

	@Override
	public int delete(Booking booking) {
		int result = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Assuming you have a Connection object named 'connection'
			connection = JDBCUltil.getConnection();

			// Define the SQL query for deleting the Booking record
			String sql = "DELETE FROM Booking WHERE id=?";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);

			// Set the parameter for the PreparedStatement
			preparedStatement.setLong(1, booking.getId());

			// Execute the delete query
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Close resources in the reverse order of their creation
			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return result;
	}

	@Override
	public ArrayList<Booking> selectAll() {
		ArrayList<Booking> bookings = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Assuming you have a Connection object named 'connection'
			connection = JDBCUltil.getConnection();

			// Define the SQL query for selecting all Booking records
			String sql = "SELECT * FROM Booking";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);

			// Execute the query
			resultSet = preparedStatement.executeQuery();

			// Iterate through the result set and create Booking objects
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				Date departureDate = resultSet.getDate("departureDate");
				int noAdults = resultSet.getInt("noAdults");
				int noChildren = resultSet.getInt("noChildren");
				String email = resultSet.getString("email");
				int tourId = resultSet.getInt("tourId");
				int customerId = resultSet.getInt("customerId");

				// Fetch associated Tour and Customer objects based on their IDs

				Customer customer = customerDAO.getIntance().selectByID(String.valueOf(customerId));

				// Create a Booking object and add it to the list
				Booking booking = new Booking(customer, departureDate, noAdults, noChildren, email, tourId);
				booking.setId(id);
				bookings.add(booking);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Close resources in the reverse order of their creation

			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return bookings;
	}

	@Override
	public Booking selectByID(String email) throws ClassNotFoundException {
		Booking booking = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Assuming you have a Connection object named 'connection'
			try {
				connection = JDBCUltil.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Define the SQL query for selecting a Booking by ID
			String sql = "SELECT * FROM Booking WHERE email = ?";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);

			// Execute the query
			resultSet = preparedStatement.executeQuery();

			// Check if a result is found and create a Booking object
			if (resultSet.next()) {
				long bookingId = resultSet.getLong("id");
				Date departureDate = resultSet.getDate("departureDate");
				int noAdults = resultSet.getInt("noAdults");
				int noChildren = resultSet.getInt("noChildren");
				int tourId = resultSet.getInt("tourId");
				int customerId = resultSet.getInt("customerId");

				// Fetch associated Tour and Customer objects based on their IDs
				Customer customer = customerDAO.getIntance().selectByIDCustomer(Integer.valueOf(customerId));

				// Create a Booking object
				booking = new Booking(customer, departureDate, noAdults, noChildren, email, tourId, customerId);
				booking.setId(bookingId);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} finally {
			// Close resources in the reverse order of their creation
			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return booking;
	}

	public List<Booking> selectBookingsByEmail(String emailCus) throws ClassNotFoundException {
		List<Booking> bookings = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = customerDAO.getIntance().selectByEmail(emailCus);
		try {
			// Assuming you have a Connection object named 'connection'
			connection = JDBCUltil.getConnection();

			// Define the SQL query for selecting Bookings by email
			String sql = "SELECT * FROM Booking WHERE email = ?";

			// Create a PreparedStatement with the SQL query
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, emailCus);

			// Execute the query
			resultSet = preparedStatement.executeQuery();

			// Iterate through the result set and create Booking objects
			while (resultSet.next()) {
				long bookingId = resultSet.getLong("id");
				Date departureDate = resultSet.getDate("departureDate");
				int noAdults = resultSet.getInt("noAdults");
				int noChildren = resultSet.getInt("noChildren");
				int tourId = resultSet.getInt("tourId");
				String email = resultSet.getString("email");
				int customerId = resultSet.getInt("customerId");

				// Create a Booking object and add to the list
				Booking booking = new Booking(customer, departureDate, noAdults, noChildren, email, tourId, customerId);
				booking.setId(bookingId);
				bookings.add(booking);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception based on your application's needs
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Close resources in the reverse order of their creation
			JDBCUltil.closePreparedStatement(preparedStatement);
			JDBCUltil.closeConnection(connection);
		}

		return bookings;
	}

	public int deleteByID(long id) {
		Connection con = null;
		try {
			con = JDBCUltil.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String sql = "DELETE FROM Booking WHERE id=?";
		int result = 0;

		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setLong(1, id);

			// Execute the delete statement
			result = pst.executeUpdate();

			// Close the PreparedStatement and Connection
			pst.close();
			JDBCUltil.closeConnection(con);
		} catch (SQLException e) {
			// Handle any SQL errors
			e.printStackTrace();
		}

		return result;
	}

}
