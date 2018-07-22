<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    componenti per avvisare il risultato dell'aggiungimento del prodotto
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- finestrina per visualizzare il risultato dell'aggiungimento -->
<c:if test="${not empty result}">
        <div class="modal fade" id="boxShowMessage">
                <div class="modal-dialog">
                        <div class="modal-content">

                                <!-- box-head -->
                                <div class="modal-header">
                                        <h4 class="modal-title">
                                                <c:choose>
                                                        <c:when test="${sessionScope.result == 'InsertOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'InsertFail'}">
                                                                errore
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'DeleteOk'}">
                                                                complimenti
                                                        </c:when>      
                                                        <c:when test="${sessionScope.result == 'DeleteFail'}">
                                                                errore
                                                        </c:when>       
                                                        <c:when test="${sessionScope.result == 'ChangeListCategoryOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'BoughtOk'}">
                                                                complimenti
                                                        </c:when>
                                                </c:choose>         
                                        </h4>
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                </div>

                                <!-- box-body -->
                                <div class="modal-body">
                                        <p>
                                                <c:choose>
                                                        <c:when test="${sessionScope.result  == 'InsertOk'}">
                                                                il prodotto è stato aggiunto correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result  == 'InsertFail'}">
                                                                il prodotto è già presente nella lista selezionata
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'DeleteOk'}">
                                                                il prodotto è stato cancellato correttamente
                                                        </c:when>      
                                                        <c:when test="${sessionScope.result == 'DeleteFail'}">
                                                                non è possibile eliminare il prodotto specificato
                                                        </c:when>   
                                                        <c:when test="${sessionScope.result  == 'ChangeListCategoryOk'}">
                                                                la categoria della lista è stata cambiata correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result  == 'BoughtOk'}">
                                                                il prodotto specificato è stato assegnato come già comprato
                                                        </c:when>
                                                </c:choose>
                                        </p>
                                </div>

                                <!-- box-footer  -->
                                <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                                </div>

                        </div>
                </div>
        </div>

        <c:remove var="result" scope="session" />
</c:if>