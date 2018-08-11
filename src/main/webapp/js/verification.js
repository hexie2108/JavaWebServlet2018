"use strict";

function updateVerifyMessages(form, data) {
    // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
    const inputs = form.find('input,textarea,select').map(function () {
        return this.name;
    }).get();
    // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
    const validityInputs = inputs.map(
        function (key) {
            const input = form.find("#input" + key);
            const span = form.find("#span" + key);

            if (data.hasOwnProperty(key)) {
                input.addClass("is-invalid");
                span.addClass("invalid-feedback");
                span.html(String(data[key]));

                return false;
            }

            input.removeClass("is-invalid");
            span.removeClass("invalid-feedback");
            span.html("");
            return true;
        }
    );

    // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
    // Se false l'invio del form verrà bloccato altrimenti no
    return validityInputs.every(v => v);
}

function formSubmit(url, form, options) {
    function resetAlert(alert) {
        alert.removeClass('alert-danger');
        alert.removeClass('alert-success');
        alert.removeClass('alert-warning');

        alert.addClass("d-none");
    }

    const multipart = options['multipart'];
    const session = options['session'];
    const redirectUrl = options['redirectUrl'];
    const unknownErrorMessage = options['unknownErrorMessage'];
    const successMessage = options['successMessage'];
    const resDiv = options['resDiv'];
    const successCallback = options['successCallback'];

    const req = {
        dataType: "json",
        url: url,
        type: "POST",
        async: false,
    };

    if (multipart === true) {
        req.data = new FormData(form[0]);
        req.processData = false;
        req.contentType = false;
        req.cache = false;
    } else {
        req.data = form.serialize();
    }

    if (session === true) {
        req.xhrFields = {withCredentials: true};
    }

    const rq = $.ajax(req);

    form.find('[type=submit]').attr("disabled", true);

    rq.done(function (data) {
        resetAlert(resDiv);

        if (redirectUrl) {
            window.location.href = redirectUrl;
        } else if (successMessage) {
            resDiv.addClass("alert-success");
            resDiv.html(successMessage);
            resDiv.removeClass("d-none");
        }

        if (successCallback !== undefined) {
            successCallback();
        }
    }).fail(function (jqXHR) {
        resetAlert(resDiv);

        resDiv.addClass("alert-danger");

        if (typeof jqXHR.responseJSON === 'object' &&
            jqXHR.responseJSON !== null &&
            jqXHR.responseJSON['message'] !== undefined
        ) {
            if (jqXHR.responseJSON['message'] === "ValidationFail") {
                jqXHR.responseJSON['message'] = undefined;
                updateVerifyMessages(form, jqXHR.responseJSON);
            } else {
                resDiv.html(jqXHR.responseJSON['message']);
                resDiv.removeClass("d-none");
            }
        } else {
            resDiv.html(unknownErrorMessage);
            resDiv.removeClass("d-none");
        }
    }).always(function () {
        form.find('[type=submit]').attr("disabled", false);
    });
}




