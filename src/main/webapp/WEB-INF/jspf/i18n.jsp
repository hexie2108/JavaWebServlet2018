<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- se c'è la cookie language,  usa cookie--%>
<c:if test="${not empty cookie.language.value }">
        <fmt:setLocale value="${cookie.language.value}"/>
</c:if>

<%-- se non c'è, usa language del browser cliente--%>
<c:if test="${empty cookie.language.value }">
        <fmt:setLocale value="${pageContext.request.locale.getLanguage()}"/>
</c:if>

<fmt:setBundle basename="text"/>
