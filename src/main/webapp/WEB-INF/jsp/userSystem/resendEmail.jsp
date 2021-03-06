<%-- la pagina form per resend email --%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator" %>

<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/userSystem/header.jsp"/>



<div class="form-box">

        <form id="form-forgot" method="POST" onsubmit="return validateResendEmail()" action="${pageContext.request.contextPath}/service/resendEmailService">

                <h2 class="form-title">
                        <fmt:message key="resend email"/>
                </h2>


                <div class="form-group error-messages">
                        <p class="email-null">
                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.EMAIL_MISSING"/>
                        </p>
                        <p class="email-max-length">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>
                        </p>
                        <p class="invalid-format-email">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>
                        </p>
                        <p class="no-existence">
                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.EMAIL_NOT_EXISTING"/>
                        </p>
                        <p class="already-activated">
                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="the specified account has already been activated"/>
                        </p>
                        

                </div>

                <div class="form-group">

                        <div id="divEmail" class="email-group">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-at"></i> 
                                                </span>
                                        </div>
                                        <input id="inputEmail" class="form-control input-box" placeholder="<fmt:message key="user.label.email"/>" required="" 
                                               type="email" name="${FormValidator.EMAIL_KEY}"  placeholder="<fmt:message key="user.label.email"/>" maxlength="${FormValidator.EMAIL_MAX_LEN}">


                                </div>

                        </div>

                </div>


                <button class="btn btn-lg btn-primary btn-block" type="submit">
                        <fmt:message key="send"/>
                </button>

        </form>

</div>

<div class="link-utils">     
        <div class="content-top">
                <a class="" href="<c:url value="/register"/>">
                        <i class="fas fa-user-plus"></i> <fmt:message key="login.label.register"/>
                </a>

        </div>
</div>

<jsp:include page="/WEB-INF/jsp/userSystem/footer.jsp"/>