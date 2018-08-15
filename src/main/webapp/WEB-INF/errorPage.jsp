<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="jspf/i18n.jsp" %>
<html>
<head>
    <title><fmt:message key="errorPage.title"/></title>
</head>
<body>
${exception}
<c:choose>
    <c:when test="${not empty pageContext.exception}">
        <div>
            <h2><fmt:message key="errorPage.label.error"/> 500</h2>
            <p>[${pageContext.exception.getClass()}]:${pageContext.exception.message}</p>
        </div>
    </c:when>
    <c:when test="${not empty requestScope['javax.servlet.error.message'] or not empty requestScope['javax.servlet.error.status_code']}">
        <div>
            <h2><fmt:message key="errorPage.label.error"/> ${requestScope['javax.servlet.error.status_code']}</h2>
            <c:forEach var="m"
                       items="${fn:split(fn:substringAfter(fn:substringBefore(requestScope['javax.servlet.error.message'],']'),'['), ',')}">
                <p>[${m}]: <fmt:message key="${m}"/></p>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <div>
            <h2><fmt:message key="generic.errors.unknownError"/></h2>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
