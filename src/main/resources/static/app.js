"use strict";

let socket;
let login;
let password;

function connect() {
    socket = new WebSocket((location.protocol === 'https:' ? 'wss:':'ws:') + '//ws.' + window.location.host + '/ws');
    // socket = new WebSocket((location.protocol === 'https:' ? 'wss:' : 'ws:') + '//' + window.location.host + '/ws');
    socket.onopen = function () {
        console.log("clientWebSocket.onopen", socket);
        console.log("clientWebSocket.readyState", "websocketstatus");
    }
    socket.onclose = function (close) {
        console.log("clientWebSocket.onclose", socket, close);
    }
    socket.onerror = function (error) {
        console.log("clientWebSocket.onerror", socket, error);
    }
    socket.onmessage = function (message) {
        if (message.data !== undefined) {
            const messageData = JSON.parse(message.data);
            if (messageData.action === "ping") {
                socket.send(JSON.stringify({action: "pong"}));
                return;
            }
            if (messageData.message) {
                let $popupError = $('#tilda-popup-for-error');
                if ($popupError.length === 0) {
                    $('body').append('<div id="tilda-popup-for-error" class="js-form-popup-errorbox tn-form__errorbox-popup" style="display: none;"> <div class="t-form__errorbox-text t-text t-text_xs"> error </div> <div class="tn-form__errorbox-close js-errorbox-close"> <div class="tn-form__errorbox-close-line tn-form__errorbox-close-line-left"></div> <div class="tn-form__errorbox-close-line tn-form__errorbox-close-line-right"></div> </div> </div>');
                    $popupError = $('#tilda-popup-for-error');
                    $('#tilda-popup-for-error').on('click', '.js-errorbox-close', function (e) {
                        e.preventDefault();
                        $('#tilda-popup-for-error').fadeOut();
                        return !1
                    })
                }
                $popupError.find('.t-form__errorbox-text').html('<p class="t-form__errorbox-item">' + messageData.message + '</p>');
                $popupError.css('display', 'block').fadeIn();
                $popupError.find('.t-form__errorbox-item').fadeIn()
                if (messageData.message === "invalid credentials") {
                    login = undefined;
                    password = undefined;
                }
                return;
            }
            if (Array.isArray(messageData)) {
                messageData.forEach(setDeviceStatus)
            } else {
                setDeviceStatus(messageData)
            }
        }
        console.log("clientWebSocket.onmessage", socket, message);
    }


}

function setDeviceStatus(controlPackage) {
    let controlElement = undefined;
    for (let controlElementVar in CONTROL_ELEMENTS) {
        if (CONTROL_ELEMENTS[controlElementVar].identifier === controlPackage.device) {
            controlElement = CONTROL_ELEMENTS[controlElementVar];
            break;
        }
    }
    if (controlElement === undefined) {
        return;
    }
    if (controlPackage.color) {
        if ("colorInput" in controlElement) {
            controlElement.colorInput.val('#' + controlPackage.color);
        }
    }
    if (controlPackage.power) {
        if ("label" in controlElement) {
            controlElement.label.text("on");
        }
        if ("indicator" in controlElement) {
            controlElement.indicator.css("background-color", COLOR_DICT.on);
        }
    } else {
        if ("label" in controlElement) {
            controlElement.label.text("off");
        }
        if ("indicator" in controlElement) {
            controlElement.indicator.css("background-color", COLOR_DICT.off);
        }
        if ("colorInput" in controlElement) {
            controlElement.colorInput.val('#000000');
        }
    }
    controlElement.button.addClass("disabled-device-button");
    setTimeout(() => controlElement.button.removeClass("disabled-device-button"), 5000);
}

function showLoginPasswordPopup() {
    document.getElementById("popup").style.display = "block";
}

function saveLoginPassword() {
    login = $("#login").val()
    password = $("#password").val()
    document.getElementById("popup").style.display = "none";
}

const COLOR_DICT = {on: "#5afa32", off: "#fe0002", inactive: "#7a7a7b"}


let CONTROL_ELEMENTS;

