<%-- 
    home page
--%>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="main-section col-9">

    <div class="content">
        <%-- componente slider --%>
        <jsp:include page="/WEB-INF/jsp/front/module/slider.jsp"/>

        <div class="product-list">

            <div class="product-list-head">
                <h1 class="list-title">
                    i prodotti ${a}
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
