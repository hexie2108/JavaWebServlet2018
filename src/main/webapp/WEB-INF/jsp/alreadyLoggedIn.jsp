<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.LogoutServlet" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<html>
    <head>
        <title>Homepage</title>
    </head>
    <body>
        <div class="py-3">
            <span class="px-2">
                <button class="btn btn-outline-primary" type="button" href="#">Home</button>
                <button class="btn btn-outline-primary" type="button" href="#">Inbox</button>
                <button class="btn btn-outline-primary" type="button" href="#">Profile</button>
                <form action="">
                    <button class="btn btn-outline-primary" type="submit">Logout</button>
                </form>
            </span>
            <div>
                <form action="PrepareAddUpdateListPageServlet" method="get">
                    <button class="btn btn-primary" type="submit">Add List</button>
                </form>
            </div>
            <div>

            </div>
        </div>
        ${sessionScope['user'].name}<br>
        ${sessionScope['user'].surname}<br>
        <a href="<c:url value="/${PagePathsConstants.LOGOUT}?${LogoutServlet.NEXT_URL_KEY}=${param.nextUrl}"/>">Logout</a><br>
        <a href="${requestScope.prevUrl}">Go back</a>
    </body>
</html>
