Ext.define("Pms.abstract.ApprovedForm", {
    extend: "Ext.form.Panel",
    columns: 1,
    border: false,
    defaultType: 'displayfield',
    fieldDefaults: {
        labelAlign: 'left',
        msgTarget: 'side'
    },
    layout: 'anchor',
    bodyPadding: 5,
    autoScroll: true,

    initComponent: function () {
        this.layout.columns = this.columns;
        this.callParent();
    }
});