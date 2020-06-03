var oApp = angular.module('pms', ['ngRoute', 'ui.bootstrap']);

oApp.filter('range', function () {
    return function (input, total) {
        total = parseInt(total);
        for (var i = 0; i < total; i++)
            input.push(i);
        return input;
    };
});

oApp.run(['$location', '$rootScope', function ($location, $rootScope) {
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
        $rootScope.title = $rootScope;
    });
}]);
oApp.run(function (settingsService, labelService, countryService) {
    settingsService.initSettings();
    labelService.initLabels();
    countryService.getCountries();
});

oApp.config(['$routeProvider', function ($routeProvider) {
    if (getQueryVariable('startD') && getQueryVariable('endD') && getQueryVariable('hotelId')) {

        $routeProvider.when('/', {
            templateUrl: 'pbb/partials/index.html',
            controller: 'homeController',
            reloadOnSearch: false,
            resolve: {
                hotelInfo: function (hotelFactory) {
                    return hotelFactory.loadInfo();
                },
                rooms: function (roomFactory) {
                    return roomFactory.loadAvailableRooms();
                },
                plans: function (plansFactory) {
                    return plansFactory.loadPlans();
                },
                baseRooms: function (baseRoomsFactory) {
                    return baseRoomsFactory.loadBaseRooms();
                }
            }
        }).when('/booking', {
            templateUrl: 'pbb/partials/booking.html',
            controller: 'bookingController',
            reloadOnSearch: false,
            resolve: {
                hotelInfo: function (hotelFactory) {
                    return hotelFactory.loadInfo();
                },
                hotelPhotos: function (hotelFactory) {
                    return hotelFactory.loadPhotos();
                },
                hotelFacilities: function (hotelFactory) {
                    return hotelFactory.loadFacilities();
                }
            }
        }).when('/booked', {
            templateUrl: 'pbb/partials/book.html',
            controller: 'bookedController',
            reloadOnSearch: false,
            resolve: {
                hotelInfo: function (hotelFactory) {
                    return hotelFactory.loadInfo();
                },
                hotelFacilities: function (hotelFactory) {
                    return hotelFactory.loadFacilities();
                },
                hotelPhotos: function (hotelFactory) {
                    return hotelFactory.loadPhotos();
                }
            }
        }).otherwise({
            redirectTo: '/'
        });
    } else {
        $routeProvider.when('/', {
            templateUrl: 'pbb/partials/error.html',
            controller: 'errorController',
            reloadOnSearch: false,
            resolve: {
                hotelInfo: function (hotelFactory) {
                    return hotelFactory.loadInfo();
                }
            }
        });
    }
}]);
