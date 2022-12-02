package edu.lehigh.cse216.jub424.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import edu.lehigh.cse216.jub424.backend.data_manager.*;

/**
 * Database is to setup the connectio of the database also set up the manager of
 * each table
 * 
 * @author Junchen Bao
 * @version 1.0.0
 * @since 2022-09-16
 */
public class Database {
    /**
     * The connection to the database. When there is no connection, it should
     * be null. Otherwise, there is a valid open connection
     */
    public Connection mConnection;

    /**
     * mIdeaTableManager deal with the ideas table
     */
    public IdeaTableManager mIdeaTableManager;

    /**
     * mLikeTableManager deal with the likes table
     */
    public LikeTableManager mLikeTableManager;

    /**
     * mDislikeTableManager deal with the dislikes table
     */
    public DislikeTableManager mDislikeTableManager;

    /**
     * mCommentsTableManager deal with the comments table
     */
    public CommentsTableManager mCommentsTableManager;

    /**
     * mUsersTableManager deal with the users table
     */
    public UsersTableManager mUsersTableManager;
    /**
     * mResourceTableManager deals with the resources table
     */
    public ResourceTableManager mResourceTableManager;
    /**
     * mResourceTableManager deals with the resources table
     */
    public GoogleDriveManager mGoogleDriveManager;

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String ip, String port, String user, String pass) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
            db.mIdeaTableManager = new IdeaTableManager(db.mConnection);
            db.mLikeTableManager = new LikeTableManager(db.mConnection);
            db.mUsersTableManager = new UsersTableManager(db.mConnection);
            db.mCommentsTableManager = new CommentsTableManager(db.mConnection);
            db.mDislikeTableManager = new DislikeTableManager(db.mConnection);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }
}