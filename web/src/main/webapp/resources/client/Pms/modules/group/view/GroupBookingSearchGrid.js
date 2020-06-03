Ext.define('Pms.modules.group.view.GroupBookingSearchGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupBookingSearchGrid',
    store: 'Pms.modules.group.store.Group',
    requires: ['Pms.abstract.field.MoneyColumn'],
    listeners: {
        itemdblclick: function(grid, record, item) {
            var win = Ext.widget('groupedBookingWindow', {customerGroupId: record.getId()});
            win.show();
        }
    },
    initComponent: function() {
        var me = this;

        me.features = [
            {
                ftype: 'summary'
            },
        ];
        me.columns = [
            {
                header: l('group.Number'),
                dataIndex: 'id',
                width: 50
            },
            {
                header: l('masterPerson'),
                dataIndex: 'customer',
                flex: 2,
                renderer: function(val, row) {
                    var data = row.record.data;
                    if(data.company) return data.company.name;
                    else return val.firstName + ' ' + val.lastName;
                }
            },
            {
                header: l('discount'),
                dataIndex: 'discount',
                flex: 1,
                renderer: function(val) {
                    return val + ' %'
                },
                summaryRenderer: function() {
                    return '<b>' + l('summary') + '</b>';
                }
            },
            {
                header: l('total'),
                xtype: 'moneycolumn',
                dataIndex: 'total',
                summaryType: 'sum',
                flex: 1,
                summaryRenderer: function(value, summaryData, dataIndex) {
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                }

            },
            {
                header: l('totalPaid'),
                xtype: 'moneycolumn',
                dataIndex: 'totalPaid',
                summaryType: 'sum',
                flex: 1,
                summaryRenderer: function(value, summaryData, dataIndex) {
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                }
            },
            {
                header: l('totalLeft'),
                dataIndex: 'totalLeft',
                renderer: function(val, raw) {
                    var totalPaid = raw.record.data.totalPaid;
                    var total = raw.record.data.total;
                    return Ext.util.Format.number((total - totalPaid) / 100, '0.00')
                },
                flex: 1
            }
        ];
        me.callParent();
    }
});