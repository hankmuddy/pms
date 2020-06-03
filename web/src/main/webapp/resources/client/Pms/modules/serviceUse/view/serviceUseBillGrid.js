Ext.define('Pms.modules.serviceUse.view.serviceUseBillGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.serviceUseBillGrid',
    store: 'Pms.modules.serviceUse.store.serviceUseBill',
    paging: false,
    requires: ['Pms.abstract.field.MoneyColumn'],
    overflowY: true,
    commitedBill: false,
    selType: 'checkboxmodel',

    listeners: {
        beforeselect: function (grid, rec) {
            return !rec.data.refund;

        }
    },
    initComponent: function () {
        var me = this,
            serviceTypes = [],
            measures = [];

        me.columns = [
            {
                header: l('service'),
                dataIndex: 'service',
                width: '40%',
                renderer: function (val) {
                    return val.title || l('living');
                }
            },
            {
                xtype: 'datecolumn',
                header: l('date'),
                dataIndex: 'date',
                flex: 1,
                format: 'd/m/y'
            },
            {
                header: l('unitPrice'),
                dataIndex: 'service',
                renderer: function (val) {
                    return val.price ? Ext.util.Format.number(val.price / 100, '0.00') : '';
                },
                flex: 1
            },
            {
                header: l('quantity'),
                dataIndex: 'quantity',
                flex: 1
            },
            {
                header: l('total'),
                xtype: 'moneycolumn',
                dataIndex: 'total',
                flex: 1
            },
            {
                header: l('refund'),
                dataIndex: 'refund',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                width: 70
            }
        ];
        me.callParent();
    }
});