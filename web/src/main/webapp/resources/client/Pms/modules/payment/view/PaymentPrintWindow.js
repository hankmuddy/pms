Ext.define('Pms.modules.payment.view.PaymentPrintWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.paymentPrintWindow',

    title: l('checkPrint'),
    width: 370,
    height: 355,
    autoShow: false,

    payment: null,

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'paymentPrintGrid',
                bill: me.bill,
                payment: me.payment
            }
        ];

        me.buttons = [
            {
                text: l('bill.printCheck'),
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