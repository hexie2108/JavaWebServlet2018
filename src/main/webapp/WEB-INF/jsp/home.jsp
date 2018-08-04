<%-- 
    home page
--%>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/header.jsp"/>


<div class="main-section col-9">

        <div class="content">
                <%-- componente slider --%>
                <jsp:include page="/WEB-INF/jsp/module/slider.jsp" />
                
                <div class="product-list">
                        
                        <div class="product-list-head">
                                <h1 class="list-title">
                                        i prodotti 
                                </h1>
                        </div>
                        
                        <%-- lista di prodotto --%>
                        <jsp:include page="/WEB-INF/jsp/module/productList.jsp"/>
                        
                </div>
                        
        </div>    
                        
</div>
                        
<%-- sidebar --%>
<jsp:include page="/WEB-INF/jsp/sidebar.jsp"/>
<%-- finestra laterale di lista spesa --%>
<jsp:include page="/WEB-INF/jsp/module/floatBoxForShoppingList.jsp"/>
<%-- finestra per inserire il prodotto --%>
<jsp:include page="/WEB-INF/jsp/module/floatBoxForAddProductInShoppingList.jsp"/>
<%-- pié di pagina --%>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
