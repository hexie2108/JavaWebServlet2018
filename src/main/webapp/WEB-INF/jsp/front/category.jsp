<%-- 
    pagina di categoria
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="category-main-section col-12">

        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="<c:url value="/"/>">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>
                        <span>
                                <i class="fas fa-store"></i> categoria
                        </span>
                        <span>&gt;</span>
                        <span>
                                ${categoria.name}
                        </span>
                </div>

                <%-- image di categoria attuale--%>
                <div class="category-image-section d-inline">
                        <img class="category-logo " src="<c:url value="/image/categoryProduct/${categoria.img}"/>"
                             alt="${categoria.name}"/>
                </div>

                <div class="product-list">
                        <div class="product-list-head">

                        </div>

                        <jsp:include page="/WEB-INF/jsp/front/module/productList.jsp"/>
                </div>

        </div>
</div>


<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForAddProductInShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
