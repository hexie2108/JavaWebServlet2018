
/**
 * script per le pagine di userSystem
 * @type type
 */

$(document).ready(function () {

        //start code per pop di suggerimenti 
        $('#form-register [data-toggle="popover"]').popover();
        //visualizza la barra della valutazione di password
        $('#form-register #inputPassword').focusin(function () {
                $(".progress-bar-div").show("slow");
        });
        //nasconde la barra della valutazione di password
        $('#form-register #inputPassword').focusout(function () {
                $(".progress-bar-div").hide("slow");
        });


        //valuta in tempo reale il punteggio di password
        $('#form-register #inputPassword').on("keyup", function () {


                var score = (zxcvbn(this.value).score);
                var progressBar = $(".progress-bar");
                switch (score)
                {
                        case 0:
                                progressBar.removeClass();
                                progressBar.addClass("progress-bar progress-bar-striped progress-bar-animated password0 bg-secondary");
                                break;
                        case 1:
                                progressBar.removeClass();
                                progressBar.addClass("progress-bar progress-bar-striped progress-bar-animated password1 bg-danger");
                                break;
                        case 2:
                                progressBar.removeClass();
                                progressBar.addClass("progress-bar progress-bar-striped progress-bar-animated password2 bg-warning");
                                break;
                        case 3:
                                progressBar.removeClass();
                                progressBar.addClass("progress-bar progress-bar-striped progress-bar-animated password3 bg-info");
                                break;
                        case 4:
                                progressBar.removeClass();
                                progressBar.addClass("progress-bar progress-bar-striped progress-bar-animated password4 bg-success");
                                break;
                }

        });




        //visualizza  il uploader di custom avatar
        $("#form-register  #avatar-custom").click(function () {
                $(".custom-avatar-uploader").show("slow");
                $(".custom-file-input").attr("required", "required");
        });

        // nasconde il uploade e clear input di file e ripristina il segnaposto predefinito
        $("#form-register  .default-avatar").click(function () {
                $(".custom-avatar-uploader").hide("slow");
                $(".custom-file-input").val("");
                $(".custom-file-input").removeAttr("required");
                $(".custom-file-label").html($(".custom-file-label-origin").html());
        });

        //elimina gli spazi di input
        $(".input-group input.input-box").change(function () {
                $(this).val($.trim($(this).val()));
        });

        //visualizza il nome file nel cutom-file-input di form
        $(".custom-file-input").on("change", function () {
                //get il nome di file
                var fileName = $(this)[0].files[0].name;
                //sostituisce il contenuto del "custom-file-label" label
                $(this).next(".custom-file-label").html(fileName);

        });
});
