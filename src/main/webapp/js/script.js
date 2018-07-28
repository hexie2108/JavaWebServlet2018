

/**
 * set dinamicamente id di prodotto da aggiungere nella lista
 */

function setProductIdForAddInList(productId) {




        var item = document.getElementById("item-" + productId);
        var image = item.getElementsByTagName("img")[0].getAttribute("src");
        var categoryName = item.getElementsByClassName("list-item-cat-link")[0].innerHTML;
        var categoryLink = item.getElementsByClassName("list-item-cat-link")[0].getAttribute("href");
        var name = item.getElementsByClassName("list-item-title")[0].innerHTML;
        var description = item.getElementsByClassName("list-item-description")[0].innerHTML;
        var logo = item.getElementsByTagName("img")[1].getAttribute("src");


        var boxAddItem = document.getElementById("boxAddItem");
        boxAddItem.getElementsByClassName("item-img")[0].setAttribute("src", image);
        boxAddItem.getElementsByClassName("item-img")[0].setAttribute("alt", name);
        boxAddItem.getElementsByClassName("modal-title")[0].innerHTML = boxAddItem.getElementsByClassName("modal-title")[0].innerHTML + name;
        boxAddItem.getElementsByClassName("item-logo-img")[0].setAttribute("src", logo);
        boxAddItem.getElementsByClassName("item-cat-link")[0].setAttribute("href", categoryLink);
        boxAddItem.getElementsByClassName("item-cat-link")[0].innerHTML = categoryName;
        boxAddItem.getElementsByClassName("item-description-text")[0].innerHTML = description;

        document.getElementById("productIdToAdd").value = productId;

}

/**
 * funzione che passa i dati del prodotto per visualizzarlo nella finestrina
 * @param {type} productId
 * @param {type} loggerUser utente loggato o utente anonimo 
 * @param {type} statusOfBuy stato del prodotto
 * @param {type} canYouDelete permesso di eliminare il prodotto dalla lista
 * @returns {undefined}
 */
function showProductWindowsFromList(productId, loggerUser, statusOfBuy, canYouDelete) {



        var item = document.getElementById("productIdInList-" + productId);
        var image = item.getElementsByClassName("img")[0].getAttribute("src");
        var categoryName = item.getElementsByClassName("cat-name")[0].value;
        var categoryLink = item.getElementsByClassName("cat-link")[0].value;
        var name = item.getElementsByClassName("name")[0].value;
        var description = item.getElementsByClassName("description")[0].value;
        var logo = item.getElementsByClassName("logo-img")[0].value;


        var boxShowItem = document.getElementById("boxShowItem");
        boxShowItem.getElementsByClassName("item-img")[0].setAttribute("src", image);
        boxShowItem.getElementsByClassName("item-img")[0].setAttribute("alt", name);
        boxShowItem.getElementsByClassName("modal-title")[0].innerHTML = boxShowItem.getElementsByClassName("modal-title")[0].innerHTML+name;
        boxShowItem.getElementsByClassName("item-logo-img")[0].setAttribute("src", logo);
        boxShowItem.getElementsByClassName("item-cat-link")[0].setAttribute("href", categoryLink);
        boxShowItem.getElementsByClassName("item-cat-link")[0].innerHTML = categoryName;
        boxShowItem.getElementsByClassName("item-description-text")[0].innerHTML = description;

        //utente anonimo
        if (loggerUser == false) {
                boxShowItem.getElementsByClassName("productIdFromList")[0].value = productId;
        } else {
                var listId = item.getElementsByClassName("list-id")[0].value;
                //se prodotto non è stato ancora comprato
                if (statusOfBuy == false) {
                        boxShowItem.getElementsByClassName("productIdFromList")[0].value = productId;
                        boxShowItem.getElementsByClassName("listIdFromList")[0].value = listId;
                        boxShowItem.getElementsByClassName("submit-button")[0].style.display = "inline-block";
                } else {
                        boxShowItem.getElementsByClassName("submit-button")[0].style.display = "none";
                }
                //se hai il permesso di eliminare il prodotto
                if (canYouDelete == true)
                {
                        boxShowItem.getElementsByClassName("productIdFromList")[1].value = productId;
                        boxShowItem.getElementsByClassName("listIdFromList")[1].value = listId;
                        boxShowItem.getElementsByClassName("submit-button")[1].style.display = "inline-block";
                } else {
                        boxShowItem.getElementsByClassName("submit-button")[1].style.display = "none";
                }
        }

}



/**
 * attivare il submit se e solo se   la categoria della lista locale selezionata non sia nullo
 */

function checkValueOfCategoryList(categoryList) {

        if (categoryList.value > -1)
        {
                document.getElementById("submitToChangeCategoryList").removeAttribute("disabled");
        } else {
                document.getElementById("submitToChangeCategoryList").setAttribute("disabled", "disabled");
        }
}