<%-- 
    
    piè di pagina front-end
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


</section>


<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForViewProductFromShoppingList.jsp"/>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForMessage.jsp"/>


<footer class="footer bg-secondary mt-5">

    <div class="author-info">
        @progettoWeb2018
    </div>

</footer>

<script>
    $(document).ready(function(){
        $('.selectpicker').selectpicker({
            style: 'btn-info',
            countSelectedText: '{0}/{1} selected',
            noneResultsText: 'No result matched {0}'
        });
    });
</script>
</div>
</body>
</html>



