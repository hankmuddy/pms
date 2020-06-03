Ext.define("Pms.desktop.LoadingModule", {
    extend: "Pms.abstract.ModalWindow",
    bodyCls: "pms-loading-module",
    layout: "auto",
    width: 400,
    height: 240,
    closable: false,

    initComponent: function () {
        var me = this;
        me.html = "<p>" + l('loading.message') + "</p>";
        me.callParent();
    }
});