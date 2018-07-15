<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.LogoutServlet" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
Sei già loggato cosa vuoi fare?
<a href="${pageContext.servletContext.contextPath}/${PagePathsConstants.LOGOUT}?${LogoutServlet.NEXT_URL_KEY}=${requestScope.nextUrl}">Logout</a><br>
<a href="${requestScope.prevUrl}">Go back</a>
</body>
</html>
