<%-- 
    la finestra per aggiungere prodotto in lista spesa
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<%--  box --%>
<div class="modal fade custom-modal" id="boxAddItem">
        <div class="modal-dialog">
                <div class="modal-content">

                        <%-- box-head --%>
                        <div class="modal-header">
                                <%-- stampa il nome del prodtto come titolo --%>
                                <h4 class="modal-title">
                                        <i class="fas fa-shopping-basket"></i>
                                        <span class="item-name"></span>
                                </h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <%-- box-body --%>
                        <div class="modal-body">
                                <div class="left-body">

                                        <%-- img di prodotto --%>
                                        <img class="item-img img-fluid" alt="item-name"/>

                                        <%--log di prodotto  --%>
                                        <div class="item-logo">
                                                <p class="font-weight-bold">
                                                        <i class="far fa-bookmark"></i> <fmt:message key="logo" />
                                                </p>
                                                <div class="item-logo-img-box">
                                                        <img class="item-logo-img img-fluid" alt="<fmt:message key="logo" />"/>
                                                </div>
                                        </div>

                                </div>

                                <div class="right-body">
                                        <div class="item-information">

                                                <%-- link della categoria di prodotto --%>
                                                <div class="item-cat">
                                                        <p class="font-weight-bold">
                                                                <i class="fas fa-store"></i> <fmt:message key="category" />
                                                        </p>
                                                        <p>
                                                                <a class="item-cat-link" href="#"></a> 
                                                        </p>
                                                </div>

                                                <%-- la descrizione del prodotto --%>
                                                <div class="item-description">
                                                        <p class="font-weight-bold">
                                                                <i class="far fa-file-alt"></i> <fmt:message key="description" />:
                                                        </p>
                                                        <p class="item-description-text">

                                                        </p>
                                                </div>

                                        </div>

                                        <div class="add-box">

                                                <%-- se è un utente anonimo--%>
                                                <c:if test="${empty sessionScope.user}">

                                                        <%--  form di inserimento per utente anonimo--%>
                                                        <form action="<c:url value="/service/updateItemInListUnloggedUserOnlyService"/>"
                                                              method="GET">
                                                                <label for="select-list" class="d-block font-weight-bold">
                                                                        <i class="fas fa-list"></i> <fmt:message key="SelectTheList" />:
                                                                </label>
                                                                <select id="select-list" class="form-control custom-select w-100" name="listId">
                                                                        <option value="default"><fmt:message key="DefaultList" /></option>
                                                                </select>
                                                                <div class="operation mt-3">

                                                                        <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                                                        <input type="hidden" name="action" value="insert"/>
                                                                        <button class="btn btn-info d-inline-block" type="submit">
                                                                                <i class="fas fa-cart-plus"></i> <fmt:message key="add" />
                                                                        </button>

                                                                </div>
                                                        </form>

                                                </c:if>

                                                <%-- se è un utente loggato--%>
                                                <c:if test="${not empty sessionScope.user}">

                                                        <%-- se utente ha qualche lista con il permesso di inserire il prodotto--%>
                                                        <c:if test="${not empty requestScope.addbleLists}">

                                                                <%--  form per inserire il prodotto nella lista selezionata--%>
                                                                <form action="<c:url value="/service/updateItemInListService"/>"
                                                                      method="GET">

                                                                        <label for="select-list" class="d-block font-weight-bold">
                                                                                <i class="fas fa-list"></i> <fmt:message key="SelectTheList" />:
                                                                        </label>
                                                                        <select id="select-list" class="form-control custom-select w-100" name="listId">

                                                                                <%--  stampa i nomi di tutte le liste  con il permesso di inserire il prodotto--%>
                                                                                <c:forEach var="shoppingList" items="${requestScope.addbleLists}">
                                                                                        <option value="${shoppingList.id}" ${sessionScope.myListId==shoppingList.id?"selected=\"selected\"" : ""} >${shoppingList.name}</option>
                                                                                </c:forEach>

                                                                        </select>

                                                                        <div class="operation mt-3">
                                                                                <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                                                                <input type="hidden" name="action" value="insert"/>
                                                                                <button class="btn btn-info d-inline-block" type="submit">
                                                                                        <i class="fas fa-cart-plus"></i> <fmt:message key="add" />
                                                                                </button>
                                                                                <%-- link per creare la nuova --%>
                                                                                <a class="btn btn-info d-inline-block"  href="<c:url value="/updateList"/>">
                                                                                        <i class="fas fa-plus"></i> <fmt:message key="createTheNewList" />
                                                                                </a>

                                                                        </div>
                                                                </form>

                                                        </c:if>

                                                        <%-- se utente loggato non ha alcuna lista con il permesso di inserire il prodotto--%>
                                                        <c:if test="${empty requestScope.addbleLists}">

                                                                <%-- stampa l'avviso--%>
                                                                <p class="font-weight-bold">
                                                                        <fmt:message key="youDoNotHaveAListYet" />
                                                                </p>

                                                                <%-- link per creare la nuova lista--%>
                                                                <div class="operation mt-3">
                                                                        <a class="btn btn-info d-inline-block"  href="<c:url value="/updateList"/>">
                                                                                <i class="fas fa-plus"></i> <fmt:message key="createTheNewList" />
                                                                        </a>
                                                                </div>

                                                        </c:if>

                                                </c:if>

                                        </div>
                                </div>
                        </div>

                        <%-- box-footer  --%>
                        <div class="modal-footer">

                        </div>

                </div>
        </div>
</div>