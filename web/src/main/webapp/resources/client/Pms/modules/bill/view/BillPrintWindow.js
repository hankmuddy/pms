Ext.define('Pms.modules.bill.view.BillPrintWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billPrintWindow',
    title: l('billPrintTitle'),
    width: 600,
    height: 400,
    bill: {},

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'billPrintGrid',
                bill: me.bill
            }
        ];

        me.buttons = [
            {
                text: l('printBill'),
                iconCls: 'app-icon-print',
                action: 'print'
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent(arguments);
    }
});