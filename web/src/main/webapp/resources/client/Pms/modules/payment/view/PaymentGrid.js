Ext.define("Pms.modules.payment.view.PaymentGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.paymentGrid',
    xtype: 'row-expander-grid',
    store: 'Pms.modules.payment.store.Payment',
    enableColumnMove: false,
    requires: ['Pms.abstract.field.MoneyColumn'],
    plugins: [
        {
            ptype: 'rowexpander',
            rowBodyTpl: new Ext.XTemplate(
                    '<span style="margin-left: 20px"><b>' + l('description') + '</b>: {description}</span>'
            )
        }
    ],

//    features: [{
//         ftype: 'summary'
//    }],

//    periodSelection: false,
//    periodStart: Pms.toUTC(Ext.Date.format(Ext.Date.add(Ext.Date.clearTime(new Date()), Ext.Date.MONTH, -1), 'U')),
//    periodEnd: Pms.toUTC(Ext.Date.format(Ext.Date.add(Ext.Date.clearTime(new Date()), Ext.Date.DAY, 1), 'U')),


    initComponent: function () {
        var me = this,
            moneyTypes = [];

//        if(me.periodSelection === true) {
//            var paymentTotal = me.getTotal('payment'),
//                billTotal = me.getTotal('bill');
//
//            me.filterParams = [{
//                field: 'date',
//                comparison: 'gte',
//                data: {
//                    type: 'datetime',
//                    value: me.periodStart
//                }
//            },{
//                field: 'date',
//                comparison: 'lt',
//                data: {
//                    type: 'datetime',
//                    value: me.periodEnd
//                }
//            }];
//        }

        me.plugins = [
            {
                ptype: 'rowexpander',
                rowBodyTpl: new Ext.XTemplate(
                    '<p><b>Number:</b> {id}</p>',
                    '<p><b>Description:</b> {description}</p>'
                )
            }
        ];

        me.columns = [
            {
                header: l('payment') + ' №',
                dataIndex: 'id',
                width: 70,
                summaryRenderer: function () {
                    return '<b>' + l('summary') + '</b>';
                }
            },
            {
                header: l('payment.date'),
                xtype: 'datecolumn',
                dataIndex: 'date',
                format: 'd/m/y',
                width: 110,
//                summaryRenderer: function () {
//                    if(me.periodSelection === true) return '<b>(' + Ext.Date.format(Pms.fromUTC(me.periodStart), 'd/m/y') + '-' + Ext.Date.format(Pms.fromUTC(me.periodEnd), 'd/m/y') + ')</b>';
//                }
            },
            {
                header: l('payer'),
                dataIndex: 'bill',
                flex: 2,
                renderer: function (val) {
                    var customerGroup = val.group ? val.group : val.roomUse.customerGroup,
                        str = customerGroup.company ? l('company') + ': ' + customerGroup.company.name : l('guest') + ': ' + customerGroup.customer.lastName + ' ' + customerGroup.customer.firstName;
                    return '<span style="text-decoration:underline;cursor:pointer;">' + str + '</span>';
                }
            },
            {
                header: l('billNum'),
                dataIndex: 'bill',
                width: 70,
                renderer: function (val) {
                    return val.id
                }
            },
            {
                header: l('paymentType'),
                dataIndex: 'bankDetails',
                width: 100,
                renderer: function (val) {
                    var account = val.account ? '<br />' + val.account : '';
                    return l('paymentType.' + val.paymentType) + account;
                }
            },
            {
                header: l('bankDetails.account'),
                dataIndex: 'bankDetails',
                width: 100,
                renderer: function (val) {
                    return val.account;
                }
            },
            {
                header: l('bill.sum'),
                dataIndex: 'billTotal',
                xtype: 'moneycolumn',
                width: 80,
//                summaryRenderer: function (value, summaryData, dataIndex) {
//                    if(me.periodSelection === true) return '<b>' + Ext.util.Format.number(billTotal.total / 100, '0.00') + '</b>';
//                    else return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
//                },
            },
            {
                header: l('paid'),
                dataIndex: 'billTotalPaid',
                xtype: 'moneycolumn',
                width: 80,
//                summaryRenderer: function (value, summaryData, dataIndex) {
//                    if(me.periodSelection === true) return '<b>' + Ext.util.Format.number(billTotal.totalPaid / 100, '0.00') + '</b>';
//                    else return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
//                },
            },
            {
                header: l('payment.sum'),
                dataIndex: 'total',
                xtype: 'moneycolumn',
                width: 95,
//                summaryRenderer: function (value, summaryData, dataIndex) {
//                    if(me.periodSelection === true) return '<b>' + Ext.util.Format.number(paymentTotal / 100, '0.00') + '</b>';
//                    else return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
//                },
            },
            {
                header: l('approved'),
                dataIndex: 'approved',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                width: 50
            },
            {
                header: l('actions'),
                xtype: 'actioncolumn',
                sortable: false,
                width: 60,
                items: [
                    {
                        iconCls: 'app-icon-print',
                        tooltip: l('bill.printCheck'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('printbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return !rec.data.approved ? true : false;
                        }
                    }
                ]
            }
        ];

        me.callParent();
    },

//    getTotal: function(path) {
//        var me = this,
//            total = 0;
//
//        Pms.Ajax.request({
//            url: 'rest/' + path + '/total?startDate=' + me.periodStart + '&endDate=' + me.periodEnd,
//            method: 'GET',
//            async: false,
//            success: function (response) {
//                total = response.content;
//            }
//        });
//        return total;
//    },

    buildToolbar: function () {
        return [
            {
                xtype: 'toolbar',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left'
                },
                items: [
//                    {
//                        text: 'Распечатать чек',
//                        action: 'print',
//                        iconCls: 'app-icon-print'
//                    },{
//                        text: l('delete.btn'),
//                        action: 'delete',
//                        iconCls: 'pms-delete-icon-16',
//                        disabled: true
//                    }
                    {
                        xtype: 'button',
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function (b, e) {
                            b.up('window').down('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ];
    }
});