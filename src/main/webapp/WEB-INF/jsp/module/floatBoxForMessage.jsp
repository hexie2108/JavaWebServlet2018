<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    finestra per avvisare il risultato delle operazione fatte
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- se ci sono message nel attributo "result"--%>
<c:if test="${not empty sessionScope.result}">

        <div class="modal fade" id="boxShowMessage">
                <div class="modal-dialog">
                        <div class="modal-content">

                                <%-- box-head --%>
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
                                                        <c:when test="${sessionScope.result == 'commentInsertOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'commentDeleteOk'}">
                                                                complimenti
                                                        </c:when>       

                                                </c:choose> 

                                        </h4>
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>

                                </div>

                                <%-- box-body --%>
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
                                                        <c:when test="${sessionScope.result == 'commentInsertOk'}">
                                                                il commento è stato inviato correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'commentDeleteOk'}">
                                                                il commento è stato cancellato correttamente
                                                        </c:when>    
                                                </c:choose>

                                        </p>
                                </div>

                                <%-- box-footer  --%>
                                <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                                </div>

                        </div>
                </div>
        </div>

        <%-- cancella l'attributo "result" --%>
        <c:remove var="result" scope="session" />

</c:if>