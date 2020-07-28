"use strict";

let socket;

function connect() {
    socket = new WebSocket('ws://localhost:9000/ws');
    socket.onopen = function() {
        console.log("clientWebSocket.onopen", socket);
        console.log("clientWebSocket.readyState", "websocketstatus");
    }
    socket.onclose = function(close) {
        console.log("clientWebSocket.onclose", socket, close);
    }
    socket.onerror = function(error) {
        console.log("clientWebSocket.onerror", socket, error);
    }
    socket.onmessage = function(message) {
        if(message.data !== undefined){
            const messageData = JSON.parse(message.data);
            if(messageData.action === "ping"){
                socket.send(JSON.stringify({action:"pong"}));
            }
        }
        console.log("clientWebSocket.onmessage", socket, message);
    }


}

function events(responseEvent) {
    document.querySelector(".events").innerHTML += responseEvent + "<br>";
}

function sendName() {
    socket.send( JSON.stringify(JSON.parse($("#name").val())));
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