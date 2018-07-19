<%-- 
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    home page
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/Header.jsp"></jsp:include>

        
        <div class="main-section col-9">
                <div class="content">
                <jsp:include page="/WEB-INF/jsp/module/Slider.jsp"></jsp:include>
                        <div class="product-list">
                                <div class="product-list-head">
                                        <h1 class="list-title">
                                                i prodotti
                                        </h1>
                                </div>
                        <jsp:include page="/WEB-INF/jsp/module/ProductList.jsp"></jsp:include>
                        </div>
                </div>               
        </div>

<jsp:include page="/WEB-INF/jsp/Sidebar.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/Footer.jsp"></jsp:include>
