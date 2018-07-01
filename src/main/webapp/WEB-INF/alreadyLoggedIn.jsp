<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
Sei già loggato cosa vuoi fare?
<a href="${pageContext.request.contextPath}/logout?nextUrl=${requestScope.nextUrl}">Logout</a><br>
<a href="${requestScope.prevUrl}">Go back</a>

</body>
</html>
