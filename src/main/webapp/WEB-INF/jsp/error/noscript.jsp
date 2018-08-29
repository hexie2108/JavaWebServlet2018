<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"></jsp:include>


<div class="error-main-section col-12">
    <div class="content">
            <h2 class="text-center mt-4">
                    <fmt:message key="generic.errors.noScript"/>
                    
            </h2>
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/front/footer.jsp"></jsp:include>

