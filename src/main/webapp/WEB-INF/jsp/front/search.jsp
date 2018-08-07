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

                        <a href="${pageContext.request.contextPath}">
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
                                <i class="fas fa-sort-alpha-down"></i> ordina per: 
                        </span>
                        <%-- ordinare per nome di prodotto--%>
                        <a class="btn btn-info ${param.order == "productName" || empty param.order ? "active" : "" }" href="${pageContext.request.contextPath}/search?searchWords=${param.searchWords}&order=productName">nome</a>

                        <%-- ordinare per nome di categoria--%>
                        <a class="btn btn-info ${param.order == "categoryName" ? "active" : "" }" href="${pageContext.request.contextPath}/search?searchWords=${param.searchWords}&order=categoryName">categoria</a>

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
                                        <a class="btn btn-info mt-3" href="${pageContext.request.contextPath}/updateProduct">
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
