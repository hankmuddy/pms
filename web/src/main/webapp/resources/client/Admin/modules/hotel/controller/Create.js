Ext.define('Admin.modules.hotel.controller.Create', {
    extend: 'Admin.generic.app.controller.Create',
    modelClass: 'Admin.modules.hotel.Model',
    viewClass: 'Admin.modules.hotel.view.Create',
    onCreateSubmit: function (data) {
//        var mapPanel = this.view.down('gmappanel');
        var objToSend = {
            user: {
                authentication: {
                    username: data.username,
                    password: data.password,
                    userType: 'user'
                },
                email: data.email,
                phone: data.phone,
                firstName: data.firstName,
                lastName: data.lastName,
                position: data.position
            },
            hotel: {
                extranet: data.extranet,
                wuName: data.wuName,
                wuPass: data.wuPass,
                lcode: "" === data.lcode ? null : data.lcode,
                maxRooms: data.maxRooms,
                info: {
                    name: data.name,
                    country: data.country,
                    currency: data.currency,
                    city: data.city,
                    timeZone: data.timeZone,
                    index: data.index,
                    postAddress: data.postAddress,
                    officialAddress: data.officialAddress,
                    bookPhone: data.bookPhone,
                    accountPhone: data.accountPhone,
                    infoPhone: data.infoPhone,
                    email: data.hotelEmail,
                    webSite: data.webSite,
                    description: data.description,
                    multiplier: data.multiplier,
                    vat: data.vat,
                    tourismTax: data.tourismTax,
                    earlyCheckInNoBreakfast: data.earlyCheckInNoBreakfast,
                    checkIn: data.checkIn.getHours() * 60 + data.checkIn.getMinutes(),
                    earlyCheckInStart: data.earlyCheckInStart ? data.earlyCheckInStart.getHours() * 60 + data.earlyCheckInStart.getMinutes() : null,
                    earlyCheckInEnd: data.earlyCheckInEnd ? data.earlyCheckInEnd.getHours() * 60 + data.earlyCheckInEnd.getMinutes() : null,
                    checkOut: data.checkOut.getHours() * 60 + data.checkOut.getMinutes(),
                    lateCheckOutStart: data.lateCheckOutStart ? data.lateCheckOutStart.getHours() * 60 + data.lateCheckOutStart.getMinutes() : null,
                    lateCheckOutEnd: data.lateCheckOutEnd ? data.lateCheckOutEnd.getHours() * 60 + data.lateCheckOutEnd.getMinutes() : null
//                    latitude: mapPanel.latitude,
//                    longitude: mapPanel.longitude
                }
            }
        };
        var newObj = new this.model(objToSend);

        newObj.save({
            scope: this,
            success: this.onSuccessSubmit,
            failure: this.onFailureSubmit
        });
    }
});
