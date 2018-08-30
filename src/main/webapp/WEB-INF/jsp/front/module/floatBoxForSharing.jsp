<%-- 
    la finestra di condivisione
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>


<div class="modal fade" id="boxSharing">
        <div class="modal-dialog">
                <div class="modal-content">

                        <%-- box-head --%>
                        <div class="modal-header">
                                <h4 class="modal-title">
                                        <i class="fas fa-share-alt"></i> <fmt:message key="shareTheList" />
                                </h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <%-- box-body --%>
                        <div class="modal-body">

                                <div class="sharing-body">

                                        <%--form per creare una nuova condivisione --%>
                                        <form class="form-inline" action="<c:url value="/service/sharingService"/>"
                                              method="GET" onsubmit="return validateSharing()">

                                                <%-- 4 checkbox di permessi --%>
                                                <div class="custom-control custom-control-inline custom-checkbox  justify-content-start">
                                                        <input type="checkbox" class="custom-control-input" id="modifyList" name="modifyList"
                                                               value="1"/>
                                                        <label class="custom-control-label" title="<fmt:message key="editTheList" />" for="modifyList">
                                                                <i class="fas fa-edit"></i> <fmt:message key="editTheList" />
                                                        </label>
                                                </div>

                                                <div class="custom-control custom-control-inline custom-checkbox  justify-content-start">
                                                        <input type="checkbox" class="custom-control-input" id="addObject" name="addObject"
                                                               value="1"/>
                                                        <label class="custom-control-label" title="<fmt:message key="addTheProduct" />" for="addObject">
                                                                <i class="fas fa-cart-plus"></i> <fmt:message key="addTheProduct" />
                                                        </label>
                                                </div>

                                                <div class="custom-control custom-control-inline custom-checkbox  justify-content-start">
                                                        <input type="checkbox" class="custom-control-input" id="deleteList" name="deleteList"
                                                               value="1"/>
                                                        <label class="custom-control-label" title="<fmt:message key="deleteTheList" />" for="deleteList">
                                                                <i class="fas fa-trash-alt"></i> <fmt:message key="deleteTheList" />
                                                        </label>
                                                </div>

                                                <div class="custom-control custom-control-inline custom-checkbox justify-content-start"
                                                    >
                                                        <input type="checkbox" class="custom-control-input" id="deleteObject" name="deleteObject"
                                                               value="1"/>
                                                        <label class="custom-control-label" title="<fmt:message key="deleteTheProduct" />" for="deleteObject">
                                                                <i class="fas fa-ban"></i> <fmt:message key="deleteTheProduct" />
                                                        </label>
                                                </div>

                                                <div class="input-group">

                                                        <%-- email di utente da condividere --%>
                                                        <input id="inputEmail" class="form-control user-email" type="email" name="userEmail"
                                                               placeholder="email dell'utente da condividere" required="required"/>
                                                        <div class="input-group-append">
                                                                <input type="hidden" name="action" value="insert">
                                                                <input id="InputHiddenlistId" type="hidden" name="listId" value="${list.id}">
                                                                <input class="btn btn-info" type="submit" value="<fmt:message key="sharing" />"/>
                                                        </div>

                                                </div>
                                                <div class="error-messages">
                                                        <p class="no-exist">
                                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_NOT_EXISTING" />
                                                        </p>
                                                        <p class="already-shared">
                                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="this user has already been shared" />
                                                        </p>
                                                </div>

                                        </form>

                                </div>

                                <%-- la lista di condivisione già creata --%>
                                <div class="sharing-list">

                                        <%-- se ci sono le condivisioni --%>
                                        <c:if test="${not empty generalPermissionsOnList}">

                                                <%-- stampa il numero di utente condiviso --%>
                                                <h2>${generalPermissionsOnList.size()} <fmt:message key="sharedUsers" /></h2>

                                                <%-- stampa la lista di condivisione --%>
                                                <c:forEach var="permission" items="${generalPermissionsOnList}">

                                                        <%-- get user bean dall'id user di permesso --%>
                                                        <custom:getUserById userId="${permission.userId}"/>

                                                        <div class="list-item">

                                                                <%-- stampa user img e user email --%>
                                                                <div class="user-info">

                                                                        <div class="user-img">
                                                                                <img class="img-fluid"
                                                                                     src="<c:url value="/image/user/${SingleUser.img}"/>"
                                                                                     alt="${SingleUser.name}"/>
                                                                        </div>
                                                                        <span>
                                                                                <i class="fas fa-envelope"></i> ${SingleUser.email}
                                                                        </span>
                                                                </div>

                                                                <%-- stampa 4 checkbox in base il permesso --%>
                                                                <div class="sharing-option">
                                                                        <form class="form-inline"
                                                                              action="<c:url value="/service/sharingService"/>"
                                                                              method="GET">

                                                                                <div class="custom-control custom-control-inline custom-checkbox  justify-content-start">
                                                                                        <input type="checkbox" class="custom-control-input"
                                                                                               id="modifyList+${permission.userId}" name="modifyList"
                                                                                               value="1" ${permission.modifyList?"checked=\"checked\"":""} />
                                                                                        <label class="custom-control-label" title="<fmt:message key="editTheList" />"
                                                                                               for="modifyList+${permission.userId}">
                                                                                                <i class="fas fa-edit"></i>
                                                                                        </label>
                                                                                </div>
                                                                                <div class="custom-control custom-control-inline custom-checkbox  justify-content-start">
                                                                                        <input type="checkbox" class="custom-control-input"
                                                                                               id="deleteList+${permission.userId}" name="deleteList"
                                                                                               value="1" ${permission.deleteList?"checked=\"checked\"":""} />
                                                                                        <label class="custom-control-label" title="<fmt:message key="deleteTheList" />"
                                                                                               for="deleteList+${permission.userId}">
                                                                                                <i class="fas fa-trash-alt"></i>
                                                                                        </label>
                                                                                </div>
                                                                                <div class="custom-control custom-control-inline custom-checkbox  justify-content-start">
                                                                                        <input type="checkbox" class="custom-control-input"
                                                                                               id="addObject+${permission.userId}" name="addObject"
                                                                                               value="1" ${permission.addObject?"checked=\"checked\"":""} />
                                                                                        <label class="custom-control-label" title="<fmt:message key="addTheProduct" />"
                                                                                               for="addObject+${permission.userId}">
                                                                                                <i class="fas fa-cart-plus"></i>
                                                                                        </label>
                                                                                </div>
                                                                                <div class="custom-control custom-control-inline custom-checkbox justify-content-start"
                                                                                     title="elimina il prodotto">
                                                                                        <input type="checkbox" class="custom-control-input"
                                                                                               id="deleteObject+${permission.userId}" name="deleteObject"
                                                                                               value="1" ${permission.deleteObject?"checked=\"checked\"":""} />
                                                                                        <label class="custom-control-label" title="<fmt:message key="deleteTheProduct" />"
                                                                                               for="deleteObject+${permission.userId}">
                                                                                                <i class="fas fa-ban"></i>
                                                                                        </label>
                                                                                </div>

                                                                                <%-- opzione per modificare o eliminare il permesso --%>
                                                                                <div class="form-submit">
                                                                                        <input type="hidden" name="action" value="update">
                                                                                        <input type="hidden" name="permissionId" value="${permission.id}">
                                                                                        <input type="hidden" name="listId" value="${list.id}">
                                                                                        <input class="btn btn-info" type="submit" value="<fmt:message key="modify" />"/>
                                                                                        <a class="btn btn-danger"
                                                                                           href="<c:url value="/service/sharingService?action=delete&permissionId=${permission.id}&listId=${list.id}"/>" onclick="if(!confirm('<fmt:message key="are you sure?" />')) return false;"><fmt:message key="delete" /></a>
                                                                                </div>

                                                                        </form>

                                                                </div>

                                                        </div>

                                                </c:forEach>

                                        </c:if>

                                        <%-- se non ci sono condivisioni --%>
                                        <c:if test="${empty generalPermissionsOnList}">

                                                <%-- stampa l'avviso --%>
                                                <div class="item-empty">
                                                        <h4><fmt:message key="you have not yet shared" /></h4>
                                                </div>

                                        </c:if>
                                </div>


                        </div>


                        <%-- box-footer  --%>
                        <div class="modal-footer">

                        </div>

                </div>
        </div>
</div>


