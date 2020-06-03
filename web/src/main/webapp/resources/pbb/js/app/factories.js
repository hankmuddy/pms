oApp.factory('roomFactory', function ($http, $q, $location) {
  var url = 'api/',
    factory = {};

  factory.loadAvailableRooms = function () {
    var oDefer = $q.defer(),
      sPath = url + "findAvailable",

      oConfig = {
        method: 'post',
        url: sPath,
        params: {
          "hotelId": $location.search().hotelId,
          "dfrom": $location.search().startD,
          "dto": $location.search().endD
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });
    return oDefer.promise;
  };

  factory.loadRoomTypeImages = function (roomTypeId) {

    var oDefer = $q.defer(),
      sPath = url + "imagesByRoomType/" + roomTypeId,
      oConfig = {
        method: 'post',
        url: sPath,
        params: {
          "hotelId": $location.search().hotelId
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });
    return oDefer.promise;
  };


  factory.loadRoomTypeFacilities = function (roomTypeId) {
    var oDefer = $q.defer(),
      sPath = url + "facilityByRoomType/" + roomTypeId,
      oConfig = {
        method: 'post',
        url: sPath,
        params: {
          "hotelId": $location.search().hotelId
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };

  return factory;
});


oApp.factory('messageFactory', function ($http, $q) {
  var url = 'api/labels',
    factory = {};

  factory.loadMessages = function () {
    var oDefer = $q.defer(),
      sPath = url,
      oConfig = {
        method: 'get',
        url: sPath
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };
  return factory;
});

oApp.factory('settingsFactory', ['$http', '$q', '$location', function ($http, $q, $location) {
  var factory = {};

  factory.loadSettings = function () {
    var url = 'api/bbs/' + $location.search().button + '?hotelId=' + $location.search().hotelId;
//        var url = 'api/bbs/'+$location.search().button +'?hotelId=' + $location.search().hotelId;

    var oDefer = $q.defer(),
      sPath = url,
      oConfig = {
        method: 'get',
        url: sPath
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };
  return factory;
}]);

oApp.factory('countriesFactory', function ($http, $q) {
  var url = 'client/JSON/countries.json',
    factory = {};

  factory.loadCountries = function () {
    var d = $q.defer();

    $http.get(url).success(function (aResolvedItems) {
      d.resolve(aResolvedItems);
    }).error(function () {
      console.error('Unable to load items list!');
      d.reject('Unable to load items list!');
    });

    return d.promise;
  };

  return factory;
});

oApp.factory('plansFactory', function ($http, $q, $location) {
  var factory = {};

  factory.loadPlans = function (hotelId) {
    var url = 'api/plans?hotelId=' + $location.search().hotelId;
    var oDefer = $q.defer(),
      oConfig = {
        method: 'post',
        url: url
      };

    $http(oConfig).success(function (aResolvedItems) {
      var result = {};
      result = aResolvedItems.content;
      oDefer.resolve(result);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };
  return factory;
});

oApp.factory('priceFactory', function ($http, $q, $location) {
  var url = 'api/prices?hotelId=' + $location.search().hotelId,
    factory = {};

  var utcTime = function (date) {
    var date = new Date(date);
    return date.getTime() / 1000 - date.getTimezoneOffset() * 60;
  };

  factory.loadPrices = function (startDate, endDate, planId) {
    startDate = $location.search().startD;
    endDate = $location.search().endD;
    startDate = startDate.split("/");
    startDate = new Date(startDate[2] + "," + startDate[1] + "," + startDate[0]);
    endDate = endDate.split("/");
    endDate = new Date(endDate[2] + "," + endDate[1] + "," + endDate[0]);
    endDate = utcTime(endDate.getTime());
    startDate = utcTime(startDate.getTime());

    var oDefer = $q.defer(),
      data = {
        plan: {
          id: planId
        },
        start: startDate,
        end: endDate
      };

    $http.post(url, data).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };
  return factory;
});

oApp.factory('baseRoomsFactory', function ($http, $q, $location) {
  var url = 'api/baseRooms',
    factory = {};

  factory.loadBaseRooms = function () {
    var oDefer = $q.defer(),
      oConfig = {
        method: 'post',
        url: url,
        params: {
          "hotelId": $location.search().hotelId
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      var result = {},
        addVR = function (v, rtId) {
          if (result[rtId]) {
            result[rtId].push(v);
          } else {
            result[rtId] = [v];
          }
        };

      aResolvedItems.content.forEach(function (v) {
        if (v.roomType) {
          addVR(v, v.roomType.id);
        } else {
          addVR(v, v.id);
        }
      });

      oDefer.resolve(result);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };

  return factory;
});

oApp.factory('hotelFactory', function ($http, $q, $location) {
  var url = '',
    factory = {};

  factory.loadInfo = function () {

    url = 'api/info?hotelId=' + $location.search().hotelId;

    var oDefer = $q.defer(),
      oConfig = {
        method: 'post',
        url: url,
        params: {
          "hotelId": $location.search().hotelId
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content.hotelInfo);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load data');
    });

    return oDefer.promise;
  };

  factory.loadPhotos = function () {
    url = 'api/hotelPhotos';

    var oDefer = $q.defer(),
      oConfig = {
        method: 'post',
        url: url,
        params: {
          "hotelId": $location.search().hotelId
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems.content);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load items list!');
    });

    return oDefer.promise;
  };

  factory.loadFacilities = function () {
    url = 'api/hotelFacilities?hotelId=' + $location.search().hotelId;

    var oDefer = $q.defer(),
      oConfig = {
        method: 'post',
        url: url,
        params: {
          "hotelId": $location.search().hotelId
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems);
    }).error(function () {
      console.error('Unable to load items list!');
      oDefer.reject('Unable to load data');
    });

    return oDefer.promise;
  };

  return factory;
});


oApp.factory('bookingFactory', function ($http, $q, $location) {
  var factory = {},
    url = '';

  factory.addBooking = function (data) {
    url = 'api/book?hotelId=' + $location.search().hotelId;

    var oDefer = $q.defer(),
      oConfig = {
        method: '',
        url: url,
        params: {
          data: data
        }
      };

    $http(oConfig).success(function (aResolvedItems) {
      oDefer.resolve(aResolvedItems);
    }).error(function () {
      oDefer.reject('Request error');
    });

    return oDefer.promise;
  };

  return factory;
});

oApp.factory('customizationFactory', function () {
  return {
    getColorAdjustDirective: function (attr, watch) {
      return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
          oScope.$watch(aAttrs[watch], function (v) {
            oElement.css(attr, v);
          });
        }
      };
    }
  };
});
