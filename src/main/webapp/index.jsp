<%-- 
    Document   : index
    Created on : 2018-7-14, 16:12:00
    Author     : mikuc
--%>

<%@page import="it.unitn.webprogramming18.dellmm.db.daos.ProductDAO"%>
<%@page import="it.unitn.webprogramming18.dellmm.javaBeans.Product"%>

<%@page import="it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <link rel="stylesheet" href="${pageContext.request.contextPath}/libs/fontAwesome/all.css" type="text/css" media="all">
        
    </head>
    <body>
        <h1>Hello sono index !</h1>
       
    </body>
</html>
