Ext.define('Pms.desktop.Window', {
    extend: "Ext.window.Window",
    requires: [
        "Ext.ux.statusbar.StatusBar"
    ],

    title: "PMS",
    stateful: false,
    isWindow: true,
    minimizable: true,
    maximizable: true,
    width: 1000,
    height: 600,
    layout: "fit",
//    iconCls: "icon-cloud",
    constrain: true,
    bodyPadding: 2,

    initComponent: function () {
        var me = this;
        this.dockedItems = [this.statusBar];
        me.callParent();
    },

    // dblclick can call again...
    doClose: function () {
        this.doClose = Ext.emptyFn;
        this.el.disableShadow();
        this.el.fadeOut({
            listeners: {
                scope: this,
                afteranimate: function () {
                    this.destroy();
                }
            }
        });
    }
});