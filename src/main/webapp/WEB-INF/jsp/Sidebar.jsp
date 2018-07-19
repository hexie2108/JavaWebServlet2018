<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    l'intestazione comune per tutti front-page jsp
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>


<aside class="sidebar col-3">
        
         <!-- la lista di tutti categoira -->
        <div class="tag-category">
                <h2 class="text-center"><i class="fas fa-shopping-basket"></i> CATEGORIA</h2>
                <custom:printAllCategoryProduct>
                        
                </custom:printAllCategoryProduct>
        </div>


</div>
</aside>




