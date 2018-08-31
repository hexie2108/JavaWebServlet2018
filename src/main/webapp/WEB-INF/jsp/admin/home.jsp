<%-- 
    admin home page
--%>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/admin/header.jsp"/>


<div class="content">



        <div class="statistics">
                <div class="statistics-head">
                        <h2><i class="fas fa-chart-bar"></i> <fmt:message key="statistical data of the site" /></h2>
                </div>      
                <div class="statistics-body">  

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                        <i class="fas fa-sitemap"> <b></b></i> <fmt:message key="the list category number" />
                                </div>
                                <div  class="statistics-value">
                                        ${numberCategoryList}
                                </div>
                        </div>

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                        <i class="fas fa-store"></i> <fmt:message key="the product category number" />
                                </div>
                                <div  class="statistics-value">
                                        ${numberCategoryProduct}
                                </div>
                        </div>

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                        <i class="fas fa-shopping-basket"></i> <fmt:message key="the product number" />
                                </div>
                                <div  class="statistics-value">
                                        ${numberProduct}
                                </div>
                        </div>

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                        <i class="fas fa-users"></i> <fmt:message key="the user number" />
                                </div>
                                <div  class="statistics-value">
                                        ${numberUser}
                                </div>
                        </div>
                </div>      


        </div>

</div>



<%-- pié di pagina --%>
<jsp:include page="/WEB-INF/jsp/admin/footer.jsp"/>
