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
                                                        <c:when test="${sessionScope.result == 'sharingInsertOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingInsertError'}">
                                                                errore
                                                        </c:when>  
                                                        <c:when test="${sessionScope.result == 'sharingUpdateOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingDeleteOk'}">
                                                                complimenti
                                                        </c:when>


                                                        <c:when test="${sessionScope.result == 'ListInsertOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListUpdateOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListDeleteOk'}">
                                                                complimenti
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'privacy'}">
                                                                Privacy statement
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
                                                        <c:when test="${sessionScope.result == 'sharingInsertOk'}">
                                                                la condivisione è stato inserito correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingInsertError'}">
                                                                non esiste l'utente che tale email
                                                        </c:when> 
                                                        <c:when test="${sessionScope.result == 'sharingUpdateOk'}">
                                                                la condivisione è stato aggiornato correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingDeleteOk'}">
                                                                la condivisione è stato cancellato correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListInsertOk'}">
                                                                la lista è stata creata correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListUpdateOk'}">
                                                                la lista è stata aggiornata correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListDeleteOk'}">
                                                                la lista è stata cancellata correttamente
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'privacy'}">
                                                                But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure? On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee
                                                        </c:when>
                                                </c:choose>

                                        </p>
                                </div>



                                <%-- box-footer  --%>
                                <div class="modal-footer">
                                        <c:if test="${sessionScope.result == 'privacy'}">
                                                <a class="btn btn-info" href="<c:url value="/service/acceptPrivacyService"/>">accetta</a>
                                        </c:if>
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                                </div>

                        </div>
                </div>
        </div>

        <%-- cancella l'attributo "result" --%>
        <c:remove var="result" scope="session"/>

</c:if>


<%-- popup di privacy--%>
<c:if test="${empty cookie.acceptedPrivacy}">

        <div class="popup-privacy fixed-bottom">

                <div class="text">
                        Questo sito utilizza cookie tecnici e di profilazione propri e di terze parti per le sue funzionalità e per inviarti pubblicità, contenuti e servizi più vicini ai tuoi gusti e interessi.
                </div>
                <div class="text-right mb-1">
                        <button class="btn btn-info" onclick="acceptedPrivacy();">
                                accetta
                        </button>
                </div>

        </div>

</c:if>
