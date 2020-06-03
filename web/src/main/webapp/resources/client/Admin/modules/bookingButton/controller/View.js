Ext.define('Admin.modules.bookingButton.controller.View', {
    extend: 'Admin.generic.app.controller.View',
    modelClass: 'Admin.modules.bookingButton.Model',
    viewClass: 'Admin.modules.bookingButton.view.View',
    convertBeforeRender: function (record) {
        var hotel = record.data.hotel,
            supervisor = record.data.supervisor;
        hotel = hotel ? hotel.info.name + ' (' + hotel.hotelId + ')' : '&mdash;';
        record.data.hotel = hotel;
        supervisor = supervisor ? supervisor.username : null;
        record.data.supervisor = supervisor;
        return record
    },
//    setBreadcrumbs: function (record, conf) {
//        this._title = 'username';
//        this.callParent(arguments);
//    },
});
