<%-- 
    admin home page
--%>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jsp/admin/header.jsp"/>


<div class="content">



        <div class="statistics">
                <div class="statistics-head">
                        <h2>dati statistici del sito</h2>
                </div>      
                <div class="statistics-body">  

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                        <i class="fas fa-sitemap"> <b></b></i> il numero di cateogria di lista
                                </div>
                                <div  class="statistics-value">
                                        ${numberCategoryList}
                                </div>
                        </div>

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                       <i class="fas fa-store"></i> il numero di cateogria di prodotto
                                </div>
                                <div  class="statistics-value">
                                        ${numberCategoryProduct}
                                </div>
                        </div>

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                      <i class="fas fa-shopping-basket"></i>  il numero di prodotto
                                </div>
                                <div  class="statistics-value">
                                        ${numberProduct}
                                </div>
                        </div>

                        <div class="statistics-item">
                                <div  class="statistics-name">
                                     <i class="fas fa-users"></i>   il numero di utente
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
