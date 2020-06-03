Ext.define('Pms.modules.payment.view.PaymentEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.paymentEditWindow',

    title: l('paymentEditor'),
    width: 370,
    height: 355,
    autoShow: false,

    bill: null,

    initComponent: function () {
        var me = this,
            payer = me.bill.customerGroup.customer ? me.bill.customerGroup.customer.lastName + ' ' + me.bill.customerGroup.customer.firstName : '',
            paid = 0;

        me.items = [
            {
                xtype: 'propertygrid',
                hideHeaders: true,
                editable: false,
                source: {
                    id: me.bill.id,
                    billTotal: me.bill.total,
                    customer: payer
                },
                sourceConfig: {
                    id: {displayName: l('billNum')},
                    billTotal: {displayName: l('amount')},
                    customer: {displayName: l('guestPayer')}
                },
                listeners: {
                    beforeedit: function () {
                        return false
                    }
                }
            },
            {
                xtype: 'paymentForm',
                bill: me.bill
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                iconCls: 'app-icon-ok',
                action: 'save',
                requestDisable: true
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