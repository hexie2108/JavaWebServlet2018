"use strict";

// Extend jQuery objects functions to be able to use a donetyping "event"
(function($){
    $.fn.extend({
        donetyping: function(callback,timeout){
            timeout = timeout || 4e2; // 1 second default timeout
            let timeoutReference;
            const doneTyping = function(el){
                if (!timeoutReference) {
                    return;
                }

                timeoutReference = null;
                callback.call(el);
            };

            return this.each(function(i,el){
                const $el = $(el);
                $el.is(':input') && $el.on('keyup keydown keypress paste',function(e){
                    // This catches the backspace button in chrome, but also prevents
                    // the event from triggering too preemptively. Without this line,
                    // using tab/shift+tab will make the focused element fire the callback.
                    if (e.type=='keyup' && e.keyCode!=8) return;

                    // Check if timeout has been set. If it has, "reset" the clock and
                    // start over again.
                    if (timeoutReference) {
                        clearTimeout(timeoutReference);
                    }

                    timeoutReference = setTimeout(function(){
                        // if we made it here, our timeout has elapsed. Fire the callback
                        doneTyping(el);
                    }, timeout);
                }).on('change blur',function(){
                    // If we can, fire the event since we're leaving the field
                    doneTyping(el);
                });
            });
        }
    });
})(jQuery);

/**
 * Module that can be used to dinamically generate a modal to use as an alert for an error or a successful operation
 * @type {{error, success}}
 */
