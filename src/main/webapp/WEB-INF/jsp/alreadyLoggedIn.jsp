<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
        <a href="${pageContext.request.contextPath}/logout?nextUrl=${requestScope.nextUrl}">Logout</a><br>
        <a href="${requestScope.prevUrl}">Go back</a>
    </body>
</html>
