<%-- 
    componente di paginazione per la lista di prodotto
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pagination-box">
        <%-- se il numero di pagina resti è >= 0 --%>
        <c:if test="${requestScope.nPages > 0}">
                <ul class="pagination">
                        <c:set var="currPage" scope="page" value="${empty param.page?1: param.page}"/>
                        <c:if test="${pageScope.currPage>1}">
                                <%-- link di precedente --%>
                                <li class="page-item page-previous">
                                        <c:url var="linkUrl" value="">
                                                <c:forEach items="${paramValues}" var="entryParam">
                                                        <c:if test="${entryParam.key != 'page'}">
                                                                <c:forEach var="value" items="${entryParam.value}">
                                                                        <c:param name="${entryParam.key}" value="${value}"/>
                                                                </c:forEach>
                                                        </c:if>
                                                </c:forEach>
                                                <c:param name="page" value="${pageScope.currPage-1}"/>
                                        </c:url>
                                        <a class="page-link " href="${linkUrl}">precedente</a>
                                </li>
                        </c:if>

                        <c:set scope="page" var="begin" value="${(pageScope.currPage - 3 > 0)? pageScope.currPage - 3: 0}"/>
                        <c:set scope="page" var="end" value="${(pageScope.begin + 4) > requestScope.nPages-1?requestScope.nPages-1: (pageScope.begin + 4)}"/>

                        <c:forEach begin="${pageScope.begin}" end="${pageScope.end}" var="i">
                                <li class="page-item ${i+1==pageScope.currPage? "active":""}">
                                        <c:url var="linkUrl" value="">
                                                <c:forEach items="${paramValues}" var="entryParam">
                                                        <c:if test="${entryParam.key != 'page'}">
                                                                <c:forEach var="value" items="${entryParam.value}">
                                                                        <c:param name="${entryParam.key}" value="${value}"/>
                                                                </c:forEach>
                                                        </c:if>
                                                </c:forEach>
                                                <c:param name="page" value="${i+1}"/>
                                        </c:url>
                                        <a class="page-link " href="${linkUrl}">${i+1}</a>
                                </li>
                        </c:forEach>

                        <c:if test="${pageScope.currPage != requestScope.nPages}">
                                <%-- link di successivo --%>
                                <li class="page-item page-next">
                                        <c:url var="linkUrl" value="">
                                                <c:forEach items="${paramValues}" var="entryParam">
                                                        <c:if test="${entryParam.key != 'page'}">
                                                                <c:forEach var="value" items="${entryParam.value}">
                                                                        <c:param name="${entryParam.key}" value="${value}"/>
                                                                </c:forEach>
                                                        </c:if>
                                                </c:forEach>
                                                <c:param name="page" value="${pageScope.currPage+1}"/>
                                        </c:url>
                                        <a class="page-link " href="${linkUrl}">successivo</a>
                                </li>
                        </c:if>
                </ul>
        </c:if>
</div>
