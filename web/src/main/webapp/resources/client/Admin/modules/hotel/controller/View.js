Ext.define('Admin.modules.hotel.controller.View', {
    extend: 'Admin.generic.app.controller.View',
    modelClass: 'Admin.modules.hotel.Model',
    viewClass: 'Admin.modules.hotel.view.View',
    setBreadcrumbs: function (record, conf) {
        this._title = 'name';
        this.callParent(arguments);
    },
    convertBeforeRender: function (record) {
        var hoursIn = (record.data.info.checkIn / 60).toFixed(),
            minutesIn = (record.data.info.checkIn - hoursIn * 60) || '00',
            hoursOut = (record.data.info.checkOut / 60).toFixed(),
            minutesOut = (record.data.info.checkOut - hoursOut * 60) || '00';
        record.data.checkIn = hoursIn + ':' + minutesIn;
        record.data.checkOut = hoursOut + ':' + minutesOut;
        record.data.earlyCheckIn = hoursIn + ':' + minutesIn;
        record.data.lateCheckOut = hoursOut + ':' + minutesOut;
        record.data.index = record.data.info.index;
        record.data.postAddress = record.data.info.postAddress;
        record.data.officialAddress = record.data.info.officialAddress;
        record.data.bookPhone = record.data.info.bookPhone;
        record.data.accountPhone = record.data.info.accountPhone;
        record.data.infoPhone = record.data.info.infoPhone;
        record.data.email = record.data.info.email;
        record.data.webSite = record.data.info.webSite;
        record.data.description = record.data.info.description;
        record.data.multiplier = record.data.info.multiplier;
        record.data.name = record.data.info.name;
        record.data.timeZone = record.data.info.timeZone;
        record.data.tourismTax = record.data.info.tourismTax;
        record.data.vat = record.data.info.vat;
        record.data.city = record.data.info.city;
        record.data.country = record.data.info.country;
        return record;
    }
});
