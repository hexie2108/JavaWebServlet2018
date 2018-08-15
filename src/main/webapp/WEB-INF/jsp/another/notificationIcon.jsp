<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ConstantsUtils" %>

<html>
<head>
    <title><fmt:message key="notificationIcon.title"/></title>

    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>" type="text/css"
          media="all">
    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css"
          media="all">

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>" crossorigin="anonymous"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"
            crossorigin="anonymous"></script>
</head>
<body>
<c:if test="${not empty sessionScope.user}">
    <div class="dropdown">
        <a class="btn dropdown-toggle" data-toggle="dropdown" type="button" id="notificationsMenu"
           aria-haspopup="true" aria-expanded="false"
           href="<c:url value="/${ConstantsUtils.NOTIFICATIONS}"/>">
            <i class="far fa-bell"></i>
        </a>
        <div class="dropdown-menu pt-0 pb-0" aria-labelledby="notificationsMenu">
            <ul class="list-group list-group-flush" id="notificationsList">
            </ul>
            <div id="notificationsEmtpy" class="p-2"><fmt:message key="notificationIcon.label.empty"/></div>
        </div>
        <script src="<c:url value="/js/notifications.js"/>" crossorigin="anonymous"></script>
        <script>
            $(document).ready(function () {
                const URL = "<c:url value="/${ConstantsUtils.NOTIFICATIONS_JSON}"/>";

                const bell = $('#notificationsMenu > i');
                const list = $('#notificationsList');
                const notificationsEmpty = $('#notificationsEmtpy');

                function update() {
                    updateNotificationList(
                        list,
                        URL,
                        true,
                        false,
                        bell,
                        notificationsEmpty,
                        "<fmt:message key="notifications.text.read"/>",
                        "<fmt:message key="notifications.text.notRead"/>",
                        "<fmt:message key="notifications.label.markAsRead"/>",
                        "<c:url value="/markNotification.json"/>",
                        true,
                        "<fmt:message key="generic.errors.unknownError"/>"
                    );
                }

                update();
                setInterval(update, 5000);
            });
        </script>
    </div>
</c:if>
</body>
</html>
