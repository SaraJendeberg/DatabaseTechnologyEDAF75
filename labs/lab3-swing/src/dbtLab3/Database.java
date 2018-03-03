package dbtLab3;

import java.sql.*;
import java.util.*;

/**
 * Database is an interface to the college application database, it
 * uses JDBC to connect to a SQLite3 file.
 */
public class Database {

    /**
     * The database connection.
     */
    private Connection conn;

    /**
     * Creates the database interface object. Connection to the
     * database is performed later.
     */
    public Database() {
        conn = null;
    }

    /**
     * Opens a connection to the database, using the specified
     * filename (if we'd used a traditional DBMS, such as PostgreSQL
     * or MariaDB, we would have specified username and password
     * instead).
     */
    public boolean openConnection(String filename) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Closes the connection to the database.
     */
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the connection to the database has been established
     * 
     * @return true if the connection has been established
     */
    public boolean isConnected() {
        return conn != null;
    }

    /* ================================== */
    /* --- insert your own code below --- */
    /* ===============================*== */
    
    /*
    public List<...> ...(...) {
        List<...> found = new LinkedList<>();
        String query =
            "SELECT  ...\n" +
            "FROM    ...\n" +
            "...\n";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ...);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                found.add(new ...(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }
    */
    
    public boolean userExists(String user) {
    	Boolean found = false;
    	String query =
                "SELECT	* " +
                "FROM	users " + 
                "WHERE	u_name = ?";
    	try (PreparedStatement ps = conn.prepareStatement(query)) {
    		ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	found = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    	return found;
    }
    
    public List<String> getMovieNames(){
    	List<String> movies = new LinkedList<String>();
    	String query =
                "SELECT	title " +
                "FROM	movies ";
    	try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	//get value of column in the current row of rs
                movies.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return movies;
    }
    
    /* Returns a list of performance dates for the movie_title movie*/
    public List<String> getPerformanceDates(String movie){
    	List<String> dates = new LinkedList<String>();
    	String query =
                "SELECT	showdate " +
                "FROM	performances " +
                "WHERE	movie = ? ";
    	try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, movie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	//get value of column in the current row of rs
                dates.add(rs.getString("showdate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return dates;
    }
    
    public Performance getPerformance(String movie, String date){
    	List<String> colNames = new LinkedList<String>(Arrays.asList("movie", "showdate", "theater", "nbr_seats"));
    	List<String> colValues = new LinkedList<String>();
    	String query =
                "SELECT	* " +
                "FROM	performances " +
                "WHERE	movie = ? AND showdate = ? ";
    	try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, movie);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	for(String colName : colNames){
            		colValues.add(rs.getString(colName));
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	Performance currPerf = new Performance(colValues);
    	return currPerf;
    }
    
    public boolean makeReservation(String movie, String date){
    	boolean isReserved = false;
    	Performance perf = getPerformance(movie, date);
    	if (Integer.parseInt(perf.getNbrSeats()) > 0) { //check if seats available
	    	String insert = "INSERT INTO reservations " +
	                "(reservation_id, user_id, showdate, movie_title) " +
	                "VALUES " +
	                "(null, ?, ?, ?); ";
	    	String update = "UPDATE performances " +
	                "SET nbr_seats = nbr_seats-1 " +
	                "WHERE showdate = ? AND movie = ? ;";
	    	try {
				conn.setAutoCommit(false);
				try (PreparedStatement psInsert = conn.prepareStatement(insert);
		    			PreparedStatement psUpdate = conn.prepareStatement(update)) {
		    		psInsert.setString(1, CurrentUser.instance().getCurrentUserId());
		    		psInsert.setString(2, date);
		    		psInsert.setString(3, movie);
		    		psInsert.executeUpdate();
		    		
		    		psUpdate.setString(1, date);
		    		psUpdate.setString(2, movie);
		    		psUpdate.executeUpdate();
		            conn.commit();
					isReserved = true;
		        } catch (SQLException e2) {
		        	e2.printStackTrace();
		        	if(conn != null){
		        		try{
		        			conn.rollback();
		        		} catch (SQLException se) {
		        			System.err.print(se.toString());
		        		}
		        	}
		        }
			conn.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    	}
    	return isReserved;
    }
}
