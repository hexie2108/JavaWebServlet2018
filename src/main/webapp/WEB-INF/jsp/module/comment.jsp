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
                <c:forEach var="comment" items="${listComment}">
                        <div class="comment-item ${comment.userId == sessionScope.user.id?"my-comment":""}">
                                <div class="comment-user">
                                        <custom:getUserNameAndImgById userId="${comment.userId}" />
                                </div>
                                <div class="comment-text">
                                        <p>
                                                ${comment.text}
                                        </p>
                                </div>
                        </div>
                </c:forEach>
        </div>
        <div class="edit-comment">
                <form action="#" method="POST">
                        <textarea name="commentText">
                                                
                        </textarea>
                        <input type="hidden" name="listId" value="${list.id}"/>
                        <input class="btn btn-info" type="submit" value="invia" />
                </form>
        </div>
</div>                        



