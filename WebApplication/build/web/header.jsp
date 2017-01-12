   <%--
	Document: aboutl.jsp
	Created On: Feb 4, 2016
	Authors: Deepak Rohan, Abhishek

 --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        
        <%-- title of the Page--%>
        <title>Researchers Exchange Participations</title>
        <%-- importing CSS stylesheet --%>
        <link rel="stylesheet" href="styles/main.css">
        <script type="text/javascript" src="js/jquery-1.12.0.min.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
        
        <!-- BootStrap -->
        
        <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" 
        integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous"> -->
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <%-- Code to specify Header section of the page--%>
        <div id="header">
            <nav id="header_menu">
                <ul  class="left" >
                    <li><a href="UserController?action=main" style="color: #009933;padding: 0;">Researchers Exchange Participations</a></li>
                </ul>
                <ul class="right">
                    <c:if test="${theUser == null && theAdmin==null}">
                        <li><a href="UserController?action=about">About Us</a></li>
                        <li><a href="UserController?action=how">How it Works</a></li>
                        <li><a href="UserController?action=main">Login</a></li>
                        </c:if>
                        <c:if test="${theUser !=null}">
                        <li><a href="UserController?action=about">About Us</a></li>
                        <li><a href="UserController?action=how">How it Works</a></li>
                         <li>Hello,&nbsp;<c:out value="${theUser.getName()}"></c:out></li>
                        <li><a href="UserController?action=logout">Logout</a></li>
                        </c:if>
                        <c:if test="${theAdmin !=null}">
                        <li><a href="UserController?action=about">About Us</a></li>
                        <li><a href="UserController?action=how">How it Works</a></li>
                        <li>Hello,&nbsp;<c:out value="${theAdmin.getName()}"></c:out></li>
                        <li><a href="UserController?action=logout">Logout</a></li>
                        </c:if>
                </ul>

            </nav>



        </div>

