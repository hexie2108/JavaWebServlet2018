<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    componente del commento
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>




<div class="comments">




        <div class="list-comment">
                <!-- se ci sono alcuni commento -->
                <c:if test="${not empty numberOfComment}">
                        <h2><i class="fas fa-comments"></i> ${numberOfComment} commenti</h2>
                        <c:forEach var="comment" items="${listComment}">
                                <div class="comment-item ${comment.userId == sessionScope.user.id?"my-comment":""}">
                                        <div class="comment-info">
                                                <custom:getUserNameAndImgById userId="${comment.userId}" />

                                        </div>
                                        <div class="comment-text">
                                                <p>
                                                        ${comment.text}
                                                </p>
                                        </div>
                                        <div class="comment-delete">
                                                <c:if test="${comment.userId == sessionScope.user.id}">
                                                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/service/comment?action=delete&commentId=${comment.id}"><i class="fas fa-trash-alt"></i> elimina</a>
                                                </c:if>
                                        </div>       
                                </div>
                        </c:forEach>
                </c:if>
                <!-- se non ci sono alcun commento -->      
                <c:if test="${empty numberOfComment}"> 
                        <h2> al momento, non c'è ancora il commento </h2>
                </c:if>
        </div>
        <div class="edit-comment">
                <form action="${pageContext.request.contextPath}/service/comment" method="POST">
                        <textarea name="commentText" placeholder="lasciare qualche messaggi"></textarea>
                        <input type="hidden" name="action" value="insert"/>
                        <input type="hidden" name="listId" value="${list.id}"/>
                        <input class="btn btn-info" type="submit" value="invia" />
                </form>
        </div>
</div>                        



