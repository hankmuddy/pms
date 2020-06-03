Ext.define("Pms.modules.serviceUse.view.serviceUseGroupGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.serviceUseGroupGrid',
    store: 'Pms.modules.serviceUse.store.serviceUseBillDiscount',
    requires: ['Pms.modules.groupRoomUse.view.customerGroupDiscountWindow', 'Pms.abstract.field.MoneyColumn'],

    enableColumnMove: false,
    data: {},
    disableSelection: true,
    initComponent: function () {
        var me = this,
            serviceTypes = [],
            measures = [],
            totalDiscount = 0;

        me.store.groupField = 'bill.id';

        me.features = [
            {
                ftype: 'summary'
            },
            {
                ftype: 'grouping',
                hideGroupedHeader: true,
                startCollapsed: false,
                groupHeaderTpl: '<tpl if="name == null">'+ l('serviceUse.withoutBill') +'</tpl>' +
                    '<tpl if="name != null">' + l('bill') + ' №{[values.rows[0].data.bill.id]} ({[values.rows[0].data.bill.total/100]}) ' +
//                '<tpl if="values.rows[0].data.bill.property_formed_fully == true">' +
                    '<tpl if="values.rows[0].data.bill.totalPaid == values.rows[0].data.bill.total">' +
                    '<font color="#31B404"> - ' + l('fullyPaid') + '</font>' +
                    '</tpl>' +
                    '<tpl if="values.rows[0].data.bill.totalPaid != values.rows[0].data.bill.total">' +
                    '<font color="#FACC2E"> - '+ l('serviceUse.billFormed') + '</font>' +
                    '</tpl>' +
                    '<tpl if="values.rows[0].data.bill.roomUse != null">: ' + l('room') + ' {[values.rows[0].data.bill.roomUse.room.number]}</tpl>' +
                    '<tpl if="values.rows[0].data.bill.roomUse == null">: ' + l('roomUse.serviceUse.toGroup') + '</tpl>' +
//                '</tpl>' +
                    '</tpl>'
            }
        ];

        me.columns = [
//            {
//                xtype: 'rownumberer',
//                header: '№',
//                width: 35,
//                sortable: false,
//                shrinkWrap: 3,
//                renderer: function (value, meta, record) {
//                    return record.index + 1;
//                }
//            },
            {
                header: l('service'),
                dataIndex: 'service',
                width: '35%',
                renderer: function (val, row) {
//                    return val.service.type == 'living' ? serviceTypes[val[0].property_service_type] + ' (' + val[0].property_name + ')' : val[0].property_name;
                    return l(val.title) || l('living') + ' (' + val.type + ')';
                },
                summaryRenderer: function () {
                    return '<b>'+ l('summary') +'</b>';
                }
            },
            {
                header: l('date'),
                xtype: 'datecolumn',
                dataIndex: 'date',
                flex: 1,
                format: 'd/m/y'
            },
            {
                header: l('unitPrice'),
                xtype: 'moneycolumn',
                dataIndex: 'rawTotal',
                summaryType: 'sum',
                renderer: function (val, col) {
                    var data = col.record.data;
                    return Ext.util.Format.number(val ? val / data.quantity / 100 : 0, '0.00');
                    ;
                },
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '';
                },
                flex: 1
            },
            {
                header: l('quantity'),
                dataIndex: 'quantity',
                flex: 1,
                renderer: function (val, col) {
                    var rec = col.record.data;
                    return val;
                }
            },
            {
                header: l('total'),
                xtype: 'moneycolumn',
                dataIndex: 'total',
                flex: 1,
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                }
            },
            {
                header: l('discount'),
                xtype: 'moneycolumn',
                dataIndex: 'discount',
                flex: 1,
                sortable: false,
                summaryType: 'sum',
                renderer: function (val, col) {
                    var rec = col.record.data,
                        tourismTax = 0;
                    if (rec.type == 'livingUse' && rec.tourismTax > 0) {
                        tourismTax = _('hotel').tourismTaxFromFullPrice ? Math.ceil(rec.tourismTax * rec.rawTotal / 100) : Math.ceil(rec.tourismTax * rec.livingAmount / 100);
                    }
                    return Ext.util.Format.number(rec.type == 'livingUse' ? ((rec.rawTotal - rec.total + tourismTax) / 100) : 0, '0.00');
                },
                summaryRenderer: function (value, summaryData, dataIndex) {
//                    var total = dataIndex.data.total,
//                        rawTotal = dataIndex.data.rawTotal;
//                    console.log(dataIndex.data)
//                    return Ext.util.Format.number((rawTotal - total) / 100, '0.00');
                    return ''
                }
            },
            {
                header: l('serviceUse.tourismTaxShort'),
                dataIndex: 'tourismTax',
                sortable: false,
                summaryType: 'sum',
                width: 40,
                renderer: function (val, col) {
                    if (val < 0){
                        return '0.00'
                    }
                    var rec = col.record.data;
                    var tax =_('hotel').tourismTaxFromFullPrice ? Math.ceil(val * rec.rawTotal / 100) / 100 : Math.ceil(val * rec.livingAmount / 100) / 100; //convert to money and / 100%
                    return Ext.util.Format.number(tax, '0.00');
                },
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '';
                }
            },
            {
                header: l('serviceUse.bill'),
                dataIndex: 'bill',
                width: 40,
                renderer: function (val) {
                    return val ? Pms.iconOk : null;
                }
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                width: 50,
                items: [
                    {
                        iconCls: 'app-icon-edit2',
                        tooltip: l('change'),
                        handler: function (grid, rowIndex, colIndex) {
                            var store = grid.getStore(),
                                rec = store.getAt(rowIndex),
                                serviceUse = rec.data,
                                serviceType = serviceUse.service.type;

                            if (serviceType == 'living') {
                                Ext.Msg.alert(l('serviceUse.editing'), l('serviceUse.canNotEdit'));
                            }
                            else {
                                var win = grid.up('window'),
                                    win = Ext.widget('serviceUseEditWindow', {
                                        serviceUse: serviceUse
                                    }),
                                    form = win.down('serviceUseForm');
                                win.down('serviceUseForm').getForm().setValues(rec.data);
                                win.show();
                            }
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return true;
                        }
                    },
                    {
                        iconCls: 'app-icon-remove',
                        tooltip: l('delete.btn'),
                        handler: function (grid, rowIndex, colIndex) {
                            var store = grid.getStore(),
                                rec = store.getAt(rowIndex),
                                bill = rec.data.property_bill,
                                catalogItem = rec.data.property_catalog_item[0],
                                service_type = catalogItem.property_service_type;

                            if (service_type == 'room_rate') {
                                Ext.Msg.alert(l('serviceUse.deleting'), l('serviceUse.canNotDelete'));
                            }
                            else if (!bill) {
                                Ext.Msg.confirm(l('serviceUse.deleting'), l('confirmation'), function (btn) {
                                    if (btn === 'yes') {
                                        store.remove(rec);
                                        store.sync();
                                    }
                                });
                            }
                            else {
                                Ext.Msg.alert(l('serviceUse.deleting'), l('serviceUse.formedCanNotBeDeleted'));
                            }
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            var bill = rec.data.bill,
                                service_type = rec.data.service.type;
                            return (bill || service_type == 'room_rate') ? true : false;
                        }
                    }
                ]
            }
        ];

        me.tbar = [
            {
                iconCls: 'app-icon-bill',
                text: l('person.setDiscount'),
                handler: function (button) {
                    var isGroup = !button.up('window').down('button[cls=group-room-use-change-btn]').filterToGroup;
                    var win = Ext.widget('customerGroupDiscountWindow', {
                        roomUseId: me.data.id,
                        isGroupForm: isGroup,
                        groupId: me.data.customerGroup.id
                    });
                    win.down('form').down('hidden').setValue(me.data.customerGroup.id);
                    win.show();
                }
            },
            '->',
            {
                iconCls: 'app-icon-bill',
                text: l('booking.detailedServices'),
                handler: function (button) {
                    Ext.create('Ext.window.Window', {
                        title: l('booking.detailedServices'),
                        width: 1000,
                        height: 600,
                        maximizable: true,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'component',
                                height: '100%',
                                width: '100%',
                                html: '<object width="100%" height="100%" data="report/folio?roomUseId=' + me.data.id + '"></object>'
                            }
                        ]
                    }).show();
                }
            }
        ];
        me.callParent();
    }
});