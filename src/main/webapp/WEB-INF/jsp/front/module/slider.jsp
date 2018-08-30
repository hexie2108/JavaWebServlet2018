<%-- 

     Componente slider di categoria in home   
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>


        <div id="slider" class="carousel slide" data-ride="carousel">

                <%-- indicatore --%>
                <ul class="carousel-indicators">

                        <c:forEach varStatus="itemStatus" items="${categoryListForSlider}" end="3">
                                <li class="${itemStatus.index == 0 ? "active" : "" }" data-target="#slider"
                                    data-slide-to="${itemStatus.index}">
                                </li>
                        </c:forEach>
                </ul>

                <%-- stampa immagini di categoria --%>
                <div class="carousel-inner">

                        <c:forEach var="product" varStatus="itemStatus" items="${categoryListForSlider}" end="3">

                                <div class="carousel-item ${itemStatus.index == 0 ? "active" : "" }">

                                        <a href="<c:url value="/category?catId=${product.id}"/>">
                                                <img class="img-fluid" src="<c:url value="/image/categoryProduct/${product.img}"/>"
                                                     alt="${product.name}"/>
                                                <div class="carousel-caption">
                                                        <span>${product.name}</span>
                                                </div>
                                        </a>

                                </div>

                        </c:forEach>

                </div>

                <%-- bulsante per next e prev --%>
                <a class="carousel-control-prev" href="#slider" data-slide="prev">
                        <span class="carousel-control-prev-icon"></span>
                </a>

                <a class="carousel-control-next" href="#slider" data-slide="next">
                        <span class="carousel-control-next-icon"></span>
                </a>

                <div class="slider-bg-bar"></div>

        </div>
