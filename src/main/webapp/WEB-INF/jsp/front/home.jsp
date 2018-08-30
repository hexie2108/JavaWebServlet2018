<%-- 
    home page
--%>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="main-section col-9">

        <div class="content">


                <c:if test="${empty param.page || param.page ==1}">
                        <%-- componente slider --%>
                        <jsp:include page="/WEB-INF/jsp/front/module/slider.jsp"/>
                </c:if>
                <c:if test="${not empty param.page && param.page > 1}">
                        <%-- breadcrumb dalla 2° pagine--%>
                        <div class="breadcrumbs">
                                <a href="<c:url value="/"/>">
                                        <i class="fas fa-home"></i>
                                </a>
                                <span>&gt;</span>
                                <span>
                                         ${param.page}° <fmt:message key="page" />
                                </span>
                                
                        </div>
                </c:if>

                <div class="product-list">

                        <div class="product-list-head mt-2 mb-2">
                                <h1 class="list-title">
                                        <fmt:message key="recent products" />
                                </h1>
                        </div>

                        <%-- lista di prodotto --%>
                        <jsp:include page="/WEB-INF/jsp/front/module/productList.jsp"/>

                </div>

        </div>

</div>

<%-- sidebar --%>
<jsp:include page="/WEB-INF/jsp/front/sidebar.jsp"/>
<%-- finestra laterale di lista spesa --%>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForShoppingList.jsp"/>
<%-- finestra per inserire il prodotto --%>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForAddProductInShoppingList.jsp"/>
<%-- pié di pagina --%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
