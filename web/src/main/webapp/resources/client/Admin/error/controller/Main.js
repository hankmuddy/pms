Ext.define('Admin.error.controller.Main', {
    extend: 'Admin.generic.app.Controller',
    card: 'error',
    name: 'error',
    defaultAction: 'notFound',
    loadPage: function (action) {
        var panel = this.panel = Ext.ComponentQuery.query('#' + this.card)[0]
        this[action]();
        var contentWrapper = panel.up('#contentwrapper');
//        contentWrapper.getLayout().setActiveItem(this.card);
        var viewport = panel.up('viewport');
        viewport.setVisible(true);
    },
    notFound: function () {
        // history.replaceState(null, null, '#' + this.name + '/not_found');
//        this.panel.getEl().setHTML('<h1 style="margin-left: 30px">404 Not Found</h1>');
        Ext.Msg.alert('Error', '404 Not Found');
    },
    serverCrash: function () {
        // history.replaceState(null, null, '#' + this.name + '/not_found');
        Ext.Msg.alert('Error', '500 Internal Server Error');
//        this.panel.getEl().setHTML('<h1 style="margin-left: 30px">500 Internal Server Error</h1>');
    }
});
