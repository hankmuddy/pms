Ext.define("Pms.modules.bill.view.BillGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.billGrid',
    store: 'Pms.modules.bill.store.Bill',
    requires: ['Pms.abstract.field.MoneyColumn'],
    enableColumnMove: false,

    initComponent: function () {
        var me = this;

        me.features = [
            {
                ftype: 'summary'
            }
        ];

        me.columns = [
            {
                header: '№',
                dataIndex: 'id',
                width: 50
            },
            {
                header: l('payer'),
                dataIndex: 'roomUse',
                flex: 1,
                renderer: function (roomUse, row, rec) {
                    var group = roomUse ? roomUse.customerGroup : rec.data.group,
                        customer = group.customer,
                        company = group.company;
                    if (company) return '<span style="text-decoration:underline;cursor:pointer;">' + company.name + ' ' + l('company') + '</span>';
                    else if (customer) return '<span style="text-decoration:underline;cursor:pointer;">' + customer.lastName + ' ' + customer.firstName + '</span>';
                    else return '&mdash;';
                }
            },
            {
                header: l('date'),
                dataIndex: 'createdDate',
                xtype: 'datecolumn',
                format: 'd/m/y',
                summaryRenderer: function () {
                    return '<b>' + l('summary') + '</b>';
                },
                width: 80
            },
            {
                header: l('amount'),
                dataIndex: 'total',
                xtype: 'moneycolumn',
                summaryType: 'sum',
                width: 80,
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                },
            },
            {
                header: l('paid'),
                dataIndex: 'totalPaid',
                xtype: 'moneycolumn',
                summaryType: 'sum',
                sortable: false,
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + Ext.util.Format.number(value / 100, '0.00') + '</b>';
                },
                width: 80,
            },
            {
                dataIndex: 'remain',
                header: l('remain'),
                xtype: 'moneycolumn',
                width: 80,
                sortable: false,
                summaryType: 'sum',
                sortable: false,
                renderer: function (val, row , rec) {
                    var rest = rec.data.total - rec.data.totalPaid;
                    return Ext.util.Format.number(rest ? rest / 100 : 0, '0.00');
                },
                summaryRenderer: function (value, summaryData, dataIndex) {
                    var total = dataIndex.data.total,
                        totalPaid = dataIndex.data.totalPaid;
                    return '<b>' + Ext.util.Format.number((total - totalPaid) / 100, '0.00') + '</b>';
                }
            },
            {
                header: l('confirmed'),
                dataIndex: 'approved',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                width: 60
            },
            {
                header: l('paid'),
                dataIndex: 'paid',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                sortable: false,
                falseText: '',
                width: 60,
                sortable: false,
                renderer: function (val, md, rec) {
                    return (rec.data.total - rec.data.totalPaid) ? '' : Pms.iconOk
                },
            },
            {
                header: l('actions'),
                xtype: 'actioncolumn',
                sortable: false,
                width: 100,
                items: [
                    {
                        iconCls: 'app-icon-commit',
                        tooltip: l('commitBill'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('commitbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return rec.data.approved ? true : false;
                        }
                    },
                    {
                        iconCls: 'app-icon-print',
                        tooltip: l('printBill'),
                        handler: function(grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('viewhotelbillbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function(view, rowIndex, colIndex, item, rec) {
                            return !rec.data.approved ? true : false;
                        }
                    },{
                        iconCls: 'app-icon-print1',
                        tooltip: l('printInvoice'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('invoicebuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return !rec.data.approved ? true : false;
                        }
                    },{
                        iconCls: 'app-icon-priceing',
                        tooltip: l('makePayment'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('paymentbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            if (rec.data.total - rec.data.totalPaid == 0)
                                return true;
                            return !rec.data.approved ? true : false;
                        }
                    },
                    {
                        iconCls: 'app-icon-refund',
                        tooltip: l('makeReturn'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('refundbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return !rec.data.approved ? true : false;
                        }
                    },
                    {
                        iconCls: 'app-icon-remove',
                        tooltip: l('delete.btn'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('deletebuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return rec.data.approved ? true : false;
                        }
                    }
                ]
            }
        ];

        if(_('hotel').country == 'UA') {
            me.columns[8].items.splice(2, 1);
        }

        me.callParent();
    },

    buildToolbar: function () {
        return [
            {
                xtype: 'toolbar',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left'
                },
                items: [
                    {
                        text: l('commitBill'),
                        action: 'commit',
                        iconCls: 'pms-success-icon-16'
                    },
                    {
                        text: l('makePayment'),
                        iconCls: 'app-icon-priceing',
                        action: 'payment'
                    },
                    {
                        text: l('makeReturn'),
                        action: 'refund',
                        iconCls: 'app-icon-refund',
                        disabled: true
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete',
                        iconCls: 'pms-delete-icon-16',
                        disabled: true
                    },
                    {
                        xtype: 'button',
                        flex: 1,
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function(b, e) {
                            b.up('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ];
    }
});