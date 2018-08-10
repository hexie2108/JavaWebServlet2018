
//visualizza finestrina di message automatico
function show_message_modal() {
        $('#boxShowMessage').modal('show');
}

$(document).ready(function () {

        show_message_modal();

        //visualizza il nome file nel cutom-file-input di form
        $(".custom-file-input").on("change", function (e) {
                //get il nome di file
                var fileName = e.target.files[0].name;
                //sostituisce il contenuto del "custom-file-label" label
                $(this).next(".custom-file-label").html(fileName);
        });





});

