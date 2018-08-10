function validateForm() {

        
return false;


}



$(document).ready(function () {

        //start code per pop di suggerimenti 
        $('[data-toggle="popover"]').popover();

        //visualizza la barra della valutazione di password
        $('#inputPassword').focusin(function () {
                $(".progress-bar-div").show("slow");
        });
        //nasconde la barra della valutazione di password
        $('#inputPassword').focusout(function () {
                $(".progress-bar-div").hide("slow");
        })


        //valuta in tempo reale il punteggio di password
        $('#inputPassword').on("keyup", function () {


                var score = (zxcvbn(this.value).score);
                var progressBar = $(".progress-bar");
                switch (score) {
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



        //visualizza il nome file nel cutom-file-input di form
        $(".custom-file-input").on("change", function (e) {
                //get il nome di file
                var fileName = e.target.files[0].name;
                //sostituisce il contenuto del "custom-file-label" label
                $(this).next(".custom-file-label").html(fileName);
        });



        //visualizza e nasconde il uploader di custom avatar
        $("#avatar-custom").click(function () {
                $(".custom-avatar-uploader").show("slow");
        });
        $(".default-avatar").click(function () {
                $(".custom-avatar-uploader").hide("slow");

        });

});
