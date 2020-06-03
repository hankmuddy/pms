Ext.define('Pms.modules.payment.view.PaymentAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.paymentAddWindow',
    title: l('makingPayment'),
    width: 370,
    height: 350,
    autoShow: false,
    resizable: false,
    layout: null,
    data: {},

    initComponent: function () {
        var me = this,
            customerGroup = me.data.group ? me.data.group : me.data.roomUse.customerGroup,
            customer = (customerGroup && customerGroup.customer) ? customerGroup.customer.lastName + ' ' + customerGroup.customer.firstName : null,
            company = (customerGroup && customerGroup.company) ? customerGroup.company.name : null,
            payer = company ? company : customer;

        me.items = [
            {
                xtype: 'propertygrid',
                hideHeaders: true,
                editable: false,
                source: {
                    id: me.data.id,
                    total: Ext.util.Format.number(me.data.total / 100, '0.00'),
                    method: me.method,
                    customer: payer
                },
                sourceConfig: {
                    id: {displayName: l('bill') + ' №'},
                    total: {displayName: l('amount')},
                    method: {displayName: l('billType')},
                    customer: {displayName: l('payer')}
                },
                listeners: {
                    beforeedit: function () {
                        return false
                    }
                }
            },
            {
                xtype: 'paymentForm'
            }
        ];

        me.buttons = [
            {
                text: l('make.btn'),
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