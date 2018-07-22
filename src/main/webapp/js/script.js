

/**
 * set dinamicamente id di prodotto da aggiungere nella lista
 */

function setProductIdForAddInList(productId) {
        
       var inputProductId = document.getElementById("productIdToAdd") ;
       inputProductId.value = productId;
        
}

/**
 * attivare il submit se e solo se   la categoria della lista locale selezionata non sia nullo
 */

function checkValueOfCategoryList(categoryList){
        
        if(categoryList.value>-1)
        {
                document.getElementById("submitToChangeCategoryList").removeAttribute("disabled"); 
        }
        else{
                document.getElementById("submitToChangeCategoryList").setAttribute("disabled","disabled");
        }
}