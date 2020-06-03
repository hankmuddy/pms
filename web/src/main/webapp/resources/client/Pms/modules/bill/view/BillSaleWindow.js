Ext.define('Pms.modules.bill.view.BillSaleWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billSaleWindow',
    title: l('setAccount'),
    width: 600,
    height: 500,
    filterIds: [],
    persons: [],
    filter: {},

    initComponent: function () {
        var me = this;

        if (me.filterIds.length) {
            me.filter.id = {in: me.filterIds};
        }

        me.items = [
            {
                xtype: 'billForm',
                persons: me.persons
            },
            {
                xtype: 'serviceUseBillGrid',
                height: 221,
                loadParams: {
                    params: {
                        filter: me.filter
                    }
                }
            }
        ];

        me.buttons = [
            {
                text: l('bill'),
                iconCls: 'app-icon-bill',
                action: 'sale'
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ]

        me.callParent(arguments);
    }
});