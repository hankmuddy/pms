Ext.define('Pms.modules.bill.view.BillRefundWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billRefundWindow',
    title: l('return'),
    width: 500,
    bill: null,

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'serviceUseBillGrid',
                region: 'center',
                border: true,
                commitedBill: me.bill.approved,
                loadParams: {
                    params: {
                        filter: [
                            {
                                field: 'bill.id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: me.bill.id
                                }
                            }
                        ]
                    }
                }
            }
        ];
        me.buttons = [
            {
                text: l('makeReturn'),
                action: 'refund'
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