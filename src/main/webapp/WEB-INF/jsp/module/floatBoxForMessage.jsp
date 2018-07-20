<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    componenti per avvisare il risultato dell'aggiungimento del prodotto
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- finestrina per visualizzare il risultato dell'aggiungimento -->
<c:if test="${not empty resultOfAddItem}">
        <div class="modal fade" id="boxShowMessage">
                <div class="modal-dialog">
                        <div class="modal-content">

                                <!-- box-head -->
                                <div class="modal-header">
                                        <h4 class="modal-title">
                                                <c:if test="${resultOfAddItem == 'ok'}">
                                                        complimenti
                                                </c:if>
                                                <c:if test="${resultOfAddItem != 'ok'}">
                                                        errore
                                                </c:if>       
                                        </h4>
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                </div>

                                <!-- box-body -->
                                <div class="modal-body">
                                        <p>
                                                <c:if test="${resultOfAddItem == 'ok'}">
                                                        il prodotto è stato aggiunto correttamente
                                                </c:if>
                                                <c:if test="${resultOfAddItem != 'ok'}">
                                                        il prodotto è già presente nella lista selezionata
                                                 </c:if>
                                         </p>
                                </div>

                                <!-- box-footer  -->
                                <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                                </div>

                        </div>
                </div>
        </div>

        <c:remove var="resultOfAddItem" scope="session" />
</c:if>