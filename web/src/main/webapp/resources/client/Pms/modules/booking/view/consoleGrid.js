Ext.define('Pms.modules.booking.view.consoleGrid', {
    extend: "Pms.abstract.Grid",
    alias: 'widget.consoleGrid',
    store: 'Pms.modules.roomUse.store.roomUse',
    requires: ['Pms.abstract.field.MoneyColumn'],

    useTypes: Ext.create('Pms.modules.roomUse.store.useType'),
    enableColumnMove: false,

    actionItems: [],

    initComponent: function () {
        var me = this,
            sourceTypes = [],
            actions = [
                { // 0
                    iconCls: 'app-icon-priceing',
                    tooltip: l('makePayment'),
                    margin: '0 5px 0 0',
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('paymentactionclick', grid, rowIndex, colIndex, 3);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return (rec.data.status == 'REFUSE');
                    }
                },
                { // 1
                    iconCls: 'app-icon-commit',
                    tooltip: l('confirmReservation'),
                    margin: '0 5px 0 0',
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('confirmactionclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return (rec.data.status != 'BOOKING_FREE');
                    }
                },
                {
                    iconCls: 'app-icon-income',
                    tooltip: l('arrival'),
                    margin: '0 5px 0 0',
                    handler: function (grid, rowIndex, colIndex) {
                        var store = grid.getStore(),
                            rec = store.getAt(rowIndex),
                            gruData = rec.data;

                        function showWin(gruData) {
                            var win = Ext.widget('groupRoomUseIncomeWindow', {data: gruData}),
                                gruForm = win.down('groupRoomUseIncomeForm');

                            gruForm.getForm().setValues(gruData);
                            win.show();
                        }

                        showWin(gruData);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return (!(rec.data.status == 'BOOKING_WARRANTY' && Ext.Date.format(new Date(rec.data.startDate), 'U') <= Ext.Date.format(Ext.Date.clearTime(new Date()), 'U')));
                    }
                },
                {// 3
                    iconCls: 'app-icon-outgo',
                    tooltip: l('departure'),
                    margin: '0 5px 0 0',
                    handler: function (grid, rowIndex, colIndex) {
                        var store = grid.getStore(),
                            rec = store.getAt(rowIndex),
                            win = Ext.widget('groupRoomUseOutgoWindow', {data: rec.data}),
                            gruForm = win.down('groupRoomUseOutgoForm'),
                            gruData = rec.data;
                        gruForm.getForm().setValues(gruData);
                    }
                },
                { // 4
                    iconCls: 'app-icon-delete',
                    tooltip: l('refuse'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('refuseactionclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return (rec.data.status == 'REFUSE');
                    }
                }
            ],
            actionItems = [];

        Ext.each(me.actionItems, function (key) {
            actionItems.push(actions[key]);
        });

        me.features = [
            {
                ftype: 'summary'
            }
        ];

        me.columns = [
            {
                header: l('bookingNumber'),
                dataIndex: 'id',
                width: 50
            },
            {
                header: l('room'),
                dataIndex: 'room',
                width: 50,
                renderer: function (val, row) {
                    return val.number;
                }
            },
            {
                header: l('roomType'),
                dataIndex: 'room',
                flex: 1,
                renderer: function (val, row) {
                    return val.roomType.name;
                }
            },
            {
                header: l('mainOfGroup'),
                dataIndex: 'customerGroup',
                flex: 1,
                renderer: function (val, row) {
                    if (val.company) return '<span style="text-decoration:underline;cursor:pointer;">' + val.company.name + '</span>';
                    else if (val.customer) return '<span style="text-decoration:underline;cursor:pointer;">' + val.customer.firstName + ' ' + val.customer.lastName + '</span>';
                    else return '&mdash;';
                }
            },
            {
                header: l('booking.Source'),
                dataIndex: 'source',
                flex: 1,
                renderer: function (val, row) {
                    return l(val)
                }
            },
            {
                header: l('bookingDate'),
                dataIndex: 'createdDate',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                width: 75
            },
            {
                header: l('from'),
                dataIndex: 'startDate',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                width: 75
            },
            {
                header: l('to'),
                dataIndex: 'endDate',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                summaryRenderer: function () {
                    return '<b>' + l('summary') + '</b>';
                },
                width: 75
            },
            {
                header: l('total'),
                xtype: 'moneycolumn',
                dataIndex: 'total',
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    var records = me.getStore().data.items;
                    if(!Ext.isEmpty(me.rowFilter)) {
                        for(var i in records) {
                            if(!me.rowFilter(records[i])) {
                                value = value - records[i].data.total;
                            }
                        }
                    }
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                },
                width: 60
            },
            {
                header: l('totalPaid'),
                xtype: 'moneycolumn',
                dataIndex: 'totalPaid',
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    var records = me.getStore().data.items;
                    if(!Ext.isEmpty(me.rowFilter)) {
                        for(var i in records) {
                            if(!me.rowFilter(records[i])) {
                                value = value - records[i].data.totalPaid;
                            }
                        }
                    }
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                },
                width: 60
            },
            {
                header: l('approved'),
                dataIndex: 'status',
                width: 40,
                renderer: function (val, row) {
//                    console.log(row.record.data.endDate);
                    row.tdAttr = 'data-qtip="' + l(val) + '"';
                    if (val == 'BOOKING_FREE' && row.record.data.approved) return '<img src="' + Pms.grayIcons + 'priceing.png"/>';
                    if (val == 'LIVING' && row.record.data.endDate <= new Date()) return '<img src="' + Pms.grayIcons + 'User-blue-icon.png"/>';
                    return '<img src="' + me.useTypes.getById(val).data.iconUrl + '"/>';
                }
            },
            {
                header: l('actions'),
                xtype: 'actioncolumn',
                sortable: false,
                width: 70,
                items: actionItems
            }
        ];
        me.callParent();
    }
});