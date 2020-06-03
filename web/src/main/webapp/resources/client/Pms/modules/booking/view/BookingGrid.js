Ext.define('Pms.modules.booking.view.BookingGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.bookingGrid',
    store: 'Pms.modules.roomUse.store.roomUse',
    enableColumnMove: false,
    useTypes: Ext.create('Pms.modules.roomUse.store.useType'),

    initComponent: function () {
        var me = this;

//        Pms.Ajax.request({
//            url: 'api/book?hotelId=test1',
//            method: 'POST',
//            async: false,
//            jsonData: {
//                "group":{
//                    "includeCustomer":true,
//                    "customer":{
//                        "firstName":"aaa",
//                        "lastName":"aaa",
//                        "country":"FO",
//                        "city":"aaa",
//                        "address":"aaa",
//                        "email":"valentin.vakatsiienko@idg.net.ua",
//                        "type":"adult"
//                    },
//                    "pov":"TOURISM"
//                },
//                "roomUses":[{
//                    "description":"",
//                    "baseRoom":{
//                        "id":"1",
//                        "type":"roomType"
//                    },
//                    "plan":{
//                        "id":"1"
//                    }
//                },{
//                    "description":"",
//                    "baseRoom":{
//                        "id":"1",
//                        "type":"roomType"
//                    },
//                    "plan":{
//                        "id":"1"
//                    }
//                }],
//                "startDate":1405090800,
//                "endDate":1405290800
//            }
//        });

        me.features = [
            {
                ftype: 'summary'
            }
        ];

        var paymentIconCls = Pms.App.isExtranet() === true ? 'display-none' : 'app-icon-priceing';
        var prolongIconCls = Pms.App.isExtranet() === true ? 'app-icon-edit1' : 'display-none';

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
                header: l('masterPerson'),
                dataIndex: 'customerGroup',
                flex: 1,
                renderer: function (val, row) {
                    if (val.company) return '<span style="text-decoration:underline;cursor:pointer;">' + val.company.name + '</span>';
                    else if (val.customer) return '<span style="text-decoration:underline;cursor:pointer;">' + val.customer.firstName + ' ' + val.customer.lastName + '</span>';
                    else return '&mdash;';
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
                header: l('plan'),
                dataIndex: 'plan',
                flex: 1,
                renderer: function (val, row) {
                    return val.name + ' (' + val.board + ')';
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
                header: l('virtualRoom'),
                dataIndex: 'baseRoom',
                flex: 1,
                renderer: function (val, row) {
                    return val.adults + ' / ' + val.children + ' / ' + val.additional
                }
            },
            {
                header: l('bookingDate'),
                dataIndex: 'createdDate',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                width: 100
            },
            {
                header: l('from'),
                dataIndex: 'startDate',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                width: 100
            },
            {
                header: l('to'),
                dataIndex: 'endDate',
                xtype: 'datecolumn',
                format: 'd/m/Y',
                summaryRenderer: function () {
                    return '<b>' + l('summary') + '</b>';
                },
                width: 100
            },
            {
                header: l('hotel.info.earlyCheckIn'),
                dataIndex: 'checkInTime',
                hidden: !_('hotel').earlyCheckInStart,
                renderer: function(val){
                    return Pms.checkForCheckInOut(true, val)
                },
                width: 100
            },
            {
                header: l('hotel.info.lateCheckOut'),
                dataIndex: 'checkOutTime',
                hidden: !_('hotel').lateCheckOutStart || Pms.App.isExtranet(),
                renderer: function(val){
                    return Pms.checkForCheckInOut(false, val)
                },
                width: 100
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
                width: 100
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
                width: 100
            },
            {
                header: l('approved'),
                xtype: 'booleancolumn',
                dataIndex: 'status',
                width: 50,
                renderer: function (val, row) {
                    row.tdAttr = 'data-qtip="' + l(val) + '"';
                    if (val == 'BOOKING_FREE' && row.record.data.approved) return '<img src="' + Pms.grayIcons + 'priceing.png"/>';
                    return "<img src='" + me.useTypes.getById(val).data.iconUrl + "'/>";
                }
            },
            {
                header: l('actions'),
                sortable: false,
                xtype: 'actioncolumn',
                sortable: false,
                width: 120,
                items: [
                    {
                        iconCls: 'app-icon-print',
                        tooltip: l('booking.print'),
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex);
                            this.up('grid').fireEvent('printbuttonclick', grid, rec, colIndex);
                        },
                    },
                    {
                        iconCls: paymentIconCls,
                        tooltip: l('makePayment'),
                        margin: '0 10px 0 0',
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('paymentactionclick', grid, rowIndex, colIndex, 3/*bill tab = 2*/);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return (rec.data.status == 'REFUSE');
                        }
                    },
                    {
                        iconCls: 'app-icon-commit',
                        tooltip: l('confirmReservation'),
                        margin: '0 10px 0 0',
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
                        margin: '0 10px 0 0',
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
                    {
                        iconCls: 'app-icon-outgo',
                        tooltip: l('outgo'),
                        margin: '0 10px 0 0',
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('outgoactionclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return (!(rec.data.status == 'LIVING' && rec.data.endDate < new Date(Pms.fromUTC(new Date(), true))));
                        }
                    },
                    {
                        iconCls: prolongIconCls,
                        tooltip: l('prolong'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('prolongactionclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return (rec.data.status == 'REFUSE');
                        }
                    },
                    {
                        iconCls: 'app-icon-delete',
                        tooltip: l('refuse'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('refuseactionclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return (rec.data.status == 'REFUSE');
                        }
                    }
                ]
            }
        ];
        me.callParent();
    }
});