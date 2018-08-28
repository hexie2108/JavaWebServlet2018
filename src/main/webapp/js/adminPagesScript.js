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