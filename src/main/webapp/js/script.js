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
        if (loggerUser == false)
        {
                boxShowItem.getElementsByClassName("productIdFromList")[0].value = productId;
        }
        else
        {
                var listId = item.getElementsByClassName("list-id")[0].value;
                //se prodotto non è stato ancora comprato
                if (statusOfBuy == false)
                {
                        boxShowItem.getElementsByClassName("productIdFromList")[0].value = productId;
                        boxShowItem.getElementsByClassName("listIdFromList")[0].value = listId;
                        boxShowItem.getElementsByClassName("submit-button")[0].style.display = "inline-block";
                }
                else
                {
                        boxShowItem.getElementsByClassName("submit-button")[0].style.display = "none";
                }
                //se hai il permesso di eliminare il prodotto
                if (canYouDelete == true)
                {
                        boxShowItem.getElementsByClassName("productIdFromList")[1].value = productId;
                        boxShowItem.getElementsByClassName("listIdFromList")[1].value = listId;
                        boxShowItem.getElementsByClassName("submit-button")[1].style.display = "inline-block";
                }
                else
                {
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
        }
        else
        {
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
        for (var i = 0; i < productsOfMyList.length; i++)
        {
                var c = productsOfMyList[i].trim();
                //se ce la ripetizione
                if (c.indexOf(productId) === 0)
                {

                        repeatItem = true;
                }
        }
        if (repeatItem === false)
        {
                if (productsOfMyList == "")
                {
                        productsOfMyList = productId;
                }
                else
                {
                        productsOfMyList.push(productId);

                }
                setCookie("productsOfMyList", productsOfMyList, 30);
                setCookie("result", "InsertOk", 0);
        }
        else
        {
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
        for (var i = 0; i < productsOfMyList.length; i++)
        {
                var c = productsOfMyList[i].trim();
                //trovare la posizione dell'elemento da eliminare
                if (c.indexOf(productId) === 0)
                {

                        deleteItem = i;
                }
        }
        if (deleteItem !== -1)
        {

                productsOfMyList.splice(i, 1);
                setCookie("productsOfMyList", productsOfMyList.join(','), 30);
                setCookie("result", "DeleteOk", 0);
        }
        else
        {
                setCookie("result", "DeleteFail", 0);
        }

        location.reload();

}

/**
 * set cookie e nasconde la finestra di popup di privacy
 * @returns {undefined}
 */

function acceptedPrivacy() {

        document.getElementsByClassName("popup-privacy")[0].style.display = "none";
        setCookie('acceptedPrivacy', 1, 365);

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
 * set la cookie in minuto
 * @param {type} cname nome di cookie
 * @param {type} cvalue valore di cookie
 * @param {type}  durata in minuto
 * @returns {undefined}
 */
function setCookieInMinute(cname, cvalue, exminutes) {
        var d = new Date();
        d.setTime(d.getTime() + (exminutes * 60 * 1000));
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
        for (var i = 0; i < ca.length; i++)
        {
                var c = ca[i].trim();
                if (c.indexOf(name) === 0)
                {
                        return c.substring(name.length, c.length);
                }
        }
        return "";
}



/**
 * funziona per check se ci sono negozi interessati in zona
 * @param {type} listCategoryName
 * @returns {undefined}
 */

function checkNearShop() {

        //se è scaduto la cookie di checked, rieffettua il controllo
        if (getCookie("NearShopChecked") === "")
        {


                //get valore di cateogry name in array
                var listCategoryNameString = document.getElementById("categoryNamesForMap").value;
                if (listCategoryNameString !== "" && listCategoryNameString !== null)
                {
                        var listCategoryNameTemp = listCategoryNameString.split(",");

                        //eliminare l'elemento ripetuto dell'array
                        var listCategoryName = new Array();
                        for (var l = 0; l < listCategoryNameTemp.length; l++)
                        {

                                if (listCategoryName.indexOf(listCategoryNameTemp[l]) === -1)
                                {
                                        listCategoryName.push(listCategoryNameTemp[l]);
                                }
                        }

                        //se la lunghezza è maggiore di 0
                        if (listCategoryName.length > 0)
                        {
                                if (navigator.geolocation)
                                {
                                        navigator.geolocation.getCurrentPosition(function (position) {

                                                var user_lat = position.coords.latitude;
                                                var user_lon = position.coords.longitude;
                                                var center = new google.maps.LatLng(user_lat, user_lon);
                                                //crea l'oggetto mappa
                                                var map = new google.maps.Map(document.getElementById('mapHolder'), {
                                                        center: center,
                                                        zoom: 18,
                                                        minZoom: 16
                                                });
                                                var service = new google.maps.places.PlacesService(map);

                                                var request;
                                                //per ogni nome effettuera una ricerca
                                                for (var i = 0; i < listCategoryName.length; i++)
                                                {
                                                        request = {
                                                                location: center,
                                                                radius: '1000',
                                                                keyword: listCategoryName[i]
                                                        };
                                                        service.nearbySearch(request, function (results, status) {

                                                                //se ha trovato set cookie e visualizza voce notifica di menu
                                                                if (status === google.maps.places.PlacesServiceStatus.OK)
                                                                {

                                                                        setCookieInMinute('notifica', 1, 10);
                                                                        $("#link-notifica").show("slow");

                                                                }
                                                        });


                                                }

                                        });


                                }
                                else
                                {
                                        alert("Geolocation is not supported");
                                }

                        }


                }

                //set cookie di checked che dura 10minuti
                setCookieInMinute('NearShopChecked', 1, 10);
        }



}

/**
 * iniziallizare la mappa
 * @returns {undefined}
 */

function initMap() {

        if (navigator.geolocation)
        {
                navigator.geolocation.getCurrentPosition(creaMap, errorHandler);
        }
        else
        {
                var text = document.getElementById("errText");
                text.innerHTML = "Geolocation is not supported";
        }


}

/**
 * creare la mappa
 * @param {type} position
 * @returns {undefined}
 */

var map;
var infoWindow;
function creaMap(position) {

        var user_lat = position.coords.latitude;
        var user_lon = position.coords.longitude;
        var center = new google.maps.LatLng(user_lat, user_lon);
        //crea la mappa
        map = new google.maps.Map(document.getElementById('mapHolder'), {
                center: center,
                zoom: 18,
                minZoom: 16
        });


        //crea l'indicatore dell'utente attuale
        var myPosition = new google.maps.Marker({
                map: map,
                icon: {
                        path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                        scale: 5,
                        fillColor: "white",
                        fillOpacity: 1.0,
                        strokeColor: "#17a2b8"

                },
                position: center,
                animation: google.maps.Animation.BOUNCE
        });

        //crea finestrina per stampare l'informazione
        infoWindow = new google.maps.InfoWindow();

        //aggancia l'evento clic su l'indicatore dell'utente
        google.maps.event.addListener(myPosition, 'click', function () {
                infoWindow.setContent("<b>sei qui</b>");
                infoWindow.open(map, this);
        });

        var service = new google.maps.places.PlacesService(map);

        //get valore di cateogry name in array
        var listCategoryNameString = document.getElementById("categoryNamesForMap").value;
        if (listCategoryNameString !== "" && listCategoryNameString !== null)
        {
                var listCategoryNameTemp = listCategoryNameString.split(",");

                //eliminare l'elemento ripetuto dell'array
                var listCategoryName = new Array();
                for (var l = 0; l < listCategoryNameTemp.length; l++)
                {

                        if (listCategoryName.indexOf(listCategoryNameTemp[l]) === -1)
                        {
                                listCategoryName.push(listCategoryNameTemp[l]);
                        }
                }

                //se la lunghezza è maggiore di 0
                if (listCategoryName.length > 0)
                {
                        var request;
                        //per ogni nome effettuera una ricerca
                        for (var i = 0; i < listCategoryName.length; i++)
                        {
                                request = {
                                        location: center,
                                        radius: '1000',
                                        keyword: listCategoryName[i]
                                };
                                service.nearbySearch(request, showMarker);
                        }
                }

        }

        //set cookie di checked che dura 10minuti
        setCookieInMinute('notifica', 1, -1);
}


/**
 * stampa l'indicatore su mappa
 * @param {type} results
 * @param {type} status
 * @returns {undefined}
 */
function showMarker(results, status) {
        if (status === google.maps.places.PlacesServiceStatus.OK)
        {
                for (var i = 0; i < results.length; i++)
                {
                        createMarker(results[i]);
                }
        }
}

/*
 * creare singolo l'indicatore
 * @param {type} place
 * @returns {undefined}
 */
function createMarker(place) {
        var marker = new google.maps.Marker({
                map: map,
                position: place.geometry.location,
                animation: google.maps.Animation.DROP
        });

        google.maps.event.addListener(marker, 'click', function () {
                infoWindow.setContent("<p><b>" + place.name + "</b></p><p>" + place.vicinity + "</p>");
                infoWindow.open(map, this);
        });
}



function errorHandler(error) {

        var text = document.getElementById("errText");
        switch (error.code)
        {
                case error.PERMISSION_DENIED:
                        text.innerHTML = "User denied the request for Geolocation.";
                        break;
                case error.POSITION_UNAVAILABLE:
                        text.innerHTML = "Location information is unavailable.";
                        break;
                case error.TIMEOUT:
                        text.innerHTML = "The request to get user location timed out.";
                        break;
                case error.UNKNOWN_ERR:
                        text.innerHTML = "An unknown error occurred.";
                        break;
        }
}



/**
 * validatore del form di sharing, che controlla se esiste davvero tale email in DB
 * @returns {Boolean}
 */
function validateSharing() {

        //get input del form
        var email = $("#inputEmail").val();


        //nasconde tutti gli errori
        $(".sharing-body .error-messages p").hide();
        //rimuove tutti classe border-danger da inpu di form
        $("#inputEmail").removeClass("border-danger");


        //check esistenza di email

        var url = location.href;
        var index = url.indexOf("mylist");
        url = url.substring(0, index);
        var repeat;
        $.ajax({
                url: url + "service/checkUserService",
                data: {action: "existence", email: email},
                type: 'POST',
                dataType: "text",
                async: false,
                cache: false,
                error: function () {
                        alert('error to check email repeat, retry submit');
                },
                success: function (data) {
                        repeat = data;
                }
        });

        if (repeat === "0")
        {
                var errorType = ".no-exist";
                $(".sharing-body .error-messages " + errorType).show("slow");
                $("#inputEmail").addClass("border-danger");
                $("#inputEmail").focus();
                return false;
        }

        return true;
}



