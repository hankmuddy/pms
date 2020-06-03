Ext.define("Pms.abstract.Tree", {
    extend: "Ext.tree.Panel",
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*'
    ],

    initComponent: function () {
        var me = this;
        me.callParent();
    }
});