Ext.define('Pms.modules.groupRoomUse.view.groupBookingGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.groupBookingGrid',

    features: [
        {
            ftype: 'summary'
        }
    ],

    storeData: [],

    initComponent: function () {
        var me = this;

        me.store = Ext.create('Ext.data.ArrayStore', {
            fields: ['room', 'number', 'roomType', 'capacity', 'property_startDate', 'property_endDate'],
            data: me.storeData
        });

        me.store.loadData(me.storeData);

        me.columns = [
            {
                header: l('room'),
                dataIndex: 'number',
                width: 50,
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>'+ l('totalGrid') + '</b>';
                }
            },
            {
                header: l('roomType'),
                dataIndex: 'roomType',
                flex: 1,
                summaryType: 'count',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + value + ' ' + l('roomUse.roomGrid') + ' </b>';
                }
            },
            {
                header: l('capacity'),
                dataIndex: 'capacity',
                width: 80,
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + value + l('roomUse.places') + ' </b>';
                }
            },
            {
                xtype: 'datecolumn',
                header: 'C',
                dataIndex: 'property_startDate',
                format: 'd/m/y',
                width: 80
            },
            {
                xtype: 'datecolumn',
                header: 'По',
                dataIndex: 'property_endDate',
                format: 'd/m/y',
                width: 80
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                width: 40,
                items: [
                    {
                        iconCls: 'app-icon-remove',
                        tooltip: l('roomUse.remove'),
                        handler: function (grid, rowIndex, colIndex) {
                            var store = grid.getStore(),
                                rec = store.getAt(rowIndex);
                            store.remove(rec);
                        }
                    }
                ]
            }
        ];
        me.callParent();
    }
});