function AppOnFinishLoad() {

    CONTROL_ELEMENTS = {
        SWITCH1: {
            label: $("[data-elem-id=1594307212547]").children(),
            indicator: $("[data-elem-id=1594307274180]").children(),
            button: $("[data-elem-id=1594222623504]").children(),
            identifier: "c26f109d-33ce-4287-abe7-b114828f4a47"
        },
        SWITCH3: {
            label: $("[data-elem-id=1594307599681]").children(),
            indicator: $("[data-elem-id=1594307599668]").children(),
            button: $("[data-elem-id=1594307599655]").children(),
            identifier: "58e8d2ce-2c54-40d0-ab6b-a9843cb11979"
        },
        SWITCH4: {
            label: $("[data-elem-id=1594307660399]").children(),
            indicator: $("[data-elem-id=1594307660391]").children(),
            button: $("[data-elem-id=1594307660382]").children(),
            identifier: "e716033e-e371-42e5-a0de-802c46f558cc"
        },
        SWITCH5: {
            label: $("[data-elem-id=1594307759008]").children(),
            indicator: $("[data-elem-id=1594307759002]").children(),
            button: $("[data-elem-id=1594307758996]").children(),
            identifier: "dd170f02-6f78-44ae-bf53-056d61b1c4b4"
        },
        LOGO_LETTER_1: {
            colorInput: $("[data-elem-id=1594731585042]").children().children(),
            button: $("[data-elem-id=1594731753585]").children(),
            identifier: "81a2e1ee-4b18-4364-ab59-2f6ef1140efa"
        },
        LOGO_LETTER_2: {
            colorInput: $("[data-elem-id=1594731847810]").children().children(),
            button: $("[data-elem-id=1594731847817]").children(),
            identifier: "e0be0e83-79e3-419b-8381-edea7806d377"
        },
        LOGO_LETTER_3: {
            colorInput: $("[data-elem-id=1594731866012]").children().children(),
            button: $("[data-elem-id=1594731866019]").children(),
            identifier: "850d68ab-7235-4ae6-8f6e-cdbeb88df8c2"
        },
        LOGO_LETTER_4: {
            colorInput: $("[data-elem-id=1594731873703]").children().children(),
            button: $("[data-elem-id=1594731873710]").children(),
            identifier: "1a27947b-601a-42ec-9bf5-81c5106ef3c9"
        },
        LOGO_LETTER_5: {
            colorInput: $("[data-elem-id=1594731876087]").children().children(),
            button: $("[data-elem-id=1594731876092]").children(),
            identifier: "fd973a42-4a63-4a9d-8f2f-fa9b994df487"
        },

    }

    CONTROL_ELEMENTS.SWITCH1.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.SWITCH1.identifier,
            power: CONTROL_ELEMENTS.SWITCH1.label.text() === "off"
        }));
    })
    CONTROL_ELEMENTS.SWITCH4.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.SWITCH4.identifier,
            power: CONTROL_ELEMENTS.SWITCH4.label.text() === "off"
        }));
    })
    CONTROL_ELEMENTS.SWITCH5.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.SWITCH5.identifier,
            power: CONTROL_ELEMENTS.SWITCH5.label.text() === "off"
        }));
    })

    CONTROL_ELEMENTS.SWITCH3.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        if (login && password) {
            socket.send(JSON.stringify({
                device: CONTROL_ELEMENTS.SWITCH3.identifier,
                power: CONTROL_ELEMENTS.SWITCH3.label.text() === "off",
                login: login,
                password: password
            }));
        } else {
            showLoginPasswordPopup();
        }
    })

    CONTROL_ELEMENTS.SWITCH4.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.SWITCH4.identifier,
            power: CONTROL_ELEMENTS.SWITCH1.label.text() === "off"
        }));
    })

    CONTROL_ELEMENTS.SWITCH5.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.SWITCH5.identifier,
            power: CONTROL_ELEMENTS.SWITCH1.label.text() === "off"
        }));
    })


    CONTROL_ELEMENTS.LOGO_LETTER_1.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.LOGO_LETTER_1.identifier,
            power: true,
            color: CONTROL_ELEMENTS.LOGO_LETTER_1.colorInput.val().substr(1)
        }));
    })
    CONTROL_ELEMENTS.LOGO_LETTER_2.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.LOGO_LETTER_2.identifier,
            power: true,
            color: CONTROL_ELEMENTS.LOGO_LETTER_2.colorInput.val().substr(1)
        }));
    })
    CONTROL_ELEMENTS.LOGO_LETTER_3.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.LOGO_LETTER_3.identifier,
            power: true,
            color: CONTROL_ELEMENTS.LOGO_LETTER_3.colorInput.val().substr(1)
        }));
    })
    CONTROL_ELEMENTS.LOGO_LETTER_4.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.LOGO_LETTER_4.identifier,
            power: true,
            color: CONTROL_ELEMENTS.LOGO_LETTER_4.colorInput.val().substr(1)
        }));
    })
    CONTROL_ELEMENTS.LOGO_LETTER_5.button.parent().on("click", "div:not(.disabled-device-button)", function () {
        socket.send(JSON.stringify({
            device: CONTROL_ELEMENTS.LOGO_LETTER_5.identifier,
            power: true,
            color: CONTROL_ELEMENTS.LOGO_LETTER_5.colorInput.val().substr(1)
        }));
    })

    connect();

}