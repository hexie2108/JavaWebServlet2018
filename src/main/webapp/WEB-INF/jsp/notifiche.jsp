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
    <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.min.js"></script>
</head>
<body>
<script>
    // request permission on page load
    document.addEventListener('DOMContentLoaded', function () {
        if (!Notification) {
            alert('Desktop notifications not available in your browser. Try Chromium.');
            return;
        }

        if (Notification.permission !== "granted")
            Notification.requestPermission();
    });

    function notifyMe() {
        if (Notification.permission !== "granted")
            Notification.requestPermission();
        else {
            const notification = new Notification('Notification title', {
                icon: 'http://cdn.sstatic.net/stackexchange/img/logos/so/so-icon.png',
                body: "Hey there! You've been notified!",
            });

            notification.onclick = function () {
                window.open("http://stackoverflow.com/a/13328397/1269037");
            };
        }
    }
</script>
<div id="idListNotifications" class="container-fluid">
    <c:forEach items="${notifications}" var="notification">
        <div class="notification row-fluid">
            <small class="float-right" title="<fmt:formatDate value="${notification.date}" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" timeZone="UTC"/>"><fmt:formatDate value="${notification.date}" type="both"/> UTC</small>
            <small> <%-- TODO: Sostituire con icona? --%>
                <c:if test="${notification.status}">Letto</c:if>
                <c:if test="${not notification.status}">Non letto</c:if>
            </small>
            <div>${notification.text}</div>
        </div>
    </c:forEach>
</div>
<script>
    $(document).ready(function() {
        const url = "${pageContext.servletContext.contextPath}/${PagePathsConstants.NOTIFICATIONS_JSON}";

        function createNotification(notification){

            const dateSm = jQuery('<small/>', {
                class: "float-right",
                title: notification.date,
                text: (new Date(notification.date)).toLocaleString() // La data è in UTC quindi la converto secondo la timezone locale
            });

            const textDiv = jQuery('<div/>', {
                text: notification.text
            });

            const lettoSm = jQuery('<small/>', {
                text: notification.status?"Letto":"Non letto"
            });

            return jQuery('<div/>', {
                class: ["notification", "row-fluid"].join(' '),
                html: [lettoSm, dateSm, textDiv]
            });
        }

        function updateList(){
            const notificationList = $('#idListNotifications');

            const request = $.ajax({
                dataType: "json",
                url : url,
                type: "get"
            });

            request.done(function(data){
                const notificationDivs = data.map((v) => createNotification(v));

                notificationList.empty();
                notificationList.append(notificationDivs);
            });
        }

        updateList();
        setTimeout(updateList, 5000); // Update notifications every 5 seconds
    })
</script>
</body>
</html>
