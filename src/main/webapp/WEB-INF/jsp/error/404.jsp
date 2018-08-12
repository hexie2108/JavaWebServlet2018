<%-- 
questa pagina non è ancora utilizzato
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"></jsp:include>


<div class="error-main-section col-12">
    <div class="content">
        <p>Error: HTTP 404 Not Found</p>
        <p>URI: ${pageContext.errorData.requestURI}</p>

        <p>Status code: ${pageContext.errorData.statusCode}</p>
        <p>Stack trace: ${pageContext.exception.stackTrace}</p>
        <p></p>
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/front/footer.jsp"></jsp:include>

