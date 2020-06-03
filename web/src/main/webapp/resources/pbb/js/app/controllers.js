oApp.controller('homeController', function ($rootScope, $scope, $modal, $route, $location, bookingService, labelService, countryService, rooms, roomFactory, priceFactory, plans, plansFactory, hotelInfo, baseRooms, baseRoomsFactory, dateUtils) {

    $scope.model = {
        hotelInfo: hotelInfo,
        rooms: rooms,
        booking: {},
        sum: {},
        photosByRoomType: {},
        facilitiesByRoomType: {},
        messages: {},
        plans: plans,
        baseRooms: baseRooms,
        plan: {
            id: 1
        },
        prices: {},
        daysDifference: {},
        baseRoom: {},
        hotel: $location.search().hotelId
//        serverUrl: ''
    };
    $rootScope.header = $scope.model.hotelInfo.name;

    var utcTime = function (date) {
        return date.getTime() / 1000 - date.getTimezoneOffset() * 60;
    };

    $scope.model.booking.dateStart = $location.search().startD;

    $scope.model.booking.dateEnd = $location.search().endD;

    $scope.isHidden = true;

    $scope.showForm = function () {
        $scope.isHidden = !$scope.isHidden;
    };

    $scope.isCollapsed = true;

    $scope.reduceSum = function (sum) {
        if (sum) {
            for (var s = 0, k = sum.length; k; s += sum[--k]);
            sum = s / $scope.model.daysDifference;
            sum = sum / 100;
            sum = Math.round(sum * 100) / 100;
            return sum;
        }
    };

    for (var b in baseRooms) {
        baseRooms[b].forEach(function (br) {
            $scope.model.booking[br.id] = {};
            $scope.model.plans.forEach(function (p) {
                $scope.model.booking[br.id][p.id] = {
                    quantity: 0,
                    isEmpty: false
                };
            })
        })
    }

    $scope.open = function (item) {
        var modalInstance = $modal.open({
            templateUrl: 'myModalContent.html',
            controller: ModalInstanceCtrl,
            size: 'lg',
            resolve: {
                items: function () {
                    return item;
                },
                details: function () {
                    return $scope.model.baseRooms[item.roomType.id][0].details;
                },
                facilities: function () {
                    return $scope.model.facilitiesByRoomType[item.roomType.id];
                },
                photos: function () {
                    return $scope.model.photosByRoomType[item.roomType.id];
                },
                hotelInfo: function () {
                    return $scope.model.hotelInfo;
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {
            $scope.selected = selectedItem;
        }, function () {
            console.info('Modal dismissed at: ' + new Date());
        });
    };


    $scope.today = function () {
        return dateUtils.toDateString(new Date());
    };

    $scope.timeDiff = function (sDate, eDate) {
        sDate = sDate.split("/");
        sDate = new Date(sDate[2] + "," + sDate[1] + "," + sDate[0]);
        eDate = eDate.split("/");
        eDate = new Date(eDate[2] + "," + eDate[1] + "," + eDate[0]);
        $scope.model.daysDifference = Math.round((eDate.getTime() - sDate.getTime()) / 86400000);
        return $scope.model.daysDifference;
    };

    $scope.timeDiff($location.search().startD, $location.search().endD);

    plans.forEach(function (plan) {
        $scope.model.prices = priceFactory.loadPrices($scope.model.booking.dateStart, $scope.model.booking.dateEnd, plan.id).then(function (prices) {
            $scope.model.prices[plan.id] = prices;
        });
    });

    rooms.forEach(function (room) {
        roomFactory.loadRoomTypeImages(room.roomType.id).then(function (photos) {
            $scope.model.photosByRoomType[room.roomType.id] = photos;
        });

        roomFactory.loadRoomTypeFacilities(room.roomType.id).then(function (facilities) {
            var facilities_arr = [
                [],
                []
            ];
            for (var f in facilities) {
                if (facilities_arr[0].length < 5) {
                    facilities_arr[0].push(facilities[f]);
                }
                else if (facilities_arr[1].length < 5) {
                    facilities_arr[1].push(facilities[f])
                }
                else break;
            }

            $scope.model.facilitiesByRoomType[room.roomType.id] = facilities_arr;
        });
    });

    $scope.getPrices = function (startDate, endDate, planId) {
        priceFactory.loadPrices(startDate, endDate, planId).then(function (value) {
            $scope.model.prices = value;
            plans.forEach(function (plan) {
                $scope.model.prices = priceFactory.loadPrices($scope.model.booking.dateStart, $scope.model.booking.dateEnd, plan.id).then(function (prices) {
                    $scope.model.prices[plan.id] = prices;
                });
            });
        });
    };

    $scope.loadAvailableRooms = function (dateStart, dateEnd) {
        $location.path('/').search({
            "hotelId": $location.search().hotelId,
            "startD": $scope.model.booking.dateStart,
            "endD": $scope.model.booking.dateEnd,
            "button": $location.search().button
        });
        $route.reload();
    };

    $scope.addBooking = function (param) {

        if (!$scope.containsBookings() || param.quantity == 0) {
//            jQuery('.error').toggleClass('error');
            param.isEmpty = true;
            return false
        }
        else {
            param.isEmpty = false;
        }
        for (var b in baseRooms) {
            baseRooms[b].forEach(function (br) {
                $scope.model.plans.forEach(function (p) {
                    var booking = $scope.model.booking[br.id][p.id];
                    booking.total = $scope.reduceSum($scope.model.prices[p.id][br.id]) * booking.quantity * $scope.model.daysDifference;
                })
            })
        }
        bookingService.save(
            $scope.model.booking,
            $scope.getAmount(),
            $scope.model.rooms,
            $scope.model.baseRooms
        );
        $location.path('/booking');
    };

    $scope.checkForError = function (param) {
        if (param.quantity > 0)
            param.isEmpty = false;
    };

    $scope.range = function (quota) {
        return Array.apply(null, {
            length: quota + 1
        }).map(Number.call, Number);
    };

    $scope.exceedsQuota = function (item, value, rt, quota) {
        if (!item || item == 0 || item == value) {
            return false;
        }
        var total = 0,
            delta = item - value;

        //Init total
        for (var br in $scope.model.baseRooms[rt]) {
            var brId = $scope.model.baseRooms[rt][br].id;
            for (var pId in $scope.model.booking[brId]) {
                var booking = $scope.model.booking[brId][pId];
                if (booking.quantity >= 0) {
                    total += parseInt(booking.quantity);
                }
            }
        }

        return  total + delta > quota;
    };


    $scope.getAmount = function () {
        var sum = 0;
        for (var rt in $scope.model.baseRooms) {
            for (var br in $scope.model.baseRooms[rt]) {
                var brId = $scope.model.baseRooms[rt][br].id;
                for (var pId in $scope.model.booking[brId]) {
                    var booking = $scope.model.booking[brId][pId];
                    if ($scope.model.prices[pId]) {
                        if ($scope.model.prices[pId][brId]) {
                            sum += $scope.reduceSum($scope.model.prices[pId][brId]) * booking.quantity;
                        }
                    }
                }
            }
        }
        return sum;
    };

    $scope.containsBookings = function () {
        var res = false;
        for (var b in baseRooms) {
            baseRooms[b].forEach(function (br) {
                $scope.model.plans.forEach(function (p) {
                    if ($scope.model.booking[br.id][p.id].quantity > 0) {
                        res = true;
                    }
                })
            })
        }
        return res;
    };
});

var ModalInstanceCtrl = function ($scope, $location, $modalInstance, items, details, facilities, photos, hotelInfo, labelService) {
    $scope.photos = photos;
    $scope.hotelInfo = hotelInfo;
    $scope.facilities = facilities;
    $scope.items = items;
    $scope.details = details;
    $scope.hotel = $location.search().hotelId;
    $scope.beds = ['single', 'standardDouble', 'largeDouble', 'xLargeDouble', 'bunk', 'sofaBed', 'futon'];

    $scope.selected = {
        item: $scope.items[0]
    };


    $scope.ok = function () {
        $modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

oApp.controller('bookingController', function ($rootScope, $scope, $route, $location, labelService, countryService, bookingService, hotelInfo, hotelFacilities, bookingInfoService, hotelPhotos) {
    $scope.model = {
        hotelPhotos: hotelPhotos,
        dateStart: {},
        daysDifference: {},
        dateEnd: {},
        hotelInfo: hotelInfo,
        hotelFacilities: hotelFacilities,
        customer: {},
        sum: {},
        hotel: $location.search().hotelId,
        rooms: {},
        baseRooms: {},
        quantity: 0
//        bookingCode: {}
    };

    $rootScope.header = $scope.model.hotelInfo.name;

    var utcTime = function (date) {
        var date = new Date(date);
        return date.getTime() / 1000 - date.getTimezoneOffset() * 60;
    };

    var booking = {
        "group": {
            "includeCustomer": true,
            "customer": {
                "id": null,
                "firstName": null,
                "lastName": null,
                "patronymic": null,
                "country": null,
                "city": null,
                "address": null,
                "postIndex": null,
                "phone": null,
                "email": null,
                "discount": null,
                "active": null,
                "language": null,
                "dob": null,
                "passportNumber": null,
                "passportIssued": null,
                "passportValidTill": null,
                "cio": null,
                "entryType": null,
                "entryValidFrom": null,
                "entryValidTill": null,
                "entryNumber": null,
                "visaType": null,
                "carBrand": null,
                "carNumber": null,
                "listMembership": null,
                "membershipReason": null,
                "type": "adult",
                "identity": null,
                "person": null,
                "roomUse": null
            },
            "pov": "TOURISM"
        },
        "roomUses": [

        ]};

    $scope.model.booking = bookingService.listBooking();
    $scope.model.rooms = bookingService.listRooms();
    $scope.model.baseRooms = bookingService.listBaseRooms();
    $scope.model.sum = bookingService.getAmount();

    $scope.isSaving = undefined;

    if (Object.keys($scope.model.booking).length === 0) {
        $location.path('/');
    }

    $scope.goBack = function () {
        $location.path('/');
    };

    $scope.timeDiff = function (sDate, eDate) {
        sDate = sDate.split("/");
        sDate = new Date(sDate[2] + "," + sDate[1] + "," + sDate[0]);
        eDate = eDate.split("/");
        eDate = new Date(eDate[2] + "," + eDate[1] + "," + eDate[0]);
        $scope.model.daysDifference = Math.round((eDate.getTime() - sDate.getTime()) / 86400000);
    };

    if ($scope.model.booking.dateStart && $scope.model.booking.dateEnd) {
        $scope.timeDiff($scope.model.booking.dateStart, $scope.model.booking.dateEnd);
    }

    if ($scope.model.booking.dateStart) {
        var startDate = $scope.model.booking.dateStart.split("/");
        startDate = new Date(startDate[2] + "," + startDate[1] + "," + startDate[0]);
        startDate = utcTime(startDate.getTime());
        $scope.model.dateStart = $scope.model.booking.dateStart;
        delete($scope.model.booking.dateStart);
    }

    if ($scope.model.booking.dateEnd) {
        var endDate = $scope.model.booking.dateEnd.split("/");
        endDate = new Date(endDate[2] + "," + endDate[1] + "," + endDate[0]);
        endDate = utcTime(endDate.getTime());
        $scope.model.dateEnd = $scope.model.booking.dateEnd;
        delete($scope.model.booking.dateEnd);
    }


    booking.startDate = startDate;
    booking.endDate = endDate;

    for (var brId in $scope.model.booking) {
        for (var pId in $scope.model.booking[brId]) {
            $scope.model.quantity += parseInt($scope.model.booking[brId][pId].quantity);
            for (var i = 0; i < $scope.model.booking[brId][pId].quantity; i++) {

                booking.roomUses.push({
                    "description": "",
                    "baseRoom": {
                        "id": brId,
                        "type": "roomType"
                    },
                    "plan": {
                        "id": pId
                    },
                    "customerPays": false
                });
            }
        }
    }

    $scope.model.bookingsByRoomType = {};

    for (var rt in $scope.model.baseRooms) {
        $scope.model.bookingsByRoomType[rt] = {
            quantity: 0,
            total: 0
        };
    }

    $scope.existsByRoomType = function (e) {
        return $scope.model.bookingsByRoomType[e.roomType.id].quantity > 0;
    };

    for (var brId in $scope.model.booking) {
        for (var pId in $scope.model.booking[brId]) {
            var bookInfo = $scope.model.booking[brId][pId];
            var byRoomType = $scope.model.bookingsByRoomType[getRoomType(brId)];
            byRoomType.quantity += parseInt(bookInfo.quantity);
            byRoomType.total += bookInfo.total;
        }
    }

    function getRoomType(brId) {
        for (var rt in $scope.model.baseRooms) {
            if (rt === brId) {
                return brId;
            } else {
                for (var br in $scope.model.baseRooms[rt]) {
                    if ($scope.model.baseRooms[rt][br].id == brId) {
                        return rt;
                    }
                }
            }
        }
    }

    $scope.makeBooking = function () {
        $scope.validating = true;
        if ($scope.customer.$invalid) {
            setTimeout(function () {
                angular.element('html, body').animate({
                    scrollTop: angular.element(".error:first").offset().top
                }, 200)
            }, 100);
            return false
        }
        for (var id in booking.roomUses) {
            booking.roomUses[id].description = $scope.model.customer.description;
        }

        booking.group.customer.firstName = $scope.model.customer.firstName;
        booking.group.customer.lastName = $scope.model.customer.lastName;
        booking.group.customer.city = $scope.model.customer.city;
        booking.group.customer.country = $scope.model.customer.country;
        booking.group.customer.email = $scope.model.customer.email;
        booking.group.customer.address = $scope.model.customer.address;
        booking.group.customer.postIndex = $scope.model.customer.postIndex;

        $scope.isSaving = true;
        bookingService.makeBooking(booking).then(function (status) {
            booking.bookingCode = {};
            booking.bookingCode = {
                bookingCode: status.content
            };
            bookingInfoService.save(booking, $scope.model.sum);
            $location.path('/booked');
        }, function (err) {
            $scope.isSaving = false;
            console.error('error creating  booking');
        });
    };

    $scope.validate = function (field) {
        return (field.$dirty && field.$invalid) || ($scope.validating && field.$invalid)
    }
});

oApp.controller('bookedController', function ($rootScope, $scope, $route, $location, labelService, hotelPhotos, countryService, hotelInfo, hotelFacilities, bookingInfoService) {

    $scope.model = {
        booking: {},
        hotelInfo: hotelInfo,
        hotelFacilities: hotelFacilities,
        hotelPhotos: hotelPhotos,
        sum: {},
        hotel: $location.search().hotelId
    };

    $rootScope.header = $scope.model.hotelInfo.name;

    $scope.model.startDate = $location.search().startD;
    $scope.model.endDate = $location.search().endD;


    $scope.model.booking = bookingInfoService.getBooking();

    $scope.model.sum = bookingInfoService.getAmount();


    if (Object.keys($scope.model.booking).length === 0) {
        $location.path('/');
    }

    $scope.timeDiff = function (sDate, eDate) {
        $scope.model.daysDifference = Math.round((eDate - sDate) / 86400);
    };

    $scope.timeDiff($scope.model.booking.startDate, $scope.model.booking.endDate);
});

oApp.controller('errorController', function ($rootScope, $scope, $route, $location, hotelInfo) {

    $scope.model = {
        hotelInfo: hotelInfo
    };

    $rootScope.header = $scope.model.hotelInfo.name;
});