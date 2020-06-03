var prefix = (window.location.protocol === 'https:') ? 'wss://' : 'ws://';
var url = prefix + document.location.host + "/app/websocket";
var websocket = null;

function connectWebSocket(attempt) {
    console.log("Connecting to " + url);


    if (!websocket) {
        websocket = new WebSocket(url);
    }

    websocket.onopen = function () {
        console.log("onOpen");
    };

    function pushNotification(code, data) {
        var createIcon = 'https://cdn1.iconfinder.com/data/icons/perfect-flat-icons-2/512/Info_information_user_about_card_button_symbol.png';
        var removeIcon = 'http://www.clker.com/cliparts/D/0/R/b/X/W/red-cross-md.png';

        switch (code) {
            case "channels.newBooking":
                notify('Новая бронь ' + channelInfo(), parseSingleAction());
                break;
            case "channels.newGroupBooking":
                notify('Новая групповая бронь ' + channelInfo(), parseGroupAction());
                break;
            case "channels.refuse":
                notify('Отказ от брони', parseSingleAction(), removeIcon);
                break;
            case "channels.groupRefuse":
                notify('Отказ от брони', parseGroupAction(), removeIcon);
                break;
            case "channels.error.newBooking.notEnoughRoomsSingle":
                notify('Ошибка создания новой брони ' + channelInfo(), parseNotEnoughRooms(), removeIcon);
                break;
            case "channels.error.newBooking.notEnoughRoomsGroup":
                notify('Ошибка создания новой брони ' + channelInfo(), parseNotEnoughRoomsGroup(), removeIcon);
                break;
            case "systemAlert":
                notify('System Alert', data, removeIcon);
                break;
            case "mailSent":
                notify('Сообщение отправлено!', "Сообщение на " + data + " успешно отправлено.");
                break;
            case "mailFailed":
                notify('Сообщение не было отправлено!', "Сообщение на \"" + data + "\" не было отправлено.\nПроверьте правильность указаного адреса электронной почты.", removeIcon);
                break;
            default:
                console.error("WebSocket: unknown message code: " + code + " was received with data: " + data);
        }

        function channelInfo() {
            var channel = "c канала ";

            function parseSource(source) {
                switch (source) {
                    case "BOOKING":
                        return channel + "Booking.com";
                    case "BOOKING_BUTTON":
                        return "с веб сайта отеля";
                    case "WUBOOK_BUTTON":
                        return "с веб сайта отеля";
                    default :
                        return source;
                }
            }

            return parseSource(data.source ? data.source : data[0].source);
        }

        function parseNotEnoughRooms() {
            return "Недостаточно свободных номеров.\n" + parsePeriodString(data) + ".\nРазмещение: " + data.baseRoom.name;
        }

        function parseNotEnoughRoomsGroup() {
            function parseBaseRooms() {
                var res = '';
                data.forEach(function (roomUse, i) {
                    res += roomUse.baseRoom.name + (i < data.length - 1 ? ", " : ".");
                });
                return res;
            }

            return "Недостаточно свободных номеров для групповой брони.\n" + parsePeriodString(data[0]) + '.\nРазмещения: ' + parseBaseRooms();
        }

        function parseSingleAction() {
            return parsePeriodString(data) + ".\nНомер: " + parseRoomString(data);
        }

        function parseGroupAction() {
            var text = parsePeriodString(data[0]) + '.\nНомера: ';
            for (var i = 0; i < data.length; i++) {
                text += parseRoomString(data[i]) + ', ';
            }
            return text;
        }

        function parsePeriodString(roomUse) {
            return "Период: " + parseDate(roomUse.startDate) + " - " + parseDate(roomUse.endDate);
        }

        function parseRoomString(roomUse) {
            function getRoomTypeName(baseRoom) {
                return baseRoom.roomType ? baseRoom.roomType.name : baseRoom.name;
            }

            return roomUse.room ? roomUse.room.number + " (" + roomUse.room.roomType.name + ")" : getRoomTypeName(roomUse.baseRoom);
        }

        function notify(title, text, icon) {
            if (!icon) {
                icon = createIcon;
            }

            if (window.webkitNotifications) {
                function show() {
                    var notification = window.webkitNotifications.createNotification(
                        icon,
                        title, text);
                    notification.show();
                }

                if (window.webkitNotifications.checkPermission() == 0) { // 0 is PERMISSION_ALLOWED
                    show();
                } else {
                    window.webkitNotifications.requestPermission(show);//As the doc says: this method should only be called while handling a user gesture; in other circumstances it will have no effect.
                }
            } else {
                function show() {
                    new Notification(title, {icon: icon, body: text});
                }

                if (Notification.permission == "granted") {
                    show();
                } else {
                    Notification.requestPermission(show);//As the doc says: this method should only be called while handling a user gesture; in other circumstances it will have no effect.
                }
            }


        }

        function parseDate(seconds) {
            var date = new Date(seconds * 1000);
            return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear()
        }

    }

    websocket.onmessage = function (event) {
        var eData;
        if (typeof event.data === "string") {
            eData = JSON.parse(event.data);
            if (eData.data && eData.code) {
                pushNotification(eData.code, eData.data);
            } else {
                console.error("Invalid message format: " + event.data + "/n Must be: { code : '', data: '')");
            }
        } else {
            console.error("Unexpected message type: " + event.data);
        }
    };

    websocket.onerror = function (event) {
        reconnect();
    };

    websocket.onclose = function (event) {
        console.log('close websocket connection');
    };

    function reconnect() {
        console.log("reconnect attempt:" + attempt);
        var reconnectLimit = 20;
        if (attempt <= reconnectLimit) {
            connectWebSocket(++attempt);
        } else {
            connectionFailed();
        }
    }

    function connectionFailed() {
        showMessage('<span style="color: red;">' + l('serverDisconnected') + '</span>');
        setTimeout(function () {
            Ext.Msg.alert({
                title: l('webSocketMessage'),
                msg: '<span style="color: red;">' + l('serverDisconnected') + '</span>',
                fn: function () {
                    location.reload();
                }
            });
        }, 1000);
    }

}

function showMessage(message) {
    console.log(l("webSocketMessage") + message);
}
