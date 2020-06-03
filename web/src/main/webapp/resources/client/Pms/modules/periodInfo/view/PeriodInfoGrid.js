Ext.define("Pms.modules.periodInfo.view.PeriodInfoGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.periodInfoGrid',
    store: "Pms.modules.periodInfo.store.PeriodInfo",
    requires: ['Pms.abstract.field.MoneyColumn'],

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('periodInfo.roomType'),
                dataIndex: 'roomType',
                renderer: function (val) {
                    return val.name;
                },
                flex: 1
            },
            {
                header: l('periodInfo.dateStart'),
                xtype: 'datecolumn',
                dataIndex: 'dateStart',
                format: 'd/m/Y',
                flex: 1
            },
            {
                header: l('periodInfo.livingPrice'),
                dataIndex: 'livingPrice',
                xtype: 'moneycolumn',
                flex: 1
            }
        ];
        me.callParent(arguments);
    }
})
;