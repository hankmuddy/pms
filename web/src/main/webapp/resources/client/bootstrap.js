Ext.Ajax.request({
    url: 'labels',
    async: false,
    success: function (response) {
        g_aLabels = JSON.parse(response.responseText);
    }
});

Ext.Loader.setConfig({
    enabled: true,
    disableCaching: true,
    paths: {
        Pms: 'client/Pms',
        Ext: 'client/Ext/src',
        Sch: 'client/Sch',
//        SchLocale: 'Sch.locale.' + lang,
        Admin: 'client/Admin'
    }
});

Ext.require('Pms.desktop.Application');
Ext.require('Ext.ux.clone');
Ext.require('Pms.abstract.field.pmsdatefield');



Ext.data.Types.UTCDATE = {
    convert: function (v, rec) {
        return Pms.convertUTC(v);
    },
    sortType: function (v) {
        return v;
    },
    type: 'UTCDate'
};

Ext.onReady(function () {

    Pms.App = Ext.create('Pms.desktop.Application', {
        name: "Pms.App",
        listeners: {
            ready: function () {
                setTimeout(function () {
                    Ext.get("loading").remove();
                    Ext.get("loading-mask").fadeOut({
                        remove: true
                    });
                }, 250);
            }
        }
    });

    connectWebSocket(0);
});