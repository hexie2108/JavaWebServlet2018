<%@ page contentType="text/javascript;charset=UTF-8" language="java" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>

"use strict";

function request_errors(form, async, url){
    return $.ajax({
        dataType: "json",
        url : url,
        type: "post",
        async: async,
        data: form.find("input").serialize()
    });
}

function updateVerifyMessages(form, data) {
    // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
    const inputs = form.find('input').map(function(){return this.name;}).get();
    // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
    const validityInputs = inputs.map(
        function(key) {
            const div = $("#div" + key);
            const span = $("#span" + key);

            if (data.hasOwnProperty(key)) {
                div.addClass("has-error");
                span.html(String(data[key]));

                return false;
            }

            div.removeClass("has-error");
            span.html("");
            return true;
        }
    );

    // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
    // Se false l'invio del form verrà bloccato altrimenti no
    return validityInputs.every( v => v );
}

function add_file_errors(form, data){
    const checked_radio = form.find('input[name="${RegistrationValidator.AVATAR_KEY}"]:checked');
    const avatarImgCustom = form.find('input[name="${RegistrationValidator.AVATAR_IMG_KEY}"]')[0];

    if(checked_radio.length === 0 || checked_radio.val() !== "${RegistrationValidator.CUSTOM_AVATAR}" ) {
        return data;
    }

    // Se l'estensione per leggere i file è supportata faccio il controllo altrimenti no
    // (fatto successivamente dal server)
    if (!window.FileReader) {
        return data;
    }

    // Se il browser ha l'estensione che permette di accedere alla proprietà files continuo altrimenti no
    // (fatto successivamente dal server)
    if (!avatarImgCustom.files) {
        return data;
    }

    const fileToUpload = avatarImgCustom.files[0];

    if(!fileToUpload) {
        data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "No file";
    } else if (fileToUpload.size < ${RegistrationValidator.MIN_LEN_FILE}) {
        data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "File has zero size";
    } else if(fileToUpload.size > ${RegistrationValidator.MAX_LEN_FILE}){
        data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "File has size > 15MB";
    } else if(window.Blob && !fileToUpload.type.startsWith("image/")) {
        data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "File must be an image";
    }

    return data;
}



