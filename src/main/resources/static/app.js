"use strict";
let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/devices', function (device) {
            showGreeting(JSON.parse(device.body));
        });
    });
}

function sendName() {
    stompClient.send("/app/devices", {}, JSON.stringify(JSON.parse($("#name").val())));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" +  JSON.stringify(message,null,2) + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendName(); });
    connect();
});