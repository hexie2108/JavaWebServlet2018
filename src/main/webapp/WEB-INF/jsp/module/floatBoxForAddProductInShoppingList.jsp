<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    la finestrina per aggiungere prodotto in shopping
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>


<!-- finestrina -->
<div class="modal fade" id="boxAddItem">
        <div class="modal-dialog">
                <div class="modal-content">

                        <!-- box-head -->
                        <div class="modal-header">
                                <h4 class="modal-title">il nome prodotto</h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <!-- box-body -->
                        <div class="modal-body">
                                <div class="left-body">
                                        <img class="item-img img-fluid" alt="item-name" />
                                        <div class="item-logo text-center mt-2 mb-2">
                                                <p class="font-weight-bold">
                                                        logo:
                                                </p>
                                                <img class="item-logo-img img-fluid w-50 "  alt="logo" />
                                        </div>
                                </div>

                                <div class="right-body">
                                        <div class="item-information">


                                                <div class="item-cat">
                                                        <p class="font-weight-bold">
                                                                categoria:
                                                        </p>
                                                        <p>
                                                                <a class="item-cat-link" href="#" >nome della categoria</a>
                                                        </p>
                                                </div>
                                                <div class="item-description">
                                                        <p class="font-weight-bold">
                                                                descrizione:
                                                        </p>
                                                        <p class="item-description-text">
                                                                la descrizione del prodotto
                                                        </p>
                                                </div>

                                        </div>

                                        <div class="add-box">
                                                <!-- se è un utente anonimo-->
                                                <c:if test="${empty sessionScope.user}">
                                                        <form action="${pageContext.request.contextPath}/service/updateItemInListUnloggedUserOnlyService" method="GET">
                                                                <label  for="select-list" class="d-block font-weight-bold">Seleziona la lista:</label>
                                                                <select id="select-list" class="form-control custom-select w-100" name="listId">
                                                                        <option value="default">Default list</option>
                                                                </select>
                                                                <div class="operation mt-3">
                                                                        <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                                                        <input type="hidden" name="action" value="insert"/>
                                                                        <input class="btn btn-info d-inline-block" type="submit" value="aggiunge" />
                                                                        
                                                                </div>
                                                        </form>
                                                </c:if>       
                                                <!-- se è un utente loggato--> 
                                                <c:if test="${not empty sessionScope.user}">
                                                        <!-- se utente loggato ha qualche lista aggiungibile-->
                                                        <c:if test="${not empty requestScope.addbleLists}">
                                                                <form action="${pageContext.request.contextPath}/service/updateItemInListService" method="GET">

                                                                        <label  for="select-list" class="d-block font-weight-bold">Seleziona la lista:</label>
                                                                        <select id="select-list" class="form-control custom-select w-100" name="listId">
                                                                                <c:forEach var="shoppingList" items="${requestScope.addbleLists}">
                                                                                        <option value="${shoppingList.id}" ${sessionScope.myListId==shoppingList.id?"selected=\"selected\"" : ""} >${shoppingList.name}</option>
                                                                                </c:forEach>
                                                                        </select>
                                                                        <div class="operation mt-3">
                                                                                <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                                                                <input type="hidden" name="action" value="insert"/>
                                                                                <input class="btn btn-info d-inline-block" type="submit" value="aggiunge" />
                                                                                <a class="btn btn-info d-inline-block" href="#">crea una nuova</a>
                                                                               
                                                                        </div>
                                                                </form>
                                                        </c:if>
                                                        <!-- se utente loggato non ha nessun lista aggiungibile-->
                                                        <c:if test="${empty requestScope.addbleLists}">
                                                                <p class="font-weight-bold">non hai ancora una lista</p>
                                                                <div class="operation mt-3">
                                                                        <a class="btn btn-info d-inline-block" href="#">crea una nuova</a>
                                                                        
                                                                </div>
                                                        </c:if>
                                                </c:if>  
                                        </div>               
                                </div>
                        </div>
                        <!-- box-footer  -->
                        <div class="modal-footer">

                        </div>

                </div>
        </div>
</div>