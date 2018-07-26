<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Notifiche</title>

    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css">
    <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
    <script src="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.min.js" crossorigin="anonymous"></script>
</head>
<body>
<button id="reloadBtn"> Reload notifications </button>
<ul id="notificationsList" class="list-group">
    <c:forEach items="${notifications}" var="notification">
        <li class="list-group-item">
            <div class="d-flex w-100 justify-content-between">
                <small> <%-- TODO: Sostituire con icona? --%>
                    <c:if test="${notification.status}">Letto</c:if>
                    <c:if test="${not notification.status}">Non letto</c:if>
                </small>
                <small class="float-right" title="<fmt:formatDate value="${notification.date}" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" timeZone="UTC"/>">   <fmt:formatDate value="${notification.date}" type="both"/> UTC</small>
            </div>
            <div>${notification.text}</div>
        </li>
    </c:forEach>
</ul>
<div id="notificationsEmtpy" class="p-2" <c:if test="${not notifications.isEmpty()}">style="display: none"</c:if>">Non hai alcuna notifica</div>
<script src="${pageContext.servletContext.contextPath}/js/notifications.js" crossorigin="anonymous"></script>
<script>
    $(document).ready(function() {
        const URL = "${pageContext.servletContext.contextPath}/${PagePathsConstants.NOTIFICATIONS_JSON}";

        const notificationList = $('#notificationsList');
        const emptyMessage = $('#notificationsEmtpy');
        const reloadBtn = $('#reloadBtn');

        reloadBtn.click(() => {
            reloadBtn.prop("disabled", true);
            updateNotificationList(notificationList, URL, false, null, null, emptyMessage);
            reloadBtn.prop("disabled", false);
        });

        reloadBtn.trigger('click');
    })
</script>
</body>
</html>
