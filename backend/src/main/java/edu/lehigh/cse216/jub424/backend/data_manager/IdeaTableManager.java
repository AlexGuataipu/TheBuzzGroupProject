package edu.lehigh.cse216.jub424.backend.data_manager;

import java.sql.*;
import java.util.ArrayList;

import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * IdeaTableManager all function interact with the ideas table
 * @author Junchen Bao
 * @version 1.0.0
 * @since 2022-09-16
 */
public class IdeaTableManager {
    /**
     * A prepared statement for getting all data in the database
     */
    private static PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private static PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private static PreparedStatement mUpdateOne;

    /**
     * This constructer set up all the sql query for the ideas table use the
     * mConnection
     * 
     * @param mConnection the connection to the database
     * 
     * @throws SQLException when things goes worng in sql
     */
    public IdeaTableManager(Connection mConnection) throws SQLException {
        // CREATE TABLE ideas (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL,
        // message VARCHAR(500) NOT NULL)

        mSelectAll = mConnection.prepareStatement("SELECT * FROM ideas ORDER BY id DESC");
        mSelectOne = mConnection.prepareStatement("SELECT * from ideas WHERE id=?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM ideas WHERE id = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO ideas VALUES (default, ?, ?)");
        mUpdateOne = mConnection.prepareStatement("UPDATE ideas SET subject = ?, message = ? WHERE id = ?");
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<Idea> selectAllIdeas() {
        ArrayList<Idea> res = new ArrayList<Idea>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new Idea(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("message")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all idea for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The idea for the requested row, or null if the ID was invalid
     */
    public Idea selectOneIdea(int id) {
        Idea res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Idea(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    public int deleteIdeas(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    public int insertIdea(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    
    /**
     * Update the subject and message for a row in the database
     * 
     * @param id The id of the row to update
     * @param subject The subject for update
     * @param message The new message contents
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int updateIdea(int id, String subject, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, subject);
            mUpdateOne.setString(2, message);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
