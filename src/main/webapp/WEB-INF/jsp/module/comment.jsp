<%-- 

    componente del commento
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>


<div class="comments">

        <div class="list-comment">

                <%-- se ci sono qualche commento --%>
                <c:if test="${not empty numberOfComment}">

                        <%-- stampa il numero di commento --%>
                        <h2>
                                <i class="fas fa-comments"></i> ${numberOfComment} commenti
                        </h2>

                        <%-- stampa la lista di commento --%> 
                        <c:forEach var="comment" items="${listComment}">

                                <div class="comment-item ${comment.userId == sessionScope.user.id?"my-comment":""}">

                                        <div class="comment-info">

                                                <%-- get user bean dal id user del commento attraverso custom tag --%>
                                                <custom:getUserById userId="${comment.userId}" />

                                                <%-- stampa user img --%>
                                                <div class="user-img">
                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${SingleUser.img}" alt="${SingleUser.name}"/>
                                                </div>
                                                <%-- stampa user name --%>
                                                <span>
                                                        <i class="far fa-address-card"></i> ${SingleUser.name}
                                                </span>

                                        </div>

                                        <%-- stampa testo di commento --%>
                                        <div class="comment-text">
                                                <p>
                                                        ${comment.text}
                                                </p>
                                        </div>

                                        <%-- se utente attuale è il proprietario del commento, allora stampa la link per eliminare il commento --%>
                                        <div class="comment-delete">
                                                <c:if test="${comment.userId == sessionScope.user.id}">
                                                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/service/comment?action=delete&commentId=${comment.id}"><i class="fas fa-trash-alt"></i> elimina</a>
                                                </c:if>
                                        </div>      

                                </div>

                        </c:forEach>

                </c:if>

                <%-- se non ci sono i commenti --%>
                <c:if test="${empty numberOfComment}"> 

                        <%-- stampa l'avviso --%>
                        <h2> al momento, non c'è ancora il commento </h2>

                </c:if>

        </div>

        <%-- stampa il form per scrivere il commento --%>        
        <div class="edit-comment">
                <form action="${pageContext.request.contextPath}/service/comment" method="POST">
                        <textarea name="commentText" placeholder="lasciare qualche messaggi" required="required"></textarea>
                        <input type="hidden" name="action" value="insert"/>
                        <input type="hidden" name="listId" value="${list.id}"/>
                        <input class="btn btn-info" type="submit" value="invia" />
                </form>
        </div>

</div>                        



