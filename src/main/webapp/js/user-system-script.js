/**
 * validatore del form di registrazione
 * @returns {Boolean} true se valido , false se non è valido
 */

function validateForm() {

    //get input del form
    var email = $("#inputEmail").val();
    var firstName = $("#inputFirstName").val();
    var lastName = $("#inputLastName").val();
    var password = $("#inputPassword").val();
    var password2 = $("#inputPassword2").val();
    var avatar = $('input[name=Avatar]:checked').val();
    var customAvatarFile;
    if (avatar === "custom") {
        customAvatarFile = $("#customAvatarImg");
    }

    //nasconde tutti gli errori
    $("#form-register .error-messages p").hide();
    //rimuove tutti classe border-danger da inpu di form
    $("#form-register .input-box").removeClass("border-danger");


    //check email
    //espressione per controllare il formatto di email
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    //se è vuoto o se supera la lunghezza massima o non rispetta il formatto di email
    if (email === "" || email.length > 44 || !reg.test(email)) {
        var errorType = "null";
        if (email === "") {
            errorType = ".null";
        }
        else if (email.length > 44) {
            errorType = ".max-length";
        }

        else if (!reg.test(email)) {
            errorType = ".invalid";
        }

        $(".email-group .error-messages " + errorType).show("slow");
        $(".email-group .input-box").addClass("border-danger");
        $(".email-group #inputEmail").focus();
        return false;
    }
    //altrimenti controlla se email è ripetuto
    else {
        var url = location.href;
        var index = url.indexOf("register");
        url = url.substring(0, index);
        var repeat;
        $.ajax({
            url: url + "service/checkExistenceOfEmailService",
            data: {email: email},
            type: 'POST',
            dataType: "text",
            async: false,
            cache: false,
            error: function () {
                alert('error to check email repeat, retry submit' + url);
            },
            success: function (data) {
                repeat = data;
            }
        });

        if (repeat === "1") {
            var errorType = ".repeat";
            $(".email-group .error-messages " + errorType).show("slow");
            $(".email-group .input-box").addClass("border-danger");
            $(".email-group #inputEmail").focus();
            return false;
        }
    }


    //check first-name
    //se è vuoto o se supera la lunghezza massima
    if (firstName === "" || firstName.length > 44) {
        var errorType = "null";
        if (firstName === "") {
            errorType = ".null";
        }
        else if (firstName.length > 44) {
            errorType = ".max-length";
        }

        $(".first-name-group .error-messages " + errorType).show("slow");
        $(".first-name-group .input-box").addClass("border-danger");
        $(".first-name-group #inputFirstName").focus();
        return false;
    }

    //check last-name
    //se è vuoto o se supera la lunghezza massima
    if (lastName === "" || lastName.length > 44) {
        var errorType = "null";
        if (lastName === "") {
            errorType = ".null";
        }
        else if (lastName.length > 44) {
            errorType = ".max-length";
        }

        $(".last-name-group .error-messages " + errorType).show("slow");
        $(".last-name-group .input-box").addClass("border-danger");
        $(".last-name-group #inputLastName").focus();
        return false;
    }

    //check password
    //se è vuoto o se supera la lunghezza massima o se è troppo corta
    if (password === "" || password.length > 44 || password.length < 8) {
        var errorType = "null";
        if (password === "") {
            errorType = ".null";
        }
        else if (password.length > 44) {
            errorType = ".max-length";
        }
        else if (password.length < 8) {
            errorType = ".min-length";
        }

        $(".password-group .error-messages " + errorType).show("slow");
        $(".password-group .input-box").addClass("border-danger");
        $(".password-group #inputPassword").focus();
        return false;
    }
    //altrimenti, controlla se password è valido
    else {

        var lower = 0;
        var lowerRegx = /^[a-z]*$/;
        var upper = 0;
        var upperRegx = /^[A-Z]*$/;
        var number = 0;
        var numberRegx = /^[0-9]*$/;
        var symbol = 0;

        var letter;

        for (var i = 0; i < password.length; i++) {
            letter = password.charAt(i);

            if (lowerRegx.test(letter)) {
                lower++;
            }
            else if (upperRegx.test(letter)) {
                upper++;
            }
            else if (numberRegx.test(letter)) {
                number++;
            }
            else if ((letter !== " ") && (letter !== "\t") && (letter !== "\n")) {
                symbol++;
            }
        }

        //se password non ha un carattere minuscolo, un maiuscolo, un numero e un simbolo, è invalido
        if ((lower < 1) || (upper < 1) || (number < 1) || (symbol < 1)) {
            var errorType = ".invalid";
            $(".password-group .error-messages " + errorType).show("slow");
            $(".password-group .input-box").addClass("border-danger");
            $(".password-group #inputPassword").focus();

            return false;
        }

    }
    //check password2
    //se è vuoto o è diverso da password
    if (password2 === "" || password2 !== password) {
        var errorType = "null";
        if (password2 === "") {
            errorType = ".null";
        }
        else if (password2 !== password) {
            errorType = ".no-equal";
        }

        $(".password2-group .error-messages " + errorType).show("slow");
        $(".password2-group .input-box").addClass("border-danger");
        $(".password2-group #inputPassword2").focus();
        return false;
    }


    //check avatar
    //array di avatar
    var arrayOfDefaultAvatar = ["user.svg", "user-astronaut.svg", "user-ninja.svg", "user-secret.svg", "custom"];
    //se è vuoto o è diverso da password
    if (avatar === undefined || arrayOfDefaultAvatar.indexOf(avatar) === -1) {
        var errorType = "null";
        if (avatar === undefined) {
            errorType = ".null";
        }
        else if (arrayOfDefaultAvatar.indexOf(avatar) === -1) {
            errorType = ".invalid";
        }

        $(".avatar-group .error-messages " + errorType).show("slow");
        $(".avatar-group #avatar-0").focus();
        return false;
    }


    //check caso del custom avatar uploader
    if (avatar === "custom") {

        //se custom avatar file è vuoto o file è minore di 0 o supera 15MB
        if (customAvatarFile.val() === "" || customAvatarFile[0].files[0].size <= 0 || customAvatarFile[0].files[0].size > (15 * 1000000)) {
            var errorType = "null";
            if (customAvatarFile.val() === "") {
                errorType = ".null";
            }
            else if (customAvatarFile[0].files[0].size <= 0) {
                errorType = ".min-length";
            }
            else if (customAvatarFile[0].files[0].size > (15 * 1000000)) {
                errorType = ".max-length";
            }

            $(".custom-avatar-uploader .error-messages " + errorType).show("slow");
            $(".custom-avatar-uploader .input-box").addClass("border-danger");
            $(".custom-avatar-uploader #customAvatarImg").focus();

            return false;

        }
        //altrimenti controlla l'estensione del file
        else {

            var arrayOfExtension = ["jpg", "jpeg", "png", "gif", "bmp"];
            var fileName = customAvatarFile[0].files[0].name.toLowerCase();
            var extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (arrayOfExtension.indexOf(extension) === -1) {
                var errorType = ".invalid";
                $(".custom-avatar-uploader .error-messages " + errorType).show("slow");
                $(".custom-avatar-uploader .input-box").addClass("border-danger");
                $(".custom-avatar-uploader #customAvatarImg").focus();
                return false;
            }

        }
    }

    return true;
}

