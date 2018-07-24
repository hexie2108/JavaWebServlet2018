if(typeof jQuery === "undefined") {
    console.error("Jquery needed");
}

function createDivNotification(notification){
    const lettoSm = jQuery('<small/>', {
        text: notification.status?"Letto":"Non letto"
    });

    const dateSm = jQuery('<small/>', {
        class: "float-right",
        title: notification.date,
        text: (new Date(notification.date)).toLocaleString() // La data è in UTC quindi la converto secondo la timezone locale
    });

    const header = jQuery('<div/>', {
        class: ["d-flex","w-100","justify-content-between"].join(' '),
        html: [lettoSm, dateSm]
    });

    const textDiv = jQuery('<div/>', {
        text: notification.text
    });

    return jQuery('<li/>', {
        class: "list-group-item",
        html: [header, textDiv]
    });
}

function updateNotificationList(list, url, async, status, count){
    if (status === true || status === false) {
        status = "" + status;
    } else if (status === undefined || status === null) {
        status = "";
    } else {
        status = "";
        console.warn("filter must be true, false, null or undefined");
    }

    const request = $.ajax({
        dataType: "json",
        url : url,
        type: "get",
        async: async,
        data: "status="+status
    });

    request.done(function(data){
        list.empty();
        list.append(data.map(v => createDivNotification(v)));

        if(count !== null && count !== undefined) {
            count.html(" "+ data.length);
        }
    });
}

