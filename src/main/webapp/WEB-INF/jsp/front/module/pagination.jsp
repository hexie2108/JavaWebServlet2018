<%-- 
    componente di paginazione per la lista di prodotto
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="pagination-box">

        <%-- se il numero di pagina resti è >= 0 --%> 
        <c:if test="${numberOfPageRest>=0}">

                <ul class="pagination">

                        <%-- se non è nella prima pagina --%>
                        <c:if test="${not empty param.page && param.page>1}">

                                <%-- link di precedente --%>
                                <li class="page-item page-previous">
                                        <a class="page-link " href="${basePath}page=${param.page-1}">
                                                precedente
                                        </a>
                                </li>

                        </c:if>

                        <%-- le logiche per settare le indici di paginazione --%>        
                        <c:choose>
                                <c:when test="${(empty param.page || param.page==1)&& numberOfPageRest <4}">
                                        <c:set var="end" value="${numberOfPageRest}"/>
                                </c:when>
                                <c:when test="${(empty param.page || param.page==1) && numberOfPageRest >=4}">
                                        <c:set var="end" value="4"/>
                                </c:when>
                                <c:when test="${not empty param.page && param.page < 3 && numberOfPageRest <3}">
                                        <c:set var="end" value="${numberOfPageRest+1}"/>
                                </c:when>
                                <c:when test="${not empty param.page && numberOfPageRest >1}">
                                        <c:set var="end" value="4"/>
                                </c:when>
                                <c:otherwise>
                                        <c:set var="end" value="${numberOfPageRest+2}"/>
                                </c:otherwise>
                        </c:choose>

                        <%-- stampa le indici di paginazione --%>      
                        <c:forEach begin="0" end="${end}" var="i" >

                                <%-- caso della prima pagina --%>
                                <c:if test="${empty param.page}">

                                        <li class="page-item ${ i==0 ? "active":""}">

                                                <a class="page-link " href="${basePath}page=${i+1}">
                                                        ${ i+1}
                                                </a>

                                        </li>

                                </c:if>

                                <%-- caso della seconda e terza pagina --%>       
                                <c:if test="${not empty param.page && param.page<3}">   

                                        <li class="page-item ${ (i+1)==param.page ? "active":""}">

                                                <a class="page-link " href="${basePath}page=${i+1}">
                                                        ${ i+1}
                                                </a>
                                        </li>

                                </c:if>

                                <%-- caso delle pagine successive --%>       
                                <c:if test="${not empty param.page && param.page>2}">   

                                        <li class="page-item ${i==2 ? "active":""}">
                                                <a class="page-link " href="${basePath}page=${param.page-2+i}">
                                                        ${param.page-2+i}
                                                </a>
                                        </li>

                                </c:if>


                        </c:forEach>

                        <%-- se non è sull'ultima pagina--%>                 
                        <c:if test="${numberOfPageRest != 0}">

                                <%-- link di successivo --%>
                                <li class="page-item page-next">
                                        <a class="page-link " href="${basePath}page=${empty param.page?2:param.page+1}">
                                                successivo
                                        </a>
                                </li>

                        </c:if>    

                </ul>

        </c:if>

</div>

