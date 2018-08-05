<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<%@include file="../jspf/i18n.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="notifications.title"/></title>

    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>" crossorigin="anonymous"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>" crossorigin="anonymous"></script>
</head>
<body>
<%@include file="../jspf/i18n_switcher.jsp"%>
<button id="reloadBtn"><fmt:message key="notifications.label.refreshNotifications"/></button>
<ul id="notificationsList" class="list-group">
    <c:forEach items="${notifications}" var="notification">
        <li class="list-group-item">
            <div class="d-flex w-100 justify-content-between">
                <small> <%-- TODO: Sostituire con icona? --%>
                    <c:if test="${notification.status}"><fmt:message key="notifications.label.read"/></c:if>
                    <c:if test="${not notification.status}"><fmt:message key="notifications.label.unread"/></c:if>
                </small>
                <small class="float-right" title="<fmt:formatDate value="${notification.date}" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" timeZone="UTC"/>"> <fmt:formatDate value="${notification.date}" type="both"/> UTC</small>
            </div>
            <div>${notification.text}</div>
        </li>
    </c:forEach>
</ul>
<div id="notificationsEmtpy" class="p-2 ${not notifications.isEmpty()?'d-none':''}"><fmt:message key="notifications.label.noNotifications"/></div>
<script src="<c:url value="/js/notifications.js"/>" crossorigin="anonymous"></script>
<script>
    $(document).ready(function() {
        const URL = "<c:url value="/${PagePathsConstants.NOTIFICATIONS_JSON}"/>";

        const notificationList = $('#notificationsList');
        const emptyMessage = $('#notificationsEmtpy');
        const reloadBtn = $('#reloadBtn');

        reloadBtn.click(() => {
            reloadBtn.prop("disabled", true);
            updateNotificationList(
                notificationList,
                URL,
                false,
                null,
                null,
                emptyMessage,
                "<fmt:message key="notifications.text.read"/>",
                "<fmt:message key="notifications.text.notRead"/>",
                "<fmt:message key="notifications.label.markAsRead"/>",
                "<c:url value="/markNotification.json"/>",
                false,
                "<fmt:message key="generic.errors.unknownError"/>"
            );
            reloadBtn.prop("disabled", false);
        });

        reloadBtn.trigger('click');
    })
</script>
</body>
</html>
