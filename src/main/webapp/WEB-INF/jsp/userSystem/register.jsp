<%-- la pagina di jsp --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ConstantsUtils" %>
<%@ include file="/WEB-INF/jspf/i18n.jsp"%>


<jsp:include page="/WEB-INF/jsp/userSystem/header.jsp"/>

<div>
        <form id="form-register" name="form-register" method="post" enctype="multipart/form-data" autocomplete="off" onsubmit="return validateForm()">

                <h2 class="form-title">
                        <i class="fas fa-user-plus"></i> <fmt:message key="register.label.title"/>
                </h2>


                <div class="form-group row ">

                        <div id="divEmail" class="email-group col-sm-12">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-at"></i> 
                                                </span>
                                        </div>
                                        <input id="inputEmail" class="form-control input-box" placeholder="<fmt:message key="user.label.email"/>" required="" 
                                               type="email" name="${RegistrationValidator.EMAIL_KEY}"  autocomplete="off"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content=" la lunghezza deve essere limitata a ${RegistrationValidator.EMAIL_MAX_LEN}" >


                                </div>

                                <div class="error-messages error-email">
                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.EMAIL_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>
                                        </p>
                                        <p class="invalid">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>
                                        </p>
                                        <p class="repeat">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_ALREADY_USED"/>
                                        </p>

                                </div>
                        </div>

                </div>

                <div class="form-group row ">

                        <div class="first-name-group col-sm-6 ">

                                <div class="input-group ">

                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-user"></i>
                                                </span>
                                        </div>
                                        <input id="inputFirstName" class="form-control input-box" placeholder="<fmt:message key="user.label.name"/>" required="" 
                                               type="text" name="${RegistrationValidator.FIRST_NAME_KEY}" 
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="la lunghezza deve essere limitata a 44"
                                               >

                                </div>
                                <div class="error-messages error-first-name">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>   <fmt:message key="validateUser.errors.FIRST_NAME_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.FIRST_NAME_TOO_LONG"/>
                                        </p>

                                </div>

                        </div>

                        <div class="last-name-group col-sm-6 ">

                                <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-user"></i>
                                                </span>
                                        </div>

                                        <input id="inputLastName" class="form-control input-box" placeholder="<fmt:message key="user.label.surname"/>" required="" 
                                               type="text" name="${RegistrationValidator.LAST_NAME_KEY}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="la lunghezza deve essere limitata a 44"
                                               >

                                </div>

                                <div class="error-messages error-last-name">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.LAST_NAME_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.LAST_NAME_TOO_LONG"/>
                                        </p>

                                </div>

                        </div>
                </div>



                <div class="form-group row">

                        <div id="divPassword" class="password-group col-sm-6">
                                <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword" class="form-control input-box" placeholder="<fmt:message key="user.label.password"/>" required=""
                                               type="password" name="${RegistrationValidator.FIRST_PWD_KEY}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="deve essere:<br/>
                                               1. Lunga almeno 8 caratteri e al massimo 44 caratteri<br/>
                                               2. Avere almeno 1 lettera minuscola<br/>
                                               3. Avere almeno 1 lettera maiuscola<br/>
                                               4. Avere almeno 1 numero<br/>
                                               5. Avere almeno 1 simbolo<br/>"

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

                        <div id="divPassword2" class="password2-group col-sm-6">

                                <label for="inputPassword2" class="sr-only"><fmt:message key="user.label.repeatPassword"/></label>
                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword2" class="input-box form-control" placeholder="<fmt:message key="user.label.repeatPassword"/>" required=""
                                               type="password" name="${RegistrationValidator.SECOND_PWD_KEY}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="deve essere uguale a password appena inserito"
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

                <div class="avatar-group form-group">
                        <div class="">seleziona avatar:</div>
                        <div class="">
                                <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av" varStatus="status">

                                        <div class="avatar-box custom-control custom-radio custom-control-inline">
                                                <input type="radio" class="custom-control-input  img-radio default-avatar" id="avatar-${status.index}" name="${RegistrationValidator.AVATAR_KEY}" value="${av}" ${status.index==0?"checked":""} required="required" />
                                                <label class="custom-control-label" for="avatar-${status.index}">
                                                        <img class="img-input img-fluid" src="<c:url value="/${ConstantsUtils.IMAGE_BASE_PATH}/${ConstantsUtils.IMAGE_OF_USER}/${av}"/>" />
                                                        <span class="img-check">
                                                                <i class="far fa-check-circle "></i>
                                                        </span>
                                                </label>

                                        </div>

                                </c:forEach>

                                <div class="avatar-box custom-control custom-radio custom-control-inline">
                                        <input type="radio" class="custom-control-input  img-radio" id="avatar-custom" name="${RegistrationValidator.AVATAR_KEY}" value="custom"  required="required" />
                                        <label class="custom-control-label" for="avatar-custom">
                                                <img class="img-input img-fluid" src="<c:url value="/${ConstantsUtils.IMAGE_BASE_PATH}/base/custom-avatar.svg"/>" />
                                                <span class="img-check">
                                                        <i class="far fa-check-circle "></i>
                                                </span>
                                        </label>

                                </div>
                        </div>
                        <div class="error-messages error-avatar">

                                <p class="null">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_MISSING"/>
                                </p>
                                <p class="invalid">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_NOT_VALID"/>
                                </p>
                        </div>


                </div>

                <%--parte di immagine --%>
                <div class="form-group custom-avatar-uploader">

                        <div class=" custom-file input-group mb-3">

                                <input type="file" class="custom-file-input"  id="customAvatarImg" name="${RegistrationValidator.AVATAR_IMG_KEY}" accept="image/jpeg, image/png, image/gif, image/bmp">
                                <label class="custom-file-label input-box" for="customAvatarImg">seleziona file</label>
                                <%-- serve per ripristinare il segnaposto --%>
                                <label class="custom-file-label-origin d-none">seleziona file</label>
                        </div>

                        <%--parte di suggerimenti --%>
                        <div class="form-group">
                                <label>accetta solo file *.jpg, *.png, *.gif, *.bmp</label>
                        </div>

                        <div class="error-messages error-custom-avatar-img">

                                <p class="null">
                                        <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.AVATAR_IMG_MISSING"/>
                                </p>
                                <p class="max-length">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_IMG_TOO_BIG"/>
                                </p>
                                <p class="min-length">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_IMG_ZERO_DIM"/>
                                </p>
                                <p class="invalid">
                                        <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.AVATAR_IMG_NOT_IMG"/>
                                </p>

                        </div>


                </div>


                <%-- accetare privacy non è obbligatoria alla registrazione--%>
                <div class="form-group row-fluid">

                        <div id="divPrivacy">
                                <div class="input-group custom-control custom-checkbox">
                                        <input id="inputInfPrivacy" class="custom-control-input"  type="checkbox" name="${RegistrationValidator.INF_PRIVACY_KEY}" >
                                        <label class="form-check-label custom-control-label ml-1" for="inputInfPrivacy">
                                                <a href="javascript:;" data-toggle="modal" data-target="#boxShowPrivacy" >
                                                        <fmt:message key="register.label.privacyStatementCheckbox"/>
                                                </a>
                                        </label>

                                </div>

                        </div>

                </div>

                <div class="alert d-none" id="id-res">
                </div>

                <button class="btn btn-lg btn-primary btn-block" type="submit">
                        <fmt:message key="register.label.submit"/>
                </button>


        </form>

</div>
<div class="util-link">
        <a class="btn btn-info" href="<c:url value="/"/>"><i class="fas fa-home"></i> HOME</a>
        <a class="btn btn-info" href="<c:url value="/login"/>"><i class="fas fa-sign-in-alt"></i> LOGIN</a>
</div>               


<%-- finestra modale di privacy --%>   
<div class="modal fade" id="boxShowPrivacy">
        <div class="modal-dialog">
                <div class="modal-content">

                        <%-- box-head --%>
                        <div class="modal-header">

                                <h4 class="modal-title">
                                        <fmt:message key="register.label.privacyStatement"/>
                                </h4>

                                <button type="button" class="close" data-dismiss="modal">&times;</button>

                        </div>
                        <%-- box-body --%>
                        <div class="modal-body">
                                <div id="privacyPolicy">
                                        <fmt:message key="register.text.privacyStatement"/>
                                </div>
                        </div>

                        <%-- box-footer  --%>
                        <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                        </div>

                </div>
        </div>
</div>       

<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script src="<c:url value="/js/userValidate.js?language=${pageContext.request.locale.getLanguage()}"/>"></script>


<jsp:include page="/WEB-INF/jsp/userSystem/footer.jsp"/>
