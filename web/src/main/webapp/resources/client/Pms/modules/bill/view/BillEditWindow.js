Ext.define('Pms.modules.bill.view.BillEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billEditWindow',
    title: l('billEdit'),
    width: 600,
    height: 500,
    autoShow: false,
    layout: 'border',

    bill: {},

    initComponent: function () {
        function inArray(el, arr) {
            for (var i in arr) if (arr[i] === el) return true;
            return false;
        }

        var me = this,
            group = me.bill.customerGroup,
            customer = group.customer;

        me.items = [
            {
                xtype: 'billForm',
                height: 150,
                region: 'north'
            },
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