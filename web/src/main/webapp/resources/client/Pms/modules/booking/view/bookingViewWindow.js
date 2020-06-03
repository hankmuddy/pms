Ext.define('Pms.modules.booking.view.bookingViewWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.bookingViewWindow',
    title: l('bookingInformation'),
    width: 1080,
    height: 650,
    useTypes: Ext.create('Pms.modules.roomUse.store.useType'),
    data: {},
    settings: {},
    defLogo: Pms.emptyImgSrc,
    roomTypePhotos: [],
    roomTypeInfo: [],

    initComponent: function () {
        var me = this,
            booking = me.data,
            settings = me.settings.hotelInfo,
            baseRoom = booking.baseRoom,
            checkIn = Ext.Date.format(Pms.setDateFromMin(settings.checkIn), 'H:i'),
            checkOut = Ext.Date.format(Pms.setDateFromMin(settings.checkOut), 'H:i'),
            phone = settings.infoPhone ? settings.infoPhone : '&mdash;',
            email = settings.email ? settings.email : '&mdash;',
            description = settings.description ? settings.description : '&mdash;',
            customerEmail = booking.customerGroup.customer.email ? booking.customerGroup.customer.email : '_',
            roomTypeInfo = me.roomTypeInfo,
            hotelFacilities = me.hotelFacilities,
            roomTypeText = '<div style="font-size: 16px;">' + l('booking.roomType.facilities') + ':' + '<ul style="font-size: 14px; margin-left: 5px;">',
            hotelFacilitiesText = '<div style="line-height: 20px;"><h3 style="font-size: 16px;">' + l('booking.hotel.facilities') + ':' + '</h3><br/>',
            logo = settings.logo ? _('imagesUrlPrefix') + settings.logo : this.defLogo,
            roomTypePhoto = me.roomTypePhotos ? _('imagesUrlPrefix') + me.roomTypePhotos.code : this.defLogo;
        console.log(settings);
        for (var service in roomTypeInfo) {
            roomTypeText += '<li style="line-height: 20px;">' + l(roomTypeInfo[service].name);
        }
        ;
        for (var service in hotelFacilities) {
            hotelFacilitiesText += l(hotelFacilities[service].facility.name);
            if (service < hotelFacilities.length - 1) {
                hotelFacilitiesText += ', '
            }
            else {
                hotelFacilitiesText += '.'
            }
        }
        ;
        roomTypeText += '</ul></div>'
        hotelFacilities += '</div>'
        var html = '<!DOCTYPE html>' +
            '<html>' +
            '<head>' +
            '<meta charset="UTF-8" />' +
//            '<style>' +
//            'html {font: 100% Arial, Verdana, Tahoma;}' +
//            'body {margin: 0; font-size: 0.9rem;}' +
//            '.wrapper-invoice { padding: 30px; min-width: 1000px; margin: 0 auto; max-width: 1100px;}' +
//            '.hotel-logo {height: 150px; margin-left: 320px; margin-top: 20px; position: absolute; width: 150px;}' +
//            '.header-invoice {overflow: hidden;}' +
//            '.invoice-info {clear: both;}' +
//            '.column-header { border-bottom: 1px solid #000; font-size: 15px; padding-left: 10px !important;}' +
//            '.booking-small-header {font-size: 15px; color: indigo !important; padding-left: 10px !important;}' +
//            '.booking-info-first {width: 200px;}' +
//            '.wrapper-invoice td {padding-left: 25px;}' +
//            '.booking-general-info {border: 1px solid black; height: 100px; width: 100%; margin: 20px; padding-top: 10px;}' +
//            '.booking-sercondary-info {border: 1px solid black; width: 100%; height: 225px; margin: 20px; padding-top: 25px;}' +
//            '.wrapper-booking-info {float: left; font-family: arial, helvetica, verdana, sans-serif; font-size: 14px; position: relative; width: 450px; }' +
//            '.wrapper-hotel-info {float: right; font-size: 14px; margin: 20px; position: relative; width: 480px; }' +
//            '.hotel-info {border: 1px solid black; height: 200px; width: 100%;}' +
//            '.booking-table tr td {width: 200px; }' +
//            '.hotel-info-name {font-weight: bold; color: black; font-size: 18px; padding-bottom: 20px; }' +
//            '.hotel-basic-info{width: 300px; float: left; margin: 20px; }' +
//            '.hotel-basic-info td{width: 120px; padding-left: 10px; }' +
//            '.hotel-basic-info .hotel-info-big-td{ width: 250px }' +
//            '.hotel-customer-name{font-weight: bold; margin-left: 30px;}' +
//            '.hotel-other-info{ margin-left: 30px; margin-top: 5px; }' +
//            '.roomType-info{min-height: 100px; }' +
//            '.leftimg {float:left; min-height: 80px; margin: 7px 7px 7px 20px; }' +
//            '.roomType-info-name {font-weight: bold; color: black; font-size: 18px; padding-bottom: 20px; }' +
//            '.roomType-facilities {font-size: 16px;}' +
//            '.roomType-facilities ul {font-size: 14px; margin-left: 5px;}' +
//            '.roomType-facilities ul li {line-height: 20px;}' +
//            '.hotel-facilities {line-height: 20px;}' +
//            '.hotel-facilities h3 {font-size: 16px;}' +
//            'h2 {text-align: center;font-size: 28px}' +
//            '</style>' +
//            '<link rel="stylesheet" type="text/css" href="/themes/default/css/booking.css">' +
            '<title>' + l('booking') + '</title>' +
            '</head>' +
            '<body>' +
            '<div style="padding: 30px; min-width: 1000px; margin: 0 auto; max-width: 1100px;">' +
            '<h2 style="text-align: center;font-size: 28px">' + l('booking.approve') + '</h2>' +
            '<div style="float: left; font-family: arial, helvetica, verdana, sans-serif; font-size: 14px; position: relative; width: 450px;">' +
            '<div style="border: 1px solid black; height: 100px; width: 100%; margin: 20px; padding-top: 10px;">' +
            '<table border="0" cellspacing="0" cellpadding="5" style="padding-bottom: 10px;">' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("booking.number") + ':</td><td>' + booking.id + '</td></tr>' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("booking.date") + ':</td><td>' + Ext.Date.format(new Date(booking.createdDate * 1000), 'd/m/y') + '</td></tr>' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("booking.yourEmail") + ':</td><td>' + customerEmail + '</td></tr>' +
            '</table>' +
            '</div>' +
            '<div style="border: 1px solid black; width: 100%; height: 225px; margin: 20px; padding-top: 25px;">' +
            '<table border="0" cellspacing="0" cellpadding="5" >' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("booking.info") + ':</td><td>' + parseInt((booking.endDate - booking.startDate) / 86400) + ' ' + l('booking.nights') + '</td></tr>' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("checkIn") + ':</td><td>' + Ext.Date.format(new Date(booking.startDate * 1000), 'D d F Y') + ' ' + l('from') + ' ' + checkIn + '</td></tr>' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("checkOut") + ':</td><td>' + Ext.Date.format(new Date(booking.endDate * 1000), 'D d F Y') + ' ' + l('to') + ' ' + checkOut + '</td></tr>' +
            '<tr><td style="width: 200px; padding-left: 25px;">' + l("booking.baseRoom") + ':</td><td>' + baseRoom.adults + ' ' + l('booking.adults') + ' ' + baseRoom.children + ' ' + l('booking.children') + '</td></tr>' +
            '<tr><td style="width: 200px; padding-left: 25px;"><b>' + l('booking.totalPrice') + '</b></td><td><b>' + Ext.util.Format.number(booking.total / 100, '0.00') + '</b></td></tr>' +
            '</table>' +
            '</div>' +
            '<div style="min-height: 100px;">' +
            '<p><img src="' + roomTypePhoto + '" alt="roomType-photo" width="120" style="float:left;  margin: 7px 7px 7px 20px;">' +
            '<span style="font-weight: bold; color: black; font-size: 18px; padding-bottom: 20px;">' + booking.room.roomType.name + '</span><br/>' +
            roomTypeText + '</p>' +
            '</div>' +
            '<span style="font-weight: bold; margin-left: 30px;">' + l('booking.customerName') + ': ' + booking.customerGroup.customer.lastName + ' ' + booking.customerGroup.customer.firstName + '</span><br/>' +
            '<span style="margin-left: 30px; margin-top: 5px;">' + l('roomAndBoard') + ': ' + l(booking.plan.board) + '</span>' +
            '</div>' +
            '<div style="float: right; font-size: 14px; margin: 20px; position: relative; width: 490px;">' +
            '<div style="border: 1px solid black; min-height: 200px; width: 100%;">' +
            '<div style="float :right; margin-top: 20px; width: 150px;"><img src="' + logo + '" alt="logo-hotel" width="150" /></div>' +
            '<div style="width: 300px; margin: 20px; ">' +
            '<div style="font-weight: bold; color: black; font-size: 18px; padding-bottom: 20px;">' + settings.name + '</div>' +
            '<table>' +
            '<tr><td style="width: 120px; padding-left: 10px;">' + l("hotel.address") + ':</td><td>' + settings.officialAddress + '</td></tr>' +
            '<tr><td style="width: 120px; padding-left: 10px;>' + l("hotel.phone") + ':</td><td>' + phone + '</td></tr>' +
            '<tr><td style="width: 120px; padding-left: 10px;>' + l("hotel.email") + ':</td><td>' + email + '</td></tr>' +
            '<tr"><td style="width: 120px; padding-left: 10px;">' + l("hotel.description") + ':</td><td style="width: 200px;">' + description + '</td></tr>' +
            '</table>' +
            '</div>' +
            '</div>' +
            hotelFacilitiesText +
            '</div>' +
//            '<div class="hotel-logo">' +
//            '<img src="' + logo + '" alt="logo-hotel" width="170" height="150" />' +
//            '</div>' +
//            '<table border="0" cellspacing="0" cellpadding="5">' +
//            '<tr><td colspan="2" class="column-header"><b>' + l('booking.approve') + '</b></td></tr>' +
//            '<tr><td colspan="2" class="booking-small-header">' + l("booking.respectful") + ' ' + booking.customerGroup.customer.lastName + ' ' + booking.customerGroup.customer.firstName + '</td></tr>' +
//            '<tr><td colspan="2" class="booking-small-header"><b>' + l("booking.thankForBooking") + '</b></td></tr>' +
//            '<tr><td>' + l("hotel.name") + ':</td><td>' + settings.name + '</td></tr>' +
//            '<tr><td>' + l("booking.number") + ':</td><td>' + booking.id + '</td></tr>';
//        if (booking.customerGroup.customer.email) html += '<tr><td>' + l("booking.yourEmail") + ':</td><td>' + booking.customerGroup.customer.email + '</td></tr>';
//        html += '<tr><td colspan="2" class="column-header"><b>' + l('booking.shortInfo') + '</b></td></tr>' +
//            '<tr><td colspan="2" class="booking-small-header"><b>' + l('hotel.info') + '</b></td></tr>' +
//            '<tr><td class="booking-info-first">' + l("hotel.name") + ':</td><td>' + settings.name + '</td></tr>' +
//            '<tr><td>' + l("hotel.address") + ':</td><td>' + settings.officialAddress + '</td></tr>' +
//            '<tr><td>' + l("hotel.phone") + ':</td><td>' + phone + '</td></tr>' +
//            '<tr><td colspan="2" class="booking-small-header"><b>' + l('booking.info') + '</b></td></tr>' +
//            '<tr><td>' + l("status") + ':</td><td>' + me.useTypes.getById(booking.status).data.label + '</td></tr>' +
//            '<tr><td>' + l("booking.date") + ':</td><td>' + Ext.Date.format(new Date(booking.createdDate * 1000), 'd/m/y') + '</td></tr>' +
//            '<tr><td>' + l("checkIn") + ':</td><td>' + Ext.Date.format(new Date(booking.startDate * 1000), 'd/m/y') + '</td></tr>' +
//            '<tr><td>' + l("checkOut") + ':</td><td>' + Ext.Date.format(new Date(booking.endDate * 1000), 'd/m/y') + '</td></tr>' +
//            '<tr><td><b>' + l('booking.totalPrice') + '</b></td><td><b>' + Ext.util.Format.number(booking.total / 100, '0.00') + '</b></td class="booking-small-header"></tr>';
////        if (!Ext.isEmpty(booking.customerGroup.company)) {
////            html += '<tr><td colspan="2" class="column-header"><b>' + l("mainCompany") + '</b></td></tr>' +
////                '<tr><td>' + l("title") + ':</td><td>' + booking.customerGroup.company.name + '</td></tr>';
////            if (booking.customerGroup.company.address) html += '<tr><td>' + l("address") + ':</td><td>' + booking.customerGroup.company.address + '</td></tr>';
////            if (booking.customerGroup.company.city) html += '<tr><td>' + l("city") + ':</td><td>' + booking.customerGroup.company.city + '</td></tr>';
////            if (booking.customerGroup.company.country) html += '<tr><td>' + l("country") + ':</td><td>' + booking.customerGroup.company.country + '</td></tr>';
////            if (booking.customerGroup.company.phone) html += '<tr><td>' + l("phone") + ':</td><td>' + booking.customerGroup.company.phone + '</td></tr>';
////            if (booking.customerGroup.company.email) html += '<tr><td>' + l("email") + ':</td><td>' + booking.customerGroup.company.email + '</td></tr>';
////        }
////        else {
////            html += '<tr><td colspan="2" class="column-header"><b>' + l("mainGuest") + '</b></td></tr>' +
////                '<tr><td>' + l("booking.onName") + ':</td><td>' + booking.customerGroup.customer.lastName + ' ' + booking.customerGroup.customer.firstName + '</td></tr>';
////            if (booking.customerGroup.customer.address) html += '<tr><td>' + l("address") + ':</td><td>' + booking.customerGroup.customer.address + '</td></tr>';
////            if (booking.customerGroup.customer.city) html += '<tr><td>' + l("city") + ':</td><td>' + booking.customerGroup.customer.city + '</td></tr>';
////            if (booking.customerGroup.customer.country) html += '<tr><td>' + l("country") + ':</td><td>' + booking.customerGroup.customer.country + '</td></tr>';
////            if (booking.customerGroup.customer.phone) html += '<tr><td>' + l("phone") + ':</td><td>' + booking.customerGroup.customer.phone + '</td></tr>';
////            if (booking.customerGroup.customer.email) html += '<tr><td>' + l("email") + ':</td><td>' + booking.customerGroup.customer.email + '</td></tr>';
////        }
//
//        html += '<tr><td colspan="2" class="column-header"><b>' + l("room") + ' №' + booking.room.number + '</b></td></tr>' +
//            '<tr><td>' + l("roomType") + ':</td><td>' + booking.room.roomType.name + '</td></tr>' +
//            '<tr><td>' + l("virtualRoom") + ':</td><td>' + booking.virtualRoom.name + '</td></tr>' +
//            '<tr><td>' + l("virtualRoom.adults") + ':</td><td>' + booking.virtualRoom.adults + '</td></tr>' +
//            '<tr><td>' + l("virtualRoom.children") + ':</td><td>' + booking.virtualRoom.children + '</td></tr>' +
//            '<tr><td>' + l("virtualRoom.additional") + ':</td><td>' + booking.virtualRoom.additional + '</td></tr>' +
//            '</table>' +
            '</div>' +
            '</body>' +
            '</html>';
        ;
        me.items = [
            {
                xtype: 'panel',
                overflowY: 'scroll',
                html: html
            }
        ];
        me.buttons = [
            {
                text: l('booking.send'),
                scope: me,
                handler: function () {
                    function sendBookingView() {
                        console.log(html);
                        Pms.Ajax.request({
                            url: 'rest/mail',
                            method: 'POST',
                            jsonData: {
                                to: customerEmail,
                                subject: l('booking.approve'),
                                content: html
                            },
                            success: function () {
                                Pms.App.showNotification({
                                    message: l('bookingView.sent'),
                                    icon: Pms.notificationOk
                                });
                            }
                        })
                    }

                    if (customerEmail == '_') {
                        Ext.Msg.prompt('Email', l('bookingView.enterEmail'), function (btn, text) {
                            if (btn == 'ok') {
                                customerEmail = text;
                                sendBookingView()
                            }
                        })
                    }
                    else sendBookingView()

                }
            },
            {
                text: l('booking.print'),
                scope: me,
                handler: function () {
                    var el = document.createElement('div');
                    el.innerHTML = html
                    Pms.Print.print(el, true);
                }
            },
            {
                text: 'Ок',
                scope: me,
                handler: me.close
            }
        ]

        me.callParent(arguments);
    }
});