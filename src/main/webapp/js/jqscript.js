

$(document).ready(function () {

        show_message_modal();

        //visualizza il nome file nel cutom-file-input di form
        $(".custom-file-input").on("change", function (e) {
                //get il nome di file
                var fileName = e.target.files[0].name;
                //sostituisce il contenuto del "custom-file-label" label
                $(this).next(".custom-file-label").html(fileName);
        });

        $('.selectpicker').selectpicker({
                style: 'btn-info',
                countSelectedText: '{0}/{1} selected',
                noneResultsText: 'No result matched {0}'
        });

        $('#mobile-menu-active-link').click(function () {
                if ($(this).hasClass("actived"))
                {
                        $(this).removeClass("actived");
                }
                else
                {
                        $(this).addClass("actived");
                }
                if ($(".front-page header .menu-nav .navbar-nav .nav-item.desktop-item").hasClass("show-item"))
                {
                        $(".front-page header .menu-nav .navbar-nav .nav-item.desktop-item").removeClass("show-item");
                }
                else
                {
                        $(".front-page header .menu-nav .navbar-nav .nav-item.desktop-item").addClass("show-item");
                }
               
        });



});



/**
 * visualizza finestrina di message automatico
 * @returns {undefined}
 */
function show_message_modal() {
        $('#boxShowMessage').modal('show');
}

