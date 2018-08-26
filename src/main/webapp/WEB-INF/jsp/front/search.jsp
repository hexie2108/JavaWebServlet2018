<%-- 
    pagina per la ricerca
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                                <i class="fas fa-search"></i> ricerca
                        </span>
                        <span>&gt;</span>
                        <span>
                                ${param.searchWords}
                        </span>

                </div>

                <%-- links per impostare l'ordine--%>
                <div class="order-manage float-right">
                        <span>
                                Ordina per:
                        </span>

                        <div class="btn-group">
                                <button class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#">
                                        <c:choose>
                                                <c:when test="${(param.order == 'productName' || empty param.order) && (param.direction == 'asc')}">
                                                        nome <i class="fas fa-sort-alpha-down"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'productName' || empty param.order) && (empty param.direction || param.direction == 'desc')}">
                                                        nome <i class="fas fa-sort-alpha-up"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'categoryName'                    ) && (param.direction == 'asc')}">
                                                        categoria <i class="fas fa-sort-alpha-down"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'categoryName'                    ) && (empty param.direction ||param.direction == 'desc')}">
                                                        categoria <i class="fas fa-sort-alpha-up"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'relevance'                       ) && (empty param.direction || param.direction == 'asc')}">
                                                        rilevanza <i class="fas fa-sort-amount-down"></i>
                                                </c:when>
                                                <c:when test="${(param.order == 'relevance'                       ) && (param.direction == 'desc')}">
                                                        rilevanza <i class="fas fa-sort-amount-up"></i>
                                                </c:when>
                                        </c:choose>
                                </button>
                                <div class="dropdown-menu dropdown-menu-right">
                                        <%-- ordinare per nome di prodotto--%>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=productName&direction=asc"/>">nome <i class="fas fa-sort-alpha-down"></i></a>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=productName&direction=desc"/>">nome <i class="fas fa-sort-alpha-up"></i></a>
                                                <%-- ordinare per nome di categoria--%>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=categoryName&direction=asc"/>">categoria <i class="fas fa-sort-alpha-down"></i></a>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=categoryName&direction=desc"/>">categoria <i class="fas fa-sort-alpha-up"></i></a>
                                                <%-- ordinare per relevance --%>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=relevance&direction=asc"/>">rilevanza <i class="fas fa-sort-amount-down"></i></a>
                                        <a class="dropdown-item" href="<c:url value="/search?searchWords=${param.searchWords}&order=relevance&direction=desc"/>">rilevanza <i class="fas fa-sort-amount-up"></i></a>
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
                                                oppos! non abbiamo trovato niente
                                        </h2>

                                        <%-- link per creare il nuovo l'oggetto --%>
                                        <a class="btn btn-info mt-3" href="<c:url value="/updateProduct"/>">
                                                <i class="fas fa-edit"></i> inserisci il tuo oggetto
                                        </a>

                                </div>
                        </c:if>

                </div>

        </div>

</div>

<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForAddProductInShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
