Ext.define('Admin.managerApp.Application', {
    extend: 'Admin.generic.app.Application',
    appName: 'managerApp',
    defaultAction: {controller: "Admin.modules.hotel.controller.Main", action: "list"},
    route: function (ctrl_name) {
        var routeMap = {};
        Ext.Object.merge(routeMap, this.routeMap, {
            hotel: 'Admin.modules.hotel.controller.Main',
            user: 'Admin.managerApp.user.controller.Main'
        });
        return routeMap[ctrl_name];
    }
}, function () {
    Ext.application('Admin.managerApp.Application');
});
