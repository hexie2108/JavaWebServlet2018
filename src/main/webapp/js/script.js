

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
        boxAddItem.getElementsByClassName("modal-title")[0].innerHTML = name;
        boxAddItem.getElementsByClassName("item-logo-img")[0].setAttribute("src", logo);
        boxAddItem.getElementsByClassName("item-cat-link")[0].setAttribute("href", categoryLink);
        boxAddItem.getElementsByClassName("item-cat-link")[0].innerHTML = categoryName;
        boxAddItem.getElementsByClassName("item-description-text")[0].innerHTML = description;

        document.getElementById("productIdToAdd").value = productId;

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