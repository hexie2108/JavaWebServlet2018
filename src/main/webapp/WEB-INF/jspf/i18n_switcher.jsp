<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.i18n" %>
<%@ include file="/WEB-INF/jspf/i18n.jsp"%>

<%-- Bootstrap bundle needed --%>
<div class="language-switch">


                <div class="dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-language"></i> <span><fmt:message key="i18n_switcher.label"/></span>
                        </a>

                        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                                <c:forEach items="${i18n.SUPPORTED_LANGUAGES}" var="entry">
                                        <a class="dropdown-item" href="javascript:;" onclick="changeLanguage('${entry.key}')">${entry.value}</a>
                                </c:forEach>
                        </div>
                </div>


</div>

