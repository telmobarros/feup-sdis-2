package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class Files {

	/**
	 * Gets all files from a user from database
	 * @param c Database connection
	 * @param email Email of the user to retrieve its files
	 * @return Arraylist of string arrays containing file ID and name of a file 
	 * @throws SQLException
	 */
	public static ArrayList<String[]> getFileNames(Connection c,String email) throws SQLException{		
		//Get info from files
		PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM (p2p.files JOIN p2p.users USING (user_id)) WHERE email = ?");
		preparedStatement.setString(1, email);
		ResultSet rs = preparedStatement.executeQuery();

		ArrayList<String[]> files = new ArrayList<>();
		while(rs.next()){
			String file_id = rs.getString("file_id");
			String name = rs.getString("name");
			System.out.println(name);
			String added_time = rs.getTimestamp("added_time").toString();

			String[] file = new String[]{file_id,name,added_time};
			files.add(file);
		};

		return files;
	}

	public static ArrayList<String[]> getPublicFiles(Connection c) throws SQLException{		
		//Get info from files
		PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM p2p.files WHERE public = true");
		ResultSet rs = preparedStatement.executeQuery();

		ArrayList<String[]> files = new ArrayList<>();
		while(rs.next()){
			String file_id = rs.getString("file_id");
			String name = rs.getString("name");
			System.out.println(name);
			String added_time = rs.getTimestamp("added_time").toString();

			String[] file = new String[]{file_id,name,added_time};
			files.add(file);
		};

		return files;
	}

	/**
	 * Gets all deleted files ids from database
	 * @param c Database connection
	 * @return Set with deleted files IDs
	 * @throws SQLException
	 */
	public static HashSet<Integer> getDeletedFiles(Connection c) throws SQLException{
		PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM p2p.deleted_files");
		ResultSet rs = preparedStatement.executeQuery();

		HashSet<Integer> files = new HashSet<>();
		while(rs.next()){
			files.add(rs.getInt("file_id"));
		};

		return files;
	}

	/**
	 * Move file id wanted to be deleted to deleted_files table in database
	 * @param c Database connection
	 * @param file_id File ID wanted to delete
	 * @return Success value of the operation
	 * @throws SQLException
	 */
	public static boolean moveFileToDeleted(Connection c, int file_id) throws SQLException {
		PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM p2p.files WHERE file_id=?");
		preparedStatement.setInt(1, file_id);

		preparedStatement.executeUpdate();

		preparedStatement = c.prepareStatement("INSERT INTO p2p.deleted_files(file_id) VALUES (?)");
		preparedStatement.setInt(1, file_id);

		preparedStatement.executeUpdate();

		return true;
	}
}