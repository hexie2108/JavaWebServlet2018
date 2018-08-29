$(document).ready(function () {


        $('#mobile-menu-active-link').click(function () {
                if ($(this).hasClass("actived"))
                {
                        $(this).removeClass("actived");
                }
                else
                {
                        $(this).addClass("actived");
                }
                if ($(".admin-page .admin-menu").hasClass("show-menu"))
                {
                        $(".admin-page .admin-menu").removeClass("show-menu");
                }
                else
                {
                        $(".admin-page .admin-menu").addClass("show-menu");
                }

        });


});


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
 * funziona che cambia la lingua di visualizzazione  e aggiorna la pagina
 * @param {type} language
 * @returns {undefined}
 */
function changeLanguage(language){
        setCookie('language',language,30);
        location.reload();
}