/**
 * rimuove class border-danger
 * @param {type} object
 * @returns {undefined}
 */
function removeBorderColor(object) {
    object.removeClass("border-danger");
}

/**
 * aggiunge class border-danger
 * @param {type} object
 * @returns {undefined}
 */
function addBorderColor(object) {
    object.addClass("border-danger");
}


/**
 * nasconde tutti errori
 * @param {type} object
 * @returns {undefined}
 */
function hideError(object) {
    object.hide();
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
    });


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
    $(".custom-file-input").on("change", function () {
        //get il nome di file
        var fileName = $(this)[0].files[0].name;
        //sostituisce il contenuto del "custom-file-label" label
        $(this).next(".custom-file-label").html(fileName);

    });


    //visualizza  il uploader di custom avatar
    $("#avatar-custom").click(function () {
        $(".custom-avatar-uploader").show("slow");
        $(".custom-file-input").attr("required", "required");
    });

    // nasconde il uploade e clear input di file e ripristina il segnaposto predefinito
    $(".default-avatar").click(function () {
        $(".custom-avatar-uploader").hide("slow");
        $(".custom-file-input").val("");
        $(".custom-file-input").removeAttr("required");
        $(".custom-file-label").html($(".custom-file-label-origin").html());
    });

    //elimina gli spazi di input
    $(".input-group input.input-box").change(function () {
        $(this).val($.trim($(this).val()));
    });
});
