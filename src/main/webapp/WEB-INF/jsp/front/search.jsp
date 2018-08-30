<%-- 
    pagina per la ricerca
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="search-main-section col-12">

        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">

                        <a href="<c:url value="/"/>">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>
                        <span>
                                <i class="fas fa-search"></i> <fmt:message key="search" />
                        </span>
                        <span>&gt;</span>
                        <span>
                                ${param.searchWords}
                        </span>
                        <c:if test="${not empty param.page && param.page > 1}">
                                <span>
                                        ${param.page}° <fmt:message key="page" />
                                </span>
                        </c:if>

                </div>

                <%-- links per impostare l'ordine--%>
                <div class="order-manage float-right">
                        <span>
                                <fmt:message key="sort by" />:
                        </span>

                        <div class="btn-group">
                                <button class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#">
                                        <c:choose>
                                                <c:when test="${(param.order == 'productName'                     ) && (param.direction == 'asc')}">
                                                        <fmt:message key="name" />: <i class="fas fa-sort-alpha-down"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'productName'                     ) && (empty param.direction || param.direction == 'desc')}">
                                                        <fmt:message key="name" /> <i class="fas fa-sort-alpha-up"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'categoryName'                    ) && (param.direction == 'asc')}">
                                                        <fmt:message key="category" /> <i class="fas fa-sort-alpha-down"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'categoryName'                    ) && (empty param.direction ||param.direction == 'desc')}">
                                                        <fmt:message key="category" /> <i class="fas fa-sort-alpha-up"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'relevance'  || empty param.order ) && (empty param.direction || param.direction == 'asc')}">
                                                        <fmt:message key="relevance" /> <i class="fas fa-sort-amount-down"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'relevance'  || empty param.order ) && (param.direction == 'desc')}">
                                                        <fmt:message key="relevance" /> <i class="fas fa-sort-amount-up"></i>
                                                </c:when>
                                        </c:choose>
                                </button>
                                <div class="dropdown-menu dropdown-menu-right">
                                        <%-- ordinare per nome di prodotto--%>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=productName&direction=asc"/>"><fmt:message key="name" /> <i class="fas fa-sort-alpha-down"></i></a>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=productName&direction=desc"/>"><fmt:message key="name" /> <i class="fas fa-sort-alpha-up"></i></a>
                                                <%-- ordinare per nome di categoria--%>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=categoryName&direction=asc"/>"><fmt:message key="category" /> <i class="fas fa-sort-alpha-down"></i></a>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=categoryName&direction=desc"/>"><fmt:message key="category" /> <i class="fas fa-sort-alpha-up"></i></a>
                                                <%-- ordinare per relevance --%>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=relevance&direction=asc"/>"><fmt:message key="relevance" /> <i class="fas fa-sort-amount-down"></i></a>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=relevance&direction=desc"/>"><fmt:message key="relevance" /> <i class="fas fa-sort-amount-up"></i></a>
                                </div>
                        </div>
                </div>

                <div class="product-list">

                        <div class="product-list-head">

                        </div>

                        <%-- se ci sono i prodotti corrispondenti--%>
                        <c:if test="${ not empty productList}">

                                <jsp:include page="/WEB-INF/jsp/front/module/productList.jsp"/>

                        </c:if>

                        <%-- se non ci sono i prodotti corrispondenti--%>
                        <c:if test="${empty productList}">

                                <div class="resultEmpty text-center">

                                        <%-- stampa l'avviso--%>
                                        <h2>
                                               <fmt:message key="oops! we did not find anything" /> 
                                        </h2>

                                        <%-- link per creare il nuovo l'oggetto --%>
                                        <a class="btn btn-info mt-3" href="<c:url value="/updateProduct"/>">
                                                <i class="fas fa-edit"></i> <fmt:message key="insert your product" />
                                        </a>

                                </div>
                        </c:if>

                </div>

        </div>

</div>

<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForAddProductInShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>