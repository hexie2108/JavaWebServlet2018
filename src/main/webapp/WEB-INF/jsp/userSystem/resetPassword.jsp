<%-- la pagina di reset password --%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/userSystem/header.jsp"/>

<div class="form-box">
        <form id="form-register" method="post" onsubmit="return validateResetPassword()" action="${pageContext.request.contextPath}/service/resetPasswordService">

                <h2 class="form-title">
                        <fmt:message key="resetPassword.title"/>
                </h2>


                <div class="form-group">

                        <div id="divPassword" class="password-group">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword" class="form-control input-box" placeholder="<fmt:message key="user.label.password"/>" required=""
                                               type="password" name="${FormValidator.FIRST_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="<fmt:message key="suggestions"/>" data-content="<fmt:message key="suggestions.password"/>"
                                               value="">

                                </div>

                                <div class="progress progress-bar-div">
                                        <div class="progress-bar progress-bar-striped progress-bar-animated"></div>
                                </div>

                                <div class="error-messages error-password">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_TOO_LONG"/>
                                        </p>
                                        <p class="min-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_TOO_SHORT"/>
                                        </p>
                                        <p class="invalid">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.PASSWORD_NOT_VALID"/>
                                        </p>

                                </div>


                        </div>

                        <div id="divPassword2" class="password2-group mt-3">


                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword2" class="input-box form-control" placeholder="<fmt:message key="user.label.repeatPassword"/>" required=""
                                               type="password" name="${FormValidator.SECOND_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"
                                               value="">

                                </div>
                                <div class="error-messages error-password2">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>   <fmt:message key="validateUser.errors.PASSWORD2_MISSING"/>
                                        </p>
                                        <p class="no-equal">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.PASSWORD2_NOT_SAME"/>
                                        </p>

                                </div>
                        </div>

                </div>


                <%-- input hidden per memorizzare dati di email e resetLink--%>
                <input type="hidden" name="resetPwdLink" value="${param.resetPwdLink}" />
                <input type="hidden" name="${FormValidator.EMAIL_KEY}" value="${param[FormValidator.EMAIL_KEY]}" />

                <button class="btn btn-lg btn-primary btn-block" type="submit">
                        <fmt:message key="resetPassword.label.submit"/>
                </button>


        </form>
</div>

<div class="link-utils">
        <div class="content-top">
                <a class="" href="<c:url value="/login"/>">
                        <i class="fas fa-sign-in-alt"></i> <fmt:message key="login.label.login"/>
                </a>

        </div>
</div>

<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>

<jsp:include page="/WEB-INF/jsp/userSystem/footer.jsp"/>