const modalAlert = (function(){

    // Function to show a modal with a specific type and content
    function show(type, title, message, closeLabel) {
        const x = $(
            '<div class="modal fade modal-'+ type + ' modal-danger" id="errorAlertBox" role="dialog">' +
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


    /**
     * Function to show a error modal
     * @param {!string} title title of the modal
     * @param {!string} message main content of the modal
     * @param {!string} closeLabel string to use as close button
     */
    function error(title, message, closeLabel) {
        show('danger', title, message, closeLabel);
    }

    // Function to show a success modal
    /**
     * Function to show a success modal
     * @param {!string} title title of the modal
     * @param {!string} message main content of the modal
     * @param {!string} closeLabel string to use as close button
     */
    function success(title, message, closeLabel) {
        show('success', title, message, closeLabel);
    }

    return {
        error: error,
        success: success
    };
})();

/**
 * Module to update info about validation and general validation
 * @type {{updateVerifyMessages, clearVerifyMessages, formSubmitWithValidation, validateFile, validateString, user}}
 */
const validationUtils = (function(){
    function checkObject(obj, ar, prefix) {
        return $.map(ar, function(key){
            if (obj[key] === undefined) {
                console.error(prefix + '.' + key + ' is undefined');
            }

            return obj[key] === undefined;
        }).some(function(v){return v;});
    }

    /**
     * Function to update the validation messages in a form given the data that contains the new validation mesages
     * @param {jQuery} form jQuery object that represents the form
     * @param {Object.<string, string>} data object that is mapped as key(input name) and message(validation error)
     * @returns {Boolean} false if there are errors, true otherwise
     */
    function updateVerifyMessages(form, data) {
        // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
        const inputs = $('input, textarea, select', form).map(function () {
            return this.name;
        }).get();
        // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
        const validityInputs = inputs.map(
            function (key) {
                const input = $('[name="' + key + '"]', form);
                const span = $('#span' + key, form);

                if (data.hasOwnProperty(key)) {
                    // If data has a key that is the same as the name of the input update the validation message

                    // If the content is the same as the new content do nothing, otherwise update
                    const toInsert = "<i class=\"fas fa-exclamation-triangle\"></i> " + String(data[key]);
                    if(toInsert !== span.html()) {
                        // Hide the validation message using a fast animation and finished the animation update the
                        // message and mark the input as invalid
                        span.hide('fast', function(){
                            // Update the text of the validation message
                            span.html("<i class=\"fas fa-exclamation-triangle\"></i> " + String(data[key]));

                            // Show all the borders in red(input-group-prepend, input-group-append and the input)
                            input.closest('.input-group').find('.input-group-prepend, .input-group-append').find('.input-group-text').addClass('border-danger');
                            input.addClass("is-invalid");

                            // Show the validation message with a slow animation
                            span.show("slow");
                        });
                    }

                    // Return false as we found a valid error
                    return false;
                }

                // If data hasn't a key that is the same as the name of the input hide the validation message
                // with a fast animation and then delete the message
                span.hide('fast', function(){
                    span.html('');
                });

                // Remove red borders
                input.closest('.input-group').find('.input-group-prepend, .input-group-append').find('.input-group-text').removeClass('border-danger');
                input.removeClass("is-invalid");

                // Return true as we didn't found a valid error
                return true;
            }
        );

        // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
        // Se false l'invio del form verrà bloccato altrimenti no
        return validityInputs.every(function(v){return v;});
    }

    /**
     * Funcion to clear the validation messages in a form
     * @param {!jQuery} form form of which we want to clear the validation messages
     */
    function clearVerifyMessages(form) {
        // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
        const inputs = $('input, textarea, select', form).map(function () {
            return this.name;
        }).get();
        // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
        const validityInputs = inputs.map(
            function (key) {
                const input = $('[name="' + key + '"]', form);
                const span = $("#span" + key, form);

                // Remove content of the validation message, remove all red borders
                span.html("");
                input.closest('.input-group').find('.input-group-prepend, .input-group-append').find('.input-group-text').removeClass('border-danger');
                input.removeClass("is-invalid");
            }
        );
    }

    /**
     * @typedef {Object} modalAlertOptions
     * @property {!string} title string to use as title
     * @property {!string} message main content of the modal
     * @property {!string} closeLabel label to use in the close button
     */

    /**
     * @typedef {Object} formOptions
     * @property {Boolean} multipart true to send using enctype/multipart, otherwise it uses default
     * @property {Boolean} session send cookies
     * @property {string} redirectUrl redirect to specific url after success(if not defined successAlert must be defined and vice versa)
     * @property {modalAlertOptions} successAlert settings for the success modal(if not defined redirectUrl must be defined and vice versa)
     * @property {modalAlertOptions} failAlert settings for the error modal (if error message form the post is standard the main content of the modal is overwritten)
     */

    /**
     *
     * @param {!string} url url to use during post
     * @param {!jQuery} form form to use(submit and update validation)
     * @param {!formOptions} options
     */
    function formSubmitWithValidation(url, form, options) {
        const multipart = options['multipart'];
        const session = options['session'];
        const redirectUrl = options['redirectUrl'];
        const successAlert = options['successAlert'];
        const failAlert = options['failAlert'];
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
            if (redirectUrl) {
                window.location.href = redirectUrl;
            } else if (successAlert['title'] && successAlert['message']) {
                modalAlert.success(successAlert['title'], successAlert['message'], successAlert['closeLabel']);
            }

            if (successCallback !== undefined) {
                successCallback();
            }
        }).fail(function (jqXHR) {
            if (typeof jqXHR.responseJSON === 'object' &&
                jqXHR.responseJSON !== null &&
                jqXHR.responseJSON['message'] !== undefined
            ) {
                if (jqXHR.responseJSON['message'] === "ValidationFail") {
                    jqXHR.responseJSON['message'] = undefined;
                    validationUtils.updateVerifyMessages(form, jqXHR.responseJSON);
                } else {
                    modalAlert.error(failAlert['title'], jqXHR.responseJSON['message'], failAlert['closeLabel']);
                }
            } else {
                modalAlert.error(failAlert['title'], failAlert['message'], failAlert['closeLabel']);
            }
        }).always(function () {
            form.find('[type=submit]').attr("disabled", false);
        });
    }

    /**
     * @typedef {Function} RequiredLambdaType
     * @param {!jQuery} form jQuery object that represents the form
     * @param {!string} name name of the input considered
     * @return {!Boolean} true if the input is required, false otherwise
     */

    /**
     * @typedef {Function} ValidationCheckLambda
     * @param {!Object} data object in which the function saves the errors found
     * @param {!jQuery} form form to consider
     * @param {...string} name names of the input used
     * @return {!Object} returns data
     */

    /**
     * @typedef {Object} ValidateFileErrors
     * @param {!string} fileEmptyOrNull message to show if file is empty or null
     * @param {!string} fileTooBig message to show if file is too big
     * @param {!string} fileContentTypeMissingOrType message to show if contentType is null or empty
     * @param {!string} fileOfWrongType message to show if contentType doesn't respects contentTypeRegex
     */

    /**
     * Function to create a custom validator for an input file
     * @param {RegExp} contentTypeRegex regex that the contentType must match
     * @param {Number} maxSize maximum size that the file must have
     * @param {RequiredLambdaType} required function that returns true if the object is to be considered required, false otherwise
     * @param {ValidateFileErrors} errors
     * @returns {ValidationCheckLambda|undefined} undefined if an error happens, ValidationCheckLambda otherwise
     */
    function validateFile(contentTypeRegex, maxSize, required, errors) {
        const errors_args = ['fileEmptyOrNull', 'fileTooBig', 'fileContentTypeMissingOrType', 'fileOfWrongType'];
        if (checkObject(errors, errors_args, 'errors')) {
            return;
        }

        const fileEmptyOrNull = errors.fileEmptyOrNull;
        const fileTooBig = errors.fileTooBig;
        const fileContentTypeMissingOrType = errors.fileContentTypeMissingOrType;
        const fileOfWrongType = errors.fileOfWrongType;

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

            const input = $('[type="file"][name="' + name + '"]', form);

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
                    data[name] = fileOfWrongType;
                }
            }

            return data;
        }
    }

    /**
     * @typedef {Object} ValidateStringErrors
     * @param {!string} emptyOrNull message to show if input value is empty or null
     * @param {!string} tooLong message to show if input value is too big
     */

    /**
     * Function to create a custom validator for an text input
     * @param {Number} maxLen maximum length that the input value must have
     * @param {RequiredLambdaType} required function that returns true if the object is to be considered required, false otherwise
     * @param {ValidateStringErrors} errors
     * @returns {ValidationCheckLambda|undefined} undefined if an error happens, ValidationCheckLambda otherwise
     */
    function validateString(maxLen, required, errors) {
        if (checkObject(errors, ['emptyOrNull', 'tooLong'], 'errors')) {
            return;
        }

        const emptyOrNull = errors.emptyOrNull;
        const tooLong = errors.tooLong;

        if  (typeof required !== 'function' ) {
            console.error('required must be a function');
            return;
        }

        return function(obj, form, name) {
            const str = $('[name="' + name + '"]', form).val();
            if (!str) {
                if (required(form, name)) {
                    obj[name] = emptyOrNull;
                }
            } else if (str.length > maxLen) {
                obj[name] = tooLong;
            }
        }
    }

    const user = (function(){
        /**
         * @typedef {Object} ValidatePasswordOptions
         * @property {!Number} minLength minimum length for the password
         * @property {!Number} maxLength maximum length for the password
         * @property {!Number} minLower minimum number of lowercase characters in the password
         * @property {!Number} minUpper minimum number of uppercase characters in the password
         * @property {!Number} minDigits minimum number of digit characters in the password
         * @property {!Number} minLower minimum number of symbol characters in the password
         */

        /**
         * @typedef {Object} ValidatePasswordErrors
         * @property {string} passwordMissingOrEmpty message to show if password is missing or empty
         * @property {string} passwordTooLong message to show if password is too long
         * @property {string} passwordTooShort message to show if password is too short
         * @property {string} passwordInvalid message to show if password is invalid(not enough lowercase, uppercase, digits, symbols)
         */

        /**
         * Function to validate a password
         * @param {!RequiredLambdaType} required function that returns true if the object is to be considered required, false otherwise
         * @param {!ValidatePasswordOptions} options options for the validation
         * @param {!ValidatePasswordErrors} errors strings to use as errors
         * @returns {ValidationCheckLambda|undefined} undefined if an error happens, ValidationCheckLambda otherwise
         */
        function validatePassword(required, options, errors) {

            const options_args = ['minLength', 'maxLength', 'minLower', 'minUpper', 'minDigits', 'minSymbol'];
            if (checkObject(options, options_args, 'options')) {
                return;
            }

            const errors_args = ['passwordMissingOrEmpty','passwordTooLong', 'passwordTooShort', 'passwordInvalid'];
            if (checkObject(errors, errors_args, 'errors')) {
                return;
            }

            const minLength = options.minLength;
            const maxLength = options.maxLength;
            const minLower = options.minLower;
            const minUpper = options.minUpper;
            const minDigits = options.minDigits;
            const minSymbol = options.minSymbol;

            const passwordMissingOrEmpty = errors.passwordMissingOrEmpty;
            const passwordTooLong = errors.passwordTooLong;
            const passwordTooShort = errors.passwordTooShort;
            const passwordInvalid = errors.passwordInvalid;

            return function (obj, form, name) {
                const password = form.find('[name="' + name + '"]').val();

                //check password
                //se è vuoto o se supera la lunghezza massima o se è troppo corta
                if (password === "" || password.trim() === "") {
                    if (required(form, name)) {
                        obj[name] = passwordMissingOrEmpty;
                    }
                } else if (password.length > maxLength) {
                    obj[name] = passwordTooLong;
                } else if (password.length < minLength) {
                    obj[name] = passwordTooShort;
                } else {
                    let lower = 0;
                    let upper = 0;
                    let number = 0;
                    let symbol = 0;

                    for (let i = 0; i < password.length; i++) {
                        const letter = password.charAt(i);

                        if (letter === letter.toLowerCase() && letter !== letter.toUpperCase()) {
                            lower++;
                        } else if (letter !== letter.toLowerCase() && letter === letter.toUpperCase()) {
                            upper++;
                        } else if (/\d/.test(letter)) {
                            number++;
                        } else if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(letter)) {
                            symbol++;
                        }
                    }

                    //se password non ha un carattere minuscolo, un maiuscolo, un numero e un simbolo, è invalido
                    if ((lower < minLower) || (upper < minUpper) || (number < minDigits) || (symbol < minSymbol)) {
                        obj[name] = passwordInvalid;
                    }
                }
            };
        }

        /**
         * @typedef {Object} ValidateEmailErrors
         * @property {string} errorEmptyOrNull message to show if email is null or empty
         * @property {string} tooLong message to show if email is too long
         * @property {string} emailInvalid message to show if email is invalid(format)
         * @property {string} emailNoExists message to show if email doesn't exist
         * @property {string} emailAlreadyActivated message to show if email is already activated
         */

        /**
         * Function to check an email
         * @param {Boolean} lazy true to check that email isn't already used, false otherwise
         * @param {Boolean} existing true if the email should exist, false otherwise
         * @param {!RequiredLambdaType} required function that returns true if the object is to be considered required, false otherwise
         * @param {string} url url to use to check that if the email is already used
         * @param {!ValidateEmailErrors} errors messages to use as errors
         * @returns {ValidationCheckLambda|undefined} undefined if an error happens, ValidationCheckLambda otherwise
         */
        function validateEmail(lazy, existing, required, url, errors) {
            const errors_args = ['emptyOrNull', 'tooLong', 'emailInvalid', 'emailNoExists', 'emailAlreadyActivated'];
            if (checkObject(errors, errors_args, 'errors')) {
                return;
            }

            const emptyOrNull = errors.emptyOrNull;
            const tooLong = errors.tooLong;
            const emailInvalid = errors.emailInvalid;
            const emailNoExists = errors.emailNoExists;
            const emailAlreadyActivated = errors.emailAlreadyActivated;

            return function(obj, form, name) {
                const emailVal = form.find('[name="'+name+'"]').val();

                validateString(name, required, {
                    emptyOrNull,
                    tooLong
                });

                if (!lazy && obj[name] === undefined && emailVal !== '') {
                    //espressione per controllare il formatto di email
                    const reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
                    //se è vuoto o se supera la lunghezza massima o non rispetta il formatto di email
                    if (!reg.test(emailVal)) {
                        obj[name] = emailInvalid;
                    } else {
                        //poi bisogna verificare se tale email esiste o no
                        //e verifica lo stato di attivazione dell'account con tale email

                        //get valore di email
                        $.ajax({
                            url: url,
                            data: {action: "checkActivationStatus", email: emailVal},
                            type: 'POST',
                            dataType: "text",
                            async: false,
                            cache: false,
                        }).done(function (data) {
                            if(existing) {
                                if (data === "0") {
                                    // Se l'email dovrebbe esistere ma non esiste visualizza errore
                                    obj[name] = emailNoExists;
                                }
                            } else {
                                if (data === "1") {
                                    // Se l'email non dovrebbe esistere ma esiste
                                    obj[name] = emailAlreadyActivated;
                                }
                            }
                        });
                    }
                }
            };
        }

        /**
         * @typedef {Object} ValidatePassword2Error
         * @property {string} password2Missing message to show when the second password input has missing or empty value
         * @property {string} password2NotSame message to show if the 2° password is not the same as the 1° one
         */

        /**
         * Function to validate the repeated password
         * @param {!ValidatePasswordErrors} errors messages to show
         * @returns {ValidationCheckLambda|undefined} undefined if an error happens, ValidationCheckLambda otherwise
         */
        function validatePassword2(errors) {
            if (checkObject(errors, ['password2Missing', 'password2NotSame'], 'errors')) {
                return;
            }

            const password2Missing = errors.password2Missing;
            const password2NotSame = errors.password2NotSame;

            return function(obj, form, namePwd1, namePwd2) {
                const password = form.find('[name="' + namePwd1 + '"]').val();
                const password2 = form.find('[name="' + namePwd2 + '"]').val();

                if (password2 === "") {
                    obj[namePwd2] = password2Missing;
                } else if (password2 !== password) {
                    obj[namePwd2] = password2NotSame;
                }
            };
        }

        /**
         * @typedef {Object} ValidateAvatarErrors
         * @property {!string} fileEmptyOrNull message to show if the file is empty or null
         * @property {!string} fileTooBig message to show if the file is too big
         * @property {!string} fileContentTypeMissingOrType message to show if the fiel's contentType is null or empty
         * @property {!string} fileOfWrongType message to show if the file is of the wrong type(wrong contentType)
         * @property {!string} selectValMissing message to show if the select has nothing selected
         * @property {!string} selectValInvalid message to show if the select has an invalid value
         */

        /**
         * Function to validate the avatar
         * @param {!RegExp} regexContentType regex to match contentType
         * @param {!string[]} arrayOfDefaultAvatar values permitted as avatar
         * @param {!Number} maxFileLen maximum size of the file(in bytes)
         * @param {!RequiredLambdaType} required function that returns true if the object is to be considered required, false otherwise
         * @param {ValidateAvatarErrors} errors messages to show
         * @returns {ValidationCheckLambda|undefined} undefined if an error happens, ValidationCheckLambda otherwise
         */
        function validateAvatar(regexContentType, arrayOfDefaultAvatar, maxFileLen, required, errors)  {
            if (maxFileLen === undefined) {
                console.error('maxFileLen is undefined');
                return;
            }
            if (errors === undefined) {
                console.error("errors is undefined");
                return;
            }

            const errors_args = [
                'fileEmptyOrNull',
                'fileTooBig',
                'fileContentTypeMissingOrType',
                'fileOfWrongType',
                'selectValMissing',
                'selectValInvalid'
            ];

            if (checkObject(errors, errors_args, 'errors')) {
                return;
            }

            const fileEmptyOrNull = errors.fileEmptyOrNull;
            const fileTooBig = errors.fileTooBig;
            const fileContentTypeMissingOrType = errors.fileContentTypeMissingOrType;
            const fileOfWrongType = errors.fileOfWrongType;
            const selectValMissing = errors.selectValMissing;
            const selectValInvalid = errors.selectValInvalid;

            const checkFile = validateFile(regexContentType, maxFileLen, (form, name) => true, {
                fileEmptyOrNull,
                fileTooBig,
                fileContentTypeMissingOrType,
                fileOfWrongType,
            });

            return function(obj, form, selectName, fileName) {
                const select = form.find('[name="' + selectName + '"]:checked').val();

                if (select === undefined) {
                    // if selected value is empty
                    if (required(form, name)) {
                        obj[selectName] = selectValMissing;
                    }
                } else if (select === "custom") {
                    // if selected value is custom than check file
                    checkFile(obj, form, fileName);
                } else if (arrayOfDefaultAvatar.indexOf(select) === -1) {
                    // if selected value is not one of the recognized one
                    obj[selectName] = selectValInvalid;
                }
            }
        }

        return {
            validateEmail: validateEmail,
            validateAvatar: validateAvatar,
            validatePassword: validatePassword,
            validatePassword2: validatePassword2
        };
    })();

    return {
        updateVerifyMessages: updateVerifyMessages,
        clearVerifyMessages: clearVerifyMessages,
        formSubmitWithValidation: formSubmitWithValidation,
        validateFile: validateFile,
        validateString: validateString,
        user
    };
})();

/**
 * Module in which utils for forms are contained
 * @type {{timedChange}}
 */
const formUtils = (function(){
    /**
     * Function to call a callback when the user is not writing in the writing for timeout or change a mouse-based input
     * @param {!jQuery} form form to consider
     * @param {!Function} callback
     * @param {Number=} timeout timeout after which the callback is called(used to prevent to call too often the callback), if not specified a default is used
     */
    function timedChange(form, callback, timeout) {
        const radioAndFile = $('input[type="radio"], input[type="file"], select', form);
        radioAndFile.on('blur change',callback);
        $('input, textarea', form).not(radioAndFile).donetyping(callback, timeout);
    }

    return {
        timedChange: timedChange
    };
})();

