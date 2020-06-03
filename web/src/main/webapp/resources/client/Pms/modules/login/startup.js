Ext.Loader.setConfig({
    enabled: true,
    paths: {
        Pms: '/client/Pms'
    }
});

Ext.require("Pms.modules.login.LoginForm");

Ext.onReady(function () {
    var win = Ext.create("Pms.modules.login.LoginWindow");
    win.show();

    Ext.get("loading").remove();
});