Ext.define('Admin.supervisorApp.Application', {
    extend: 'Admin.generic.app.Application',
    appName: 'supervisorApp',
    defaultAction: {controller: "Admin.modules.hotel.controller.Main", action: "list"},
    route: function (ctrl_name) {
        var routeMap = {};
        Ext.Object.merge(routeMap, this.routeMap, {
            hotel: 'Admin.modules.hotel.controller.Main',
            user: 'Admin.supervisorApp.user.controller.Main'
        });
        return routeMap[ctrl_name];
    }
}, function () {
    Ext.application('Admin.supervisorApp.Application');
});
