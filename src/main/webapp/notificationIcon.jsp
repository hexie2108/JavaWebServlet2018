<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"  type="text/css" media="all">
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/fontawesome-free-5.1.1-web/css/all.min.css" type="text/css" media="all">

    <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
    <script src="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</head>
<body>
<c:if test="${not empty sessionScope.user}">
<div class="dropdown">
    <a class="btn dropdown-toggle" data-toggle="dropdown" type="button" id="notificationsMenu"
       aria-haspopup="true" aria-expanded="false"
       href="${pageContext.servletContext.contextPath}/${PagePathsConstants.NOTIFICATIONS}">
        <i class="far fa-bell"></i>
    </a>
    <div class="dropdown-menu pt-0 pb-0" aria-labelledby="notificationsMenu">
        <ul class="list-group list-group-flush" id="notificationsList">
        </ul>
        <div id="notificationsEmtpy" class="p-2">Non hai alcuna notifica</div>
    </div>
    <script src="${pageContext.servletContext.contextPath}/js/notifications.js" crossorigin="anonymous"></script>
    <script>
        $(document).ready(function() {
            const URL = "${pageContext.servletContext.contextPath}/${PagePathsConstants.NOTIFICATIONS_JSON}";
            console.log(URL);

            const bell = $('#notificationsMenu > i');
            const list = $('#notificationsList');
            const notificationsEmpty = $('#notificationsEmtpy');

            const update = function(){updateNotificationList(list, URL, true, false, bell, notificationsEmpty);};

            update();
            setInterval(update,5000);
        });
    </script>
</div>
</c:if>
</body>
</html>