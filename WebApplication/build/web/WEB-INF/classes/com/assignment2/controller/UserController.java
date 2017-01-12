   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment2.controller;

import com.assignment2.beans.Mail;
import com.assignment2.beans.User;
import com.assignment2.model.EmailDB;
import com.assignment2.model.UserDB;
import com.assignment2.util.CommonUtil;
import com.assignment2.util.PasswordUtil;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author varanasiavinash
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {
    Cookie hostcookie;
    Cookie portcookie;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
        
        
        
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String str = String.valueOf(request.getServerPort());
        hostcookie = new Cookie("Host",request.getServerName());
        portcookie = new Cookie("Port",str);
        
        response.addCookie(hostcookie);
        response.addCookie(portcookie);
        
        
        String action = request.getParameter("action");
        //String path=System.getenv("OPENSHIFT_DATA_DIR");
        //String path = getServletContext().getRealPath("/WEB-INF");
        String url = "/home.jsp";
        HttpSession session = request.getSession();
        if (action != null && action.equals("")){
        url = "/home.jsp";    
        }
        else if (action.equals("login")) {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String HashAndSaltPassword="";
        User user=new User();
        user.setEmail(email);
            UserDB userDB=new UserDB();
            user=userDB.Checkuser(email);
            try {
                HashAndSaltPassword=PasswordUtil.hashAndSaltPassword(password, user.getSalt());
                
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
           // user=userDB.Validateuser(user,password);
            if(HashAndSaltPassword.equals(user.getPassword())){ 
                String userType=user.getType();
                if(userType.equals("Participant")){
                    session.setAttribute("theUser", user);
                    session.setAttribute("coins", user.getCoins());
                    session.setAttribute("NumParticipation", user.getParticipants());
                    url="/main.jsp";
                }
                if(userType.equals("Admin")){
                    session.setAttribute("theAdmin", user);
                    session.setAttribute("coins", user.getCoins());
                    session.setAttribute("NumParticipation", user.getParticipants());
                    url="/admin.jsp";
                }
            }else{
                request.setAttribute("msg","Invalid User Name/Password");
                url="/login.jsp";
            }
    }
        else if(action.equals("create")) {
            System.out.println("message is ");
            String Name=CommonUtil.checkNull(request.getParameter("name"));
            String Email=CommonUtil.checkNull(request.getParameter("email"));
            String password=CommonUtil.checkNull(request.getParameter("password"));
            String confirmPassword=CommonUtil.checkNull(request.getParameter("confirm_password"));
            if(!Email.equals("") && password.equals(confirmPassword)) {
                User user=new User();
                user.setName(Name);
                user.setEmail(Email);
                //user.setType("Participant");
                user.setPassword(password);
                /*user.setCoins(0);
                user.setParticipants(0);
                user.setPostedstudies(0);*/
                String token=UUID.randomUUID().toString();
                user.setToken(token);
                UserDB userDB=new UserDB();
                User checkUser=userDB.getUser(Email);
                if(checkUser == null){
                    int count=userDB.addTempUser(user);
                    
                    if(count==1){
                            String message= request.getRequestURL()+ "?action=activate&token="+token;
                            System.out.println("message is " +message);
                            String subject = "Research Exchange Participations - Activation Link For Login";
                            Mail mail =  new Mail();
                            mail.setReceiverEmail(Email);
                            mail.setReceiverName(Name);
                            mail.setMessage(message);
                            mail.setSubject(subject);
                            mail.setMailType("activation");
                            boolean isMessageSent = EmailDB.sendMessage(mail);
                            if(isMessageSent){
                                request.setAttribute("msg", "Please check your mail for activation link");
                                url = "/login.jsp";
                            }  else{
                            request.setAttribute("msg", "Error in sending mail");
                            url="/signup.jsp";
                            }
                        
                     
                     
                    }else
                    {
                    request.setAttribute("msg", "Error in Saving");
                    }
                  
                    /*session.setAttribute("theUser", user);
                    session.setAttribute("coins", user.getCoins());
                    session.setAttribute("NumParticipation", user.getParticipants());*/
                     
                }
                else{
                    request.setAttribute("name",Name);
                    request.setAttribute("email",Email);
                    request.setAttribute("password",password);
                    request.setAttribute("confirmPassword",confirmPassword);
                    if(!Name.equals("") || !Email.equals("") || !password.equals("") || !confirmPassword.equals("") ){
                        request.setAttribute("msg","User already Registered");
                    }
                    url="/signup.jsp";
                }
            } 
            else{
                request.setAttribute("name",Name);
                request.setAttribute("email",Email);
                request.setAttribute("password",password);
                request.setAttribute("confirmPassword",confirmPassword);
                if(!Name.equals("") || !Email.equals("") || !password.equals("") || !confirmPassword.equals("") ){
                    request.setAttribute("msg","Please fill all the details correctly/password and confirm password are not same");
                }
                url="/signup.jsp";
            }
        }
else if(action.equals("activate")){
            
          String Token=CommonUtil.checkNull(request.getParameter("token"));
                UserDB userDB=new UserDB();
                User checkTempUser=userDB.getTempUser(Token);
                
                
                String SaltValue=PasswordUtil.getSalt();
            try {
                String HashAndSaltPassword=PasswordUtil.hashAndSaltPassword(checkTempUser.getPassword(), SaltValue);
                checkTempUser.setPassword(HashAndSaltPassword);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                checkTempUser.setSalt(SaltValue);
                
                checkTempUser.setType("Participant");
               
                checkTempUser.setCoins(0);
                checkTempUser.setParticipants(0);
                checkTempUser.setPostedstudies(0);
                int count=userDB.addUser(checkTempUser);
                if(count==1){
                userDB.deleteTempUser(checkTempUser.getEmail());
                request.setAttribute("msg", "Your account has been activated.Please login with your credentials");
                url="/login.jsp";
                }else{
                request.setAttribute("msg", "Error in saving data");
                url="/login.jsp";
                }
        
        }
else if(action.equals("forgotpassword")) {     
            String Email=CommonUtil.checkNull(request.getParameter("email"));
            UserDB userDB=new UserDB();
            User checkUser=userDB.getUser(Email);
           if(checkUser != null) {
               String token=UUID.randomUUID().toString();
                User user=new User();
                user.setToken(token);
                user.setEmail(Email);
                int count=userDB.addForgotPasswordUser(user);
                    if(count==1) {
                            String message= request.getRequestURL()+ "?action=resetpassword&token="+token+"&email="+Email;
                            System.out.println("message is " +message);
                            String subject = "Research Exchange Participations - Reset Password Link";
                            Mail mail =  new Mail();
                            mail.setReceiverEmail(Email);
                            mail.setReceiverName(checkUser.getName());
                            mail.setMessage(message);
                            mail.setSubject(subject);
                            mail.setMailType("ResetPassword");
                            boolean isMessageSent = EmailDB.sendMessage(mail);
                            if(isMessageSent){
                                request.setAttribute("msg", "Please check your mail for forgot password link");
                                url = "/login.jsp";
                            }  else{
                            request.setAttribute("msg", "Error in sending mail");
                            url="/login.jsp";
                            }             
                    }else
                    {
                    request.setAttribute("msg", "Error in Saving");
                    }
                }
                else{
                    
                    url="/login.jsp";
                }
            }
            
 else if(action.equals("resetpassword")) {
     String Email=CommonUtil.checkNull(request.getParameter("email"));
     String Token=CommonUtil.checkNull(request.getParameter("token"));
     UserDB userDB=new UserDB();
            User checkForgotUser=userDB.getForgotUser(Email,Token);
     if (checkForgotUser != null) {
                User actualUser = userDB.getUser(checkForgotUser.getEmail());
                request.setAttribute("user", actualUser);
                url="/changepassword.jsp";
            }else{
         request.setAttribute("msg", "User doesn't exist");
                url="/login.jsp";
            }
     
 }   
 else if(action.equals("changepassword")) {
     String Email=CommonUtil.checkNull(request.getParameter("email"));
            String password=CommonUtil.checkNull(request.getParameter("password"));
            String confirmPassword=CommonUtil.checkNull(request.getParameter("confirm_password"));
            UserDB userDB=new UserDB(); 
            
     if (!Email.equals("") && password.equals(confirmPassword)) {
                User actualUser = userDB.getUser(Email);
                String SaltValue=PasswordUtil.getSalt();
            try {
                String HashAndSaltPassword=PasswordUtil.hashAndSaltPassword(password, SaltValue);
                actualUser.setPassword(HashAndSaltPassword);
                actualUser.setSalt(SaltValue);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
            int count=userDB.updateUser(actualUser);
            if(count==1) {
            request.setAttribute("msg", "Your password is updated.Kindly login with your new credentials");
                url="/login.jsp";
            }
            else{
            request.setAttribute("msg", "Error in updating details");
                url="/changepassword.jsp";
            }
            }else{
         request.setAttribute("msg", "Password and Confirm Password are not same");
                url="/changepassword.jsp";
            }
     
 }       
  
 
        
      /*  else if(action.equals("create")) {
            String Name=CommonUtil.checkNull(request.getParameter("name"));
            String Email=CommonUtil.checkNull(request.getParameter("email"));
            String password=CommonUtil.checkNull(request.getParameter("password"));
            String confirmPassword=CommonUtil.checkNull(request.getParameter("confirm_password"));
            if(!Email.equals("") && password.equals(confirmPassword)){
                User user=new User();
                user.setName(Name);
                user.setEmail(Email);
                user.setType("Participant");
                user.setPassword(password);
                user.setCoins(0);
                user.setParticipants(0);
                user.setPostedstudies(0);
                UserDB userDB=new UserDB();
                User checkUser=userDB.getUser(Email);
                if(checkUser == null){
                    userDB.addUser(user);
                    session.setAttribute("theUser", user);
                    session.setAttribute("coins", user.getCoins());
                    session.setAttribute("NumParticipation", user.getParticipants());
                    url="/main.jsp";  
                }
                else{
                    request.setAttribute("name",Name);
                    request.setAttribute("email",Email);
                    request.setAttribute("password",password);
                    request.setAttribute("confirmPassword",confirmPassword);
                    if(!Name.equals("") || !Email.equals("") || !password.equals("") || !confirmPassword.equals("") ){
                        request.setAttribute("msg","User already Registered");
                    }
                    url="/signup.jsp";
                }
            }
            else{
                request.setAttribute("name",Name);
                request.setAttribute("email",Email);
                request.setAttribute("password",password);
                request.setAttribute("confirmPassword",confirmPassword);
                if(!Name.equals("") || !Email.equals("") || !password.equals("") || !confirmPassword.equals("") ){
                    request.setAttribute("msg","Please fill all the details correctly");
                }
                url="/signup.jsp";
            }
        }*/
        else if(action.equals("how")){
            if(session.getAttribute("theUser")!=null){
                url="/main.jsp";
            }else if(session.getAttribute("theAdmin")!=null){
                url="/admin.jsp";
            }else{
                url="/how.jsp";
            }
        }else if(action.equals("about")){
            if(session.getAttribute("theUser")!=null || session.getAttribute("theAdmin")!=null){
                url="/aboutl.jsp";
            }else{
                url="/about.jsp";
            }
        }else if(action.equals("home")){
            if(session.getAttribute("theUser")!=null){
                url="/main.jsp";
            }else if(session.getAttribute("theAdmin")!=null){
                url="/admin.jsp";
            }
            else{
                url="/home.jsp";
            }
        }else if(action.equals("main")){
            if(session.getAttribute("theUser")!=null){
                url="/main.jsp";
            }else if(session.getAttribute("theAdmin")!=null){
                url="/admin.jsp";
            }else{
                url="/login.jsp";
            }
        }else if(action.equals("logout")) {
            if(session.getAttribute("theUser")!=null || session.getAttribute("theAdmin")!=null)
                session.invalidate();
            url="/home.jsp";
        } 
        else if(action.equals("recommend")) {
                            String toName = request.getParameter("study_name");
                            String toMail = request.getParameter("friend_email");
                            String message = request.getParameter("message");
                            String subject = "Research Exchange Participations ";
                            Mail mail =  new Mail();
                            mail.setReceiverEmail(toMail);
                            mail.setReceiverName(toName);
                            mail.setMessage(message);
                            mail.setSubject(subject);
                            mail.setMailType("recommend");
                            boolean count = EmailDB.sendMessage(mail);
                            if(count){
                                url = "/confirmr.jsp";
                            }
                            
                        }
        
         else if(action.equals("contact")) {
                            String toName = request.getParameter("study_name");
                            String toMail = request.getParameter("email");
                            String message = request.getParameter("message");
                            String subject = "Research Exchange Participations";
                            Mail mail =  new Mail();
                            mail.setReceiverEmail(toMail);
                            mail.setReceiverName(toName);
                            mail.setMessage(message);
                            mail.setSubject(subject);
                            mail.setMailType("contact");
                            boolean count = EmailDB.sendMessage(mail);
                            if(count){
                                url = "/confirmc.jsp";
                            }
                            
                        }
        
        else{
            url="/home.jsp";   
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);    
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    
// </editor-fold>
    

}
    
