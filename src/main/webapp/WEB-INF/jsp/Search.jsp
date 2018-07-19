<%-- 
   
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    pagina di ricerca
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/Header.jsp"></jsp:include>


        <div class="search-main-section col-12">
                <div class="content">

                        <div class="product-list">
                                <div class="product-list-head product-list-head mt-2 mb-2 pt-3 pb-3">

                                        <!-- breadcrumb-->
                                        <div class="d-inline">
                                                        <a href="${pageContext.request.contextPath}">
                                                                <i class="fas fa-home"></i>
                                                        </a>
                                                <span>&gt;</span>                       
                                                <span>ricerca</span>  
                                                <span>&gt;</span>                       
                                                <span>${param.searchWords}</span>
                                        </div>
                                <!-- bottone per impostare l'ordine-->
                                        <div class="order-manage float-right">
                                                <span>ordina per : 
                                                <a class="btn btn-info ${param.order == "productName" || empty param.order ? "disabled" : "" }" href="${pageContext.request.contextPath}/search?searchWords=${param.searchWords}&order=productName">nome</a>
                                                <a class="btn btn-info ${param.order == "categoryName" ? "disabled" : "" }" href="${pageContext.request.contextPath}/search?searchWords=${param.searchWords}&order=categoryName">categoria</a>

                                        </div>
                                </div>
                                <c:if test="${empty productList}">
                                        <div class="resultEmpty text-center">
                                                <h2>
                                                        oppos! non abbiamo trovato niente
                                                </h2>
                                                <a class="btn btn-info" href="#">inserisci tuo oggetto</a>
                                        </div>
                                </c:if>
                                <c:if test="${ not empty productList}">
                                        <jsp:include page="/WEB-INF/jsp/module/ProductList.jsp"></jsp:include>
                                </c:if>


                </div>
        </div>               
</div>

<jsp:include page="/WEB-INF/jsp/Footer.jsp"></jsp:include>
