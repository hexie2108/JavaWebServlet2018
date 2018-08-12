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
    boxAddItem.getElementsByClassName("item-name")[0].innerHTML = name;
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
    boxShowItem.getElementsByClassName("item-name")[0].innerHTML = name;
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
        if (canYouDelete == true) {
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

    if (categoryList.value > -1) {
        document.getElementById("submitToChangeCategoryList").removeAttribute("disabled");
    } else {
        document.getElementById("submitToChangeCategoryList").setAttribute("disabled", "disabled");
    }
}


/**non è utilizzato
 * inserire il prodotto nella lista locale
 * cookie dura 30giorni
 * @returns  null
 */

function insertProductLocal() {

    var productId = document.getElementById("productIdToAdd").value;
    var productsOfMyList = getCookie("productsOfMyList").split(',');

    var repeatItem = false;
    for (var i = 0; i < productsOfMyList.length; i++) {
        var c = productsOfMyList[i].trim();
        //se ce la ripetizione
        if (c.indexOf(productId) === 0) {

            repeatItem = true;
        }
    }
    if (repeatItem === false) {
        if (productsOfMyList == "") {
            productsOfMyList = productId;
        }
        else {
            productsOfMyList.push(productId);

        }
        setCookie("productsOfMyList", productsOfMyList, 30);
        setCookie("result", "InsertOk", 0);
    } else {
        setCookie("result", "InsertFail", 0);
    }

    location.reload();

}

/**
 * non è utilizzato
 *  elimina il prodotto dalla lista locale
 * @returns  null
 */
function deleteProductLocal() {
    var productId = document.getElementById("productIdFromList").value;
    var productsOfMyList = getCookie("productsOfMyList").split(',');
    var deleteItem = -1;
    for (var i = 0; i < productsOfMyList.length; i++) {
        var c = productsOfMyList[i].trim();
        //trovare la posizione dell'elemento da eliminare
        if (c.indexOf(productId) === 0) {

            deleteItem = i;
        }
    }
    if (deleteItem !== -1) {

        productsOfMyList.splice(i, 1);
        setCookie("productsOfMyList", productsOfMyList.join(','), 30);
        setCookie("result", "DeleteOk", 0);
    } else {
        setCookie("result", "DeleteFail", 0);
    }

    location.reload();

}


/**
 * set la cookie
 * @param {type} cname nome di cookie
 * @param {type} cvalue valore di cookie
 * @param {type} exdays durata
 * @returns {undefined}
 */
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

/**
 * get il valore di cookie , dato la chiave
 * @param {type} cname la chiave
 * @returns {String} valore di cookie associato
 */
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
