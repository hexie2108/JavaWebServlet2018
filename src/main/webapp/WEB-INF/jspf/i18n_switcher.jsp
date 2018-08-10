<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.i18n" %>

<%-- Bootstrap bundle needed --%>
<form action="">
    <div class="dropdown show">
        <a class="btn dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <fmt:message key="i18n_switcher.label"/>
        </a>

        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
            <c:forEach items="${i18n.SUPPORTED_LANGUAGES}" var="entry">
                <c:url var="languageUrl" value="">
                    <c:forEach items="${param}" var="entryParam">
                        <c:if test="${entryParam.key != 'language'}">
                            <c:param name="${entryParam.key}" value="${entryParam.value}" />
                        </c:if>
                    </c:forEach>
                    <c:param name="language" value="${entry.key}" />
                </c:url>
                <a class="dropdown-item" href="${languageUrl}">${entry.value}</a>
            </c:forEach>
        </div>
    </div>
</form>

