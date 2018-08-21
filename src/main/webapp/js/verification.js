"use strict";

function showErrorAlert(title, message, closeLabel) {
    const x = $(
        '<div class="modal fade modal-danger" id="errorAlertBox" role="dialog">' +
        '     <div class="modal-dialog">' +
        '        <div class="modal-content">' +
        '            <div class="modal-header">' +
        '                <h4 class="modal-title"><i class="fas fa-exclamation-triangle"></i> ' + title + '</h4>' +
        '                <button type="button" class="close" data-dismiss="modal">&times;</button>' +
        '            </div>' +
        '            <div class="modal-body">' + message +'</div>' +
        '            <div class="modal-footer">' +
        '                <button type="button" class="btn btn-secondary" data-dismiss="modal">' + closeLabel + '</button>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>'
    );

    x.modal('show');
    x.on('hidden.bs.modal', function(){
        x.modal('dispose');
    });
}

function updateVerifyMessages(form, data) {
    // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
    const inputs = form.find('input,textarea,select').map(function () {
        return this.name;
    }).get();
    // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
    const validityInputs = inputs.map(
        function (key) {
            const input = form.find("[name=\"" + key + "\"]");
            const span = form.find("#span" + key);

            span.html("");

            if (data.hasOwnProperty(key)) {
                span.html("<i class=\"fas fa-exclamation-triangle\"></i> " + String(data[key]));
                input.closest('.input-group').find('.input-group-prepend, .input-group-append').find('.input-group-text').addClass('border-danger');
                input.addClass("is-invalid");
                span.show("slow");

                return false;
            }

            input.closest('.input-group').find('.input-group-prepend, .input-group-append').find('.input-group-text').removeClass('border-danger');
            input.removeClass("is-invalid");
            return true;
        }
    );

    // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
    // Se false l'invio del form verrà bloccato altrimenti no
    return validityInputs.every(v => v);
}

function clearVerifyMessages(form) {
    // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
    const inputs = form.find('input,textarea,select').map(function () {
        return this.name;
    }).get();
    // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
    const validityInputs = inputs.map(
        function (key) {
            const input = form.find("[name=\"" + key + "\"]");
            const span = form.find("#span" + key);

            span.html("");
            input.closest('.input-group').find('.input-group-prepend, .input-group-append').find('.input-group-text').removeClass('border-danger');
            input.removeClass("is-invalid");
            return true;
        }
    );

    // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
    // Se false l'invio del form verrà bloccato altrimenti no
    return validityInputs.every(v => v);
}

function resetAlert(alert) {
    alert.removeClass('alert-danger');
    alert.removeClass('alert-success');
    alert.removeClass('alert-warning');

    alert.addClass("d-none");
}

function formSubmit(url, form, options) {
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

function add_file_errors(contentTypeRegex, maxSize, required, errors) {
    let fileEmptyOrNull = errors.fileEmptyOrNull;
    let fileTooBig = errors.fileTooBig;
    let fileContentTypeMissingOrType = errors.fileContentTypeMissingOrType;
    let fileOfWrongType = errors.fileOfWrongType;

    if ( fileEmptyOrNull === undefined){
        console.error('errors.fileEmptyOrNull is null/undefined/empty');
        return;
    }

    if ( fileTooBig === undefined) {
        console.error('errors.fileTooBig is null/undefined/empty');
        return;
    }

    if ( fileContentTypeMissingOrType === undefined) {
        console.error('errors.fileContentTypeMissingOrType is null/undefined/empty');
        return;
    }

    if ( fileOfWrongType === undefined) {
        console.error('errors.fileOfWrongType is null/undefined/empty');
        return;
    }

    if  (typeof required !== 'function' ) {
        console.error('required must be a function');
        return;
    }

    if (!(contentTypeRegex instanceof RegExp)) {
        console.error('contentTypeRegex must be a regex');
        return;
    }

    return function(data, form, name){
        // Se l'estensione per leggere i file è supportata faccio il controllo altrimenti no
        // (fatto successivamente dal server)
        if (!window.FileReader) {
            return data;
        }

        const input = form.find('[type="file"][name="' + name + '"]');

        // Se il browser ha l'estensione che permette di accedere alla proprietà files continuo altrimenti no
        // (fatto successivamente dal server)
        if (!input[0].files) {
            return data;
        }

        const fileToUpload = input[0].files[0];

        if (!fileToUpload) {
            if (required(form, name)) {
                data[name] = fileEmptyOrNull;
            }
        } else if (fileToUpload.size > maxSize) {
            data[name] = fileTooBig;
        } else if (window.Blob) {
            if (!fileToUpload.type || !fileToUpload.type.trim()) {
                data[name] = fileContentTypeMissingOrType;
            } else if (!(contentTypeRegex.test(fileToUpload.type))) {
                data[name] = fileOfWrongType; // '<fmt:message key="validateCategoryProduct.errors.Img.FILE_OF_WRONG_TYPE"/>';
            }
        }

        return data;
    }
}

function validateString(maxLen, required, errors) {
    const emptyOrNull = errors.emptyOrNull;
    const tooLong = errors.tooLong;

    if (emptyOrNull === undefined) {
        console.error("errors.emptyOrNull undefined");
        return;
    }

    if (tooLong === undefined) {
        console.error("errors.tooLong undefined");
        return;
    }

    if  (typeof required !== 'function' ) {
        console.error('required must be a function');
        return;
    }

    return function(obj, form, name) {
        const str = form.find('[name="' + name + '"]').val();
        if (!str) {
            if (required(form, name)) {
                obj[name] = emptyOrNull;
            }
        } else if (str.length > maxLen) {
            obj[name] = tooLong;
        }
    }
}

