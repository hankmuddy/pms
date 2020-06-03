oApp.service('bookingService', ['$http', '$q', '$location', function ($http, $q, $location) {

    var uid = 1,
        book = {},
        baseRooms = {},
        rooms = {},
        amount = {};


    var save = function (booking, sum, room, bRoom) {
        book = booking;
        amount = sum;
        baseRooms = bRoom;
        rooms = room;
    };

    var getAmount = function () {
        return amount;
    };

    var get = function (id) {
        for (var i in book) {
            if (book[i].id == id) {
                return book[i];
            }
        }
    };

    var deleteItem = function (id) {
        for (var i in book) {
            if (book[i].id == id) {
                book.splice(i, 1);
            }
        }
    };

    var listBooking = function () {
        return book;
    };

    var listBaseRooms = function () {
        return baseRooms;
    };

    var listRooms = function () {
        return rooms;
    };


    var makeBooking = function (booking) {
        var d = $q.defer();

        $http({
            method: 'POST',
            data: booking,
            url: 'api/book?hotelId=' + $location.search().hotelId
        }).success(function (data, status, header) {
            d.resolve(data);
        }).error(function (data, status, header) {
            d.reject(data);
        });

        return d.promise;
    };

    return {
        save: save,
        get: get,
        getAmount: getAmount,
        listBooking: listBooking,
        deleteItem: deleteItem,
        makeBooking: makeBooking,
        listRooms: listRooms,
        listBaseRooms: listBaseRooms
    };
}]);


oApp.service('dateService', function ($rootScope) {
    var startDate = '',
        endDate = '',
        hotelId = '';

    var setParams = function (sDate, eDate, hId) {
        startDate = sDate;
        endDate = eDate;
        hotelId = hId;
    };

    var getParams = function () {

        return {
            "startDate": startDate,
            "endDate": endDate,
            "hotelId": hotelId
        }
    };

    return {
        setParams: setParams,
        getParams: getParams
    };
});

oApp.service('bookingInfoService', ['$http', '$q', function ($http, $q) {
    var booking = {};
    var amount = {};

    var save = function (book, sum) {
        amount = sum;
        booking = book;
    };

    var getAmount = function () {
        return amount;
    };

    var getBookings = function () {
        return booking;
    };

    return {
        save: save,
        getBooking: getBookings,
        getAmount: getAmount
    }
}]);

oApp.service('utilsService', function ($rootScope) {

    var timeDiff = function (startDate, endDate) {
        startDate = startDate.split("/");
        startDate = new Date(startDate[2] + "," + startDate[1] + "," + startDate[0]);
        endDate = endDate.split("/");
        endDate = new Date(endDate[2] + "," + endDate[1] + "," + endDate[0]);
        return Math.round((endDate.getTime() - startDate.getTime()) / 86400000);
    };

    var utcTime = function (date) {
        return date.getTime() / 1000 - date.getTimezoneOffset() * 60;
    };

    return {
        timeDiff: timeDiff,
        utcTime: utcTime
    }
});

oApp.service('labelService', function ($rootScope, messageFactory) {
    this.initLabels = function () {
        messageFactory.loadMessages().then(function (value) {
            $rootScope.labels = value;
        });
    };
});

oApp.service('settingsService', function ($rootScope, settingsFactory, $location) {
    this.initSettings = function () {

        $rootScope.serverUrl = '';


        if ($location.search().button && $location.search().button != 'null' && $location.search().button != 'undefined') {
            settingsFactory.loadSettings().then(function (value) {
                $rootScope.properties = value;
                $rootScope.settings = {};
                if (value) {
                    for (var i in value.keyValues) {
                        $rootScope.settings[value.keyValues[i].key] = value.keyValues[i].value;
                    }
                }
                function putDefault(key, val) {
                    if (!$rootScope.settings[key]) {
                        $rootScope.settings[key] = val;
                    }
                }

                putDefault("textColor", "#00B1FA");
                putDefault("buttonColor", "#0db455");
                putDefault("buttonTextColor", "#FFFFFF");
                putDefault("buttonHover", "#00c956");
            });
        }

        if ($location.host() == 'localhost') {
            $rootScope.serverUrl = 'http://pmsphotos.loc/';
        } else if ($location.host() == 'pmscloud.com') {
            $rootScope.serverUrl = 'http://photo.pmscloud.com/';
        } else if ($location.host() == '88.198.41.177:8081') {
            $rootScope.serverUrl = 'http://88.198.41.177:8088/';
        } else {
            $rootScope.serverUrl = 'http://88.198.41.177:8088/';
        }
    };
});

oApp.service('countryService', function ($rootScope, countriesFactory) {
    this.getCountries = function () {
        countriesFactory.loadCountries().then(function (value) {
            $rootScope.countries = value;
        });
    };
});

oApp.service('dateUtils', function ($rootScope) {
    this.days = {
        1: "monday",
        2: "tuesday",
        3: "wednesday",
        4: "thursday",
        5: "friday",
        6: "saturday",
        7: "sunday"
    };
    this.months = {
        0: "ofJanuary",
        1: "ofFebruary",
        2: "ofMarch",
        3: "ofApril",
        4: "ofMay",
        5: "ofJune",
        6: "ofJuly",
        7: "ofAugust",
        8: "ofSeptember",
        9: "ofOctober",
        10: "ofNovember",
        11: "ofDecember"
    };
    this.getDay = function (day) {
        return $rootScope.labels[this.days[day]];
    };
    this.getMonth = function (month) {
        return $rootScope.labels[this.months[month]];
    };
    this.toDate = function (v) {
        var split = v.split("/");
        return new Date(split[2] + "," + split[1] + "," + split[0]);
    };
    this.toDateString = function (date) {
        return toTwoDigitString(date.getDate()) + "/" + toTwoDigitString(date.getMonth() + 1) + "/" + date.getFullYear()
    };
    function toTwoDigitString(number) {
        return number < 10 ? "0" + number : number;
    }
});