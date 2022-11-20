// package edu.lehigh.cse216.jub424.admin;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.sql.*;
// import java.util.Map;




// /**
//  * AppTest Unit tests for Admin
//  */
// public class AppTest extends TestCase{
//     /**
//      *  Test program for insert a row to the ideas table
//      */
//     public void testInsertIdeas(){
//         //get connected to the database
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mIdeaTable = new IdeaTable(mDatabase.mConnection);
//         }catch(Exception e){
//             fail("Fail insert");
//         }
//         assertFalse(mDatabase.mIdeaTable.insertIdea("Admin", "Unit Testing", "alg223@lehigh.edu") == 0);
//         ResultSet rs;
//         int id = -1;
//         String subject = "";
//         String message = "";
//         try{
//             Statement s = mDatabase.mConnection.createStatement();
//             //get the id of last_insert_row
//             String getID = "SELECT max(ID) AS id FROM ideas;";
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                 id=rs.getInt("id");
//             }
//             //select from last_insert_row
//             String getInfo = "SELECT * from ideas where id="+id;
//             rs=s.executeQuery(getInfo);
//             //pick the subject and message attributes from the last_insert_row
//             while(rs.next()){
//                 subject=rs.getString("subject");
//                 message=rs.getString("message");
//             }
//         }catch(SQLException e){
//             e.fillInStackTrace();
//         }
//         //check if the subject and message match
//         assertEquals("Admin", subject);
//         assertEquals("Unit Testing", message);
//     }


//     /**
//      *  Test program for update a row to the ideas table
//      */
//     public void testUpdateIdeas(){       
//         //get connected to the database 
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mIdeaTable=new IdeaTable(mDatabase.mConnection);
//         }catch(Exception e){
//             fail("Fail update");
//         }
//         ResultSet rs;
//         int id=-1;
//         String subject="";
//         String message="";
//         try{
//             Statement s=mDatabase.mConnection.createStatement();
//             //get the id of last_insert_row
//             String getID="SELECT max(ID) as id from ideas;";
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                  id=rs.getInt("id");
//             }
//             assertFalse(mDatabase.mIdeaTable.updateIdea(id,"subject1", "message1")==-1);
//             //select from last_insert_row
//             String getResult="SELECT * from ideas";
//             rs=s.executeQuery(getResult);
//             //pick the subject and message attrubutes from the last_insert_row
//             while(rs.next()){
//                 subject=rs.getString("subject");
//                 message=rs.getString("message");
//             }
//         }catch(SQLException e){
//             e.fillInStackTrace();
//         }
//         //check if the string in the subject and message attributes match the new subject and message
//         assertEquals("subject1", subject);
//         assertEquals("message1", message);
//     }

//     /**
//      *  Test program for read a row to the ideas table
//      */
//     public void testSelectIdeas(){
//         //get connected to the database
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mIdeaTable=new IdeaTable(mDatabase.mConnection);
//         }catch(Exception e){
//             fail("Fail select");
//         }
//         ResultSet rs;
//         int id=-1;
//         try{
//             Statement s=mDatabase.mConnection.createStatement();
//             String getID="SELECT max(ID) AS id FROM ideas;";
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                  id=rs.getInt("id");
//             }
//             //check if the select statement get a null value
//             assertFalse(mDatabase.mIdeaTable.selectOneIdea(id)==null);
//         }catch(SQLException e){
//             e.fillInStackTrace();
//         }
//     }

//     /**
//      *  Test program for delete a row to the ideas table
//      */
//     public void testDeleteIdeas(){
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mIdeaTable=new IdeaTable(mDatabase.mConnection);
//         }catch(Exception e){
//             fail("Fail delete");
//         }
//         ResultSet rs;
//         int id=-1;
//         int newId=-1;
//         try{
//             //get the id of last_insert_row, since the last insert row is inserted just now during testing, we will delete it
//             Statement s=mDatabase.mConnection.createStatement();
//             String getID="SELECT max(ID) as id from ideas;";
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                  id=rs.getInt("id");
//             }
//             assertFalse(mDatabase.mIdeaTable.deleteIdea(id)==-1);
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                  newId=rs.getInt("id");
//             }
//             //check if the max(id) after deleted is getting small, since for this specific case, we are deleting the row with largest id
//             assertTrue(id>newId);
//         }catch(SQLException e){
//             e.fillInStackTrace();
//         }
//     }

//     /**
//      * test if it can connect to the heroku database by the DATABASE_URL
//      * @throws SQLException
//      */
//     public void testConnection(){
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Connection conn = null;
//         try {
//             Class.forName("org.postgresql.Driver");
//             URI dbUri = new URI(db_url);
//             String username = dbUri.getUserInfo().split(":")[0];
//             String password = dbUri.getUserInfo().split(":")[1];
//             String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//             conn = DriverManager.getConnection(dbUrl, username, password);
//             if (conn == null) {
//                 fail("Error: DriverManager.getConnection() returned a null object");
//             }
//             conn.close();
//         } catch (SQLException e) {
//             fail("connect fail because the SQLException" + e.getMessage());
//         } catch (ClassNotFoundException cnfe) {
//             fail("Unable to find postgresql driver");
//         } catch (URISyntaxException s) {
//             fail("URI Syntax Error");
//         }
//     }
//     /**
//      * test program to add a comment
//      */
//     public void testInsertComment(){
//         //get connected to the database
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mCommentsTable=new CommentsTable(mDatabase.mConnection);
//         }catch(Exception e){
//             fail("Fail insert to Comments");
//         }
//         // If test fails, manually pick an idea to insert a comment into
//         assertFalse(mDatabase.mCommentsTable.insertComments(5, "content", "alg223@lehigh.edu")==0);
//         ResultSet rs;
//         int id=-1;
//         String content = "";
//         try{
//             Statement s=mDatabase.mConnection.createStatement();
//             //get the id of last_insert_row
//             String getID="SELECT max(com_id) AS id FROM comments;";
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                 id=rs.getInt("id");
//             }
//             //select from last_insert_row
//             String getInfo="SELECT * from comments where com_id = " + id;
//             rs=s.executeQuery(getInfo);
//             //pick the subject and message attributes from the last_insert_row
//             while(rs.next()){
//                 content = rs.getString("content");
//             }
//         }catch(SQLException e){
//             e.fillInStackTrace();
//         }
//         //check if the subject and message match
//         assertEquals("content", content);
//     }

//     /**
//      *  Test program for deleting a row in comments table
//      */
//     public void testDeleteComment(){
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mCommentsTable=new CommentsTable(mDatabase.mConnection);
//         }catch(Exception e){
//             fail("Fail delete");
//         }
//         ResultSet rs;
//         int com_id=-1;
//         int newId=-1;
//         try{
//             //get the id of last_insert_row, since the last insert row is inserted just now during testing, we will delete it
//             Statement s=mDatabase.mConnection.createStatement();
//             String getID="SELECT max(com_id) AS id FROM comments;";
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                 com_id=rs.getInt("com_id");
//             }
//             assertFalse(mDatabase.mCommentsTable.deleteComments(com_id)==-1);
//             rs=s.executeQuery(getID);
//             if(rs.next()){
//                 newId=rs.getInt("com_id");
//             }
//             //check if the max(id) after deleted is getting small, since for this specific case, we are deleting the row with largest id
//             assertTrue(com_id>newId);
//         }catch(SQLException e){
//             e.fillInStackTrace();
//         }
//     }

//     /**
//      *  Test program for liking and disliking an idea
//      */
//     public void testLikenDislike(){
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mLikesTable = new LikesTable(mDatabase.mConnection);
//             mDatabase.mDislikesTable = new DislikesTable(mDatabase.mConnection);
//         } catch(Exception e){
//             fail("Fail insert");
//         }
//         ResultSet rs;
//         int id = -1;
//         try{
//             // get the id of last_inserted_idea
//             Statement s = mDatabase.mConnection.createStatement();
//             String getID = "SELECT max(ID) AS id FROM ideas";
//             rs = s.executeQuery(getID);
//             if(rs.next()){
//                 id = rs.getInt("id");
//             }
//             assertFalse(mDatabase.mLikesTable.likeIdea(id, "alg223@lehigh.edu") == 0);
//             assertFalse(mDatabase.mDislikesTable.DislikeIdea(id, "alg223@lehigh.edu") == 0);
//         }catch (SQLException e){
//             e.fillInStackTrace();
//         }
//     }

//     /**
//      *  Test program for liking and disliking an idea
//      */
//     public void testRemoveLikenDislike(){
//         Map<String, String> env = System.getenv();
//         String db_url = env.get("DATABASE_URL");
//         Database mDatabase = Database.getDatabase(db_url);
//         assert(mDatabase != null);
//         try{
//             mDatabase.mLikesTable = new LikesTable(mDatabase.mConnection);
//             mDatabase.mDislikesTable = new DislikesTable(mDatabase.mConnection);
//         } catch(Exception e){
//             fail("Fail insert");
//         }
//         ResultSet rs;
//         int id = -1;
//         try{
//             // get the id of last_inserted_idea
//             Statement s = mDatabase.mConnection.createStatement();
//             String getID = "SELECT max(ID) AS id FROM ideas";
//             rs = s.executeQuery(getID);
//             if(rs.next()){
//                 id = rs.getInt("id");
//             }
//             assertFalse(mDatabase.mLikesTable.removeLike(id, "alg223@lehigh.edu") == 0);
//             assertFalse(mDatabase.mDislikesTable.removeDislike(id, "alg223@lehigh.edu") == 0);
//         }catch (SQLException e){
//             e.fillInStackTrace();
//         }
//     }
// }