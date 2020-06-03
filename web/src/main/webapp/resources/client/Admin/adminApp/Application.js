Ext.define('Admin.adminApp.Application', {
    extend: 'Admin.generic.app.Application',
    appName: 'adminApp',
    defaultAction: {
        controller: "Admin.modules.hotel.controller.Main",
        action: "list"
    },
    route: function (ctrl_name) {
        var routeMap = {};
        Ext.Object.merge(routeMap, this.routeMap, {
            hotel: 'Admin.adminApp.hotel.controller.Main',
            user: 'Admin.modules.user.controller.Main',
            profile: 'Admin.modules.profile.controller.Main',
            bookingButton: 'Admin.modules.bookingButton.controller.Main'
        });
        return routeMap[ctrl_name];
    }
}, function () {
    Ext.application('Admin.adminApp.Application');
});
