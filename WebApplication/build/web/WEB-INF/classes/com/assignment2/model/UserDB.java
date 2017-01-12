/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment2.model;

import com.assignment2.beans.User;
import com.assignment2.util.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author varanasiavinash
 */
public class UserDB {
    
    private String path;
    
    public UserDB(String path){
        this.path=path+"/UserDB.xml";
    }
    
    public UserDB(){
        this.path="";
     }
    public User Validateuser (User user,String password){
    
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user1=null;
        String query = "SELECT * from User where Username = ? and Password = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.setString(2, password);
            rs = ps.executeQuery();
            while (rs.next()) {
                user1=new User();
                user1.setName(rs.getString("Name"));
                user1.setEmail(rs.getString("Username"));
                user1.setPassword(rs.getString("Password"));
                user1.setType(rs.getString("Type"));
                user1.setParticipants(rs.getInt("Participation"));
                user1.setPostedstudies(rs.getInt("Studies"));
                user1.setCoins(rs.getInt("Coins"));               
            }
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return user1;
           
        
        
        
    }
     public User getUser (String email){
    
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;
        String query = "SELECT * from User where Username = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                user=new User();
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setType(rs.getString("Type"));
                user.setParticipants(rs.getInt("Participation"));
                user.setPostedstudies(rs.getInt("Studies"));
                user.setCoins(rs.getInt("Coins"));               
            }
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return user;
           
    }
      public int addUser(User user) throws IOException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        int count=-1;

        String query = "INSERT INTO User "
                + "(Username, Password, Type, Studies, Participation, Coins, Name, SALT) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getType());
            ps.setInt(4, user.getPostedstudies());
            ps.setInt(5, user.getParticipants());
            ps.setInt(6, user.getCoins());
            ps.setString(7, user.getName());
            ps.setString(8,user.getSalt());
            count=ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e);
            count=-1;
            
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        } 
      return count;
      }
     
     
   /*  public void updateUser(String email,User user) throws IOException {
        List<User> list = null;
        UserList ul = null;
        try {

            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            JAXBContext jaxbContext = JAXBContext.newInstance(UserList.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ul = (UserList) jaxbUnmarshaller.unmarshal(file);
            //System.out.println(study);
            List lst = new ArrayList();
            for (User u : ul.getUser()) {
                if (!u.getEmail().equals(email)){
                    lst.add(u);    
            }
            }

            lst.add(user);

            ul = new UserList();
            ul.setUser(lst);

            Marshaller jaxbMarshaller = null;
            try {
                jaxbContext = JAXBContext.newInstance(UserList.class);
                jaxbMarshaller = jaxbContext.createMarshaller();

                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.marshal(ul, file);
                jaxbMarshaller.marshal(ul, System.out);
            } catch (JAXBException ex) {
               // Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }*/

    public int addTempUser(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        int count=-1;

        String query = "INSERT INTO TempUser "
                + "(UName, Email, Password, IssueDate, Token) "
                + "VALUES (?, ?, ?, NOW(), ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getToken());
            count=ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e);
            count=-1;
            
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }    
     return count;
    }

    public User getTempUser(String Token) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;
        String query = "SELECT * from TempUser where Token = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, Token);
            rs = ps.executeQuery();
            while (rs.next()) {
                user=new User();
                user.setName(rs.getString("UName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setToken(rs.getString("Token"));
                           
            }
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return user;
    
    }

    public void deleteTempUser(String email) {
      ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        int count=-1;

        String query = "DELETE FROM TempUser where Email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            
            count=ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e);
            count=-1;
            
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }    
    // return count;  
    }

    public User Checkuser(String email) {
        //ArrayList<String> list= new ArrayList<String>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;
        
        String query = "SELECT * from User where Username = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            //ps.setString(2, password);
            rs = ps.executeQuery();
            while (rs.next()) {
                
                 user=new User();
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setType(rs.getString("Type"));
                user.setParticipants(rs.getInt("Participation"));
                user.setPostedstudies(rs.getInt("Studies"));
                user.setCoins(rs.getInt("Coins"));
                user.setSalt(rs.getString("SALT"));
                               
            }
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return user;
    }

    public int addForgotPasswordUser(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        int count=-1;

        String query = "INSERT INTO ForgotPwdUser "
                + "(Email, Token) "
                + "VALUES (?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getToken() );
            
            count=ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e);
            count=-1;
            
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }    
     return count;
    }

    public User getForgotUser(String Email, String Token) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user=null;
        String query = "SELECT * from ForgotPwdUser where Email = ? and Token = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, Email);
            ps.setString(2, Token);
            rs = ps.executeQuery();
            while (rs.next()) {
                user=new User();
               
                user.setEmail(rs.getString("Email"));
                
                user.setToken(rs.getString("Token"));
                           
            }
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return user;
    }

    public int updateUser(User actualUser) {
       ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        int count=-1;

        String query = "Update User set Password=?, SALT=? where Username=?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, actualUser.getPassword());
            ps.setString(2, actualUser.getSalt());
            ps.setString(3, actualUser.getEmail());
            
            count=ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e);
            count=-1;
            
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }    
     return count;
    }
}
