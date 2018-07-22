<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    la finestrina per aggiungere prodotto in shopping
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!-- finestrina -->
<div class="modal fade" id="boxAddItem">
        <div class="modal-dialog">
                <div class="modal-content">

                        <!-- box-head -->
                        <div class="modal-header">
                                <h4 class="modal-title">Aggiunge</h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <!-- box-body -->
                        <div class="modal-body">
                                <!-- se è un utente anonimo-->
                                <c:if test="${empty sessionScope.user}">
                                        <form action="${pageContext.request.contextPath}/service/updateItemInListUnloggedUserOnlyService" method="GET">
                               </c:if>       
                                <c:if test="${not empty sessionScope.user}">
                                          <form action="${pageContext.request.contextPath}/service/updateItemInListService" method="GET">
                                 </c:if>
                                                <!-- se non è un utente anonimo--> 

                                                <label  for="select-list" class="d-block">Seleziona la lista:</label>
                                                <select id="select-list" class="form-control custom-select w-100" name="listId">
                                                        <!-- se è un utente anonimo-->
                                                        <c:if test="${empty sessionScope.user}">
                                                                <option value="default">Default list</option>
                                                        </c:if>
                                                        <!-- se non è un utente anonimo-->         
                                                        <c:if test="${not empty sessionScope.user}">
                                                                <c:if test="${not empty sessionScope.allMyList}">
                                                                        <c:forEach var="i" begin="0" end="${sessionScope.allMyList.size()}">
                                                                                <c:if test="${sessionScope.allMyListPermission[i].addObject == true}">
                                                                                        <option value="${sessionScope.allMyList[i].id}" ${sessionScope.allMyList[i].id == sessionScope.myList.id?"selected=\"selected\"":""}>${sessionScope.allMyList[i].name}</option>
                                                                                        <c:set var="hasAtLestOneList" value="1"></c:set>
                                                                                </c:if>
                                                                        </c:forEach>
                                                                </c:if>

                                                        </c:if>

                                                </select>

                                                <div class="operation mt-3">
                                                        <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                                        <input type="hidden" name="action" value="insert"/>
                                                        <!-- se è un utente anonimo oppure se un utente registrato con almeno una lista-->
                                                        <c:if test="${empty sessionScope.user || not empty hasAtLestOneList}">   
                                                                <input class="btn btn-info d-inline-block" type="submit" value="aggiunge" />
                                                        </c:if>
                                                        <a class="btn btn-info d-inline-block" href="#">crea una nuova</a>       
                                                </div>



                                        </form>
                        </div>

                        <!-- box-footer  -->
                        <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                        </div>

                </div>
        </div>
</div>