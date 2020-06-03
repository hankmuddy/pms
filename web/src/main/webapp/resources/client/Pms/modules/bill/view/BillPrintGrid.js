Ext.define("Pms.modules.bill.view.BillPrintGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.billPrintGrid',
    store: 'Pms.modules.bill.store.Bill',

    paging: false,

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('billNumber'),
                dataIndex: 'id',
                width: 50
            },

            {
                header: l('payer'),
                dataIndex: 'customerGroup',
                flex: 1,
                renderer: function (val) {
                    return val.customer.lastName + ' ' + val.customer.firstName;
                }
            },
            {
                header: l('amount'),
                dataIndex: 'total',
                xtype: 'numbercolumn',
                flex: 1
            }
        ];

        me.callParent();
        me.getStore().filter([
            {
                filterFn: function (item) {
                    return item.getId() == me.bill.id;
                }
            }
        ]);
    }
});