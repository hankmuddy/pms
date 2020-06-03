Ext.define("Pms.modules.bill.view.BillGroupGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.billGroupGrid',
    store: 'Pms.modules.bill.store.Bill',

    features: [
        {
            ftype: 'summary'
        }
    ],
    plugins: [
        {
            ptype: 'rowexpander',
            rowBodyTpl: new Ext.XTemplate(
                // '<div style="float:left;margin-left:57px;"><b>Услуги: </b></div>',
                '<div style="margin-left:57px;">{property_service_uses:this.formatExpand}</div>', {
                    formatExpand: function (val) {
                        services = '<table border="0" cellspacing="0" cellpadding="0">';
                        services += '<tr>';
                        services += '<th style="border-bottom:1px solid #000;padding-right:30px;" align="left">Услуга</th>';
                        services += '<th style="border-bottom:1px solid #000;padding-right:30px;" align="left">Цена</th>';
                        services += '<th style="border-bottom:1px solid #000;padding-right:30px;" align="left">К-во</th>';
                        services += '<th style="border-bottom:1px solid #000;" align="left">Сумма</th>';
                        services += '</tr>';

                        for (var i in val) {
                            services += '<tr>';
                            services += '<td style="padding-right:30px;">' + val[i].property_catalog_item[0].property_name + '</td>';
                            services += '<td style="padding-right:30px;">' + val[i].property_price + '</td>';
                            services += '<td style="padding-right:30px;">' + val[i].property_qty + '</td>';
                            services += '<td>' + val[i].property_total + '</td>';
                            services += '</tr>';
                        }
                        services += '</table>';
                        return services;
                    }
                }
            )
        }
    ],

    initComponent: function () {
        var me = this,
            ioTypes = [];

//        Ext.create('Pms.modules.bill.store.ioType').load({
//            callback: function (records, operation, success) {
//                for (var i in records) ioTypes[records[i].data.name] = records[i].data.label;
//            }
//        });

        me.columns = [
            {
                header: '№',
                dataIndex: 'id',
                width: 50
            },
            {
                header: l('type'),
                dataIndex: 'property_io_type',
                width: 80,
                renderer: function (val) {
                    return ioTypes[val];
                }
            },
            {
                header: l('payer'),
                dataIndex: 'property_company',
                flex: 1,
                renderer: function (val, row) {
                    var group = row.record.data.property_group[0];
                    return group.property_def_payer == 'master_company' ? group.property_master_company[0].property_name : group.property_master_person[0].property_surname + ' ' + group.property_master_person[0].property_name;
                },
                summaryRenderer: function () {
                    return '<b>ИТОГО</b>';
                }
            },
            {
                header: l('date'),
                dataIndex: 'property_date',
                xtype: 'datecolumn',
                format: 'd/m/y',
                flex: 1
            },
            {
                header: l('amount'),
                dataIndex: 'property_total',
                xtype: 'numbercolumn',
                flex: 1,
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + value + '</b>';
                }
            },
            {
                header: l('paid'),
                dataIndex: 'paid',
                xtype: 'numbercolumn',
                flex: 1,
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + value + '</b>';
                }
            },
            {
                header: 'Остаток',
                dataIndex: 'rest',
                xtype: 'numbercolumn',
                flex: 1,
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + value + '</b>';
                }
            },
            {
                header: 'Проведен',
                dataIndex: 'property_commit',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                flex: 1
            },
            {
                header: 'Сформирован',
                dataIndex: 'property_formed_fully',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                flex: 1
            },
            {
                header: 'Оплачен',
                dataIndex: 'property_paid_fully',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                flex: 1
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                flex: 1,
                items: [
//                    {
//                        iconCls: 'app-icon-edit2',
//                        tooltip: l('change'),
//                        handler: function (grid, rowIndex, colIndex) {
//                            this.up('grid').fireEvent('editbuttonclick', grid, rowIndex, colIndex);
//                        },
//                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
//                            return true;
//                        }
//                    },
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
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('printbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
                            return !rec.data.approved ? true : false;
                        }
                    },
                    {
                        iconCls: 'app-icon-priceing',
                        tooltip: l('makePayment'),
                        handler: function (grid, rowIndex, colIndex) {
                            this.up('grid').fireEvent('paymentbuttonclick', grid, rowIndex, colIndex);
                        },
                        isDisabled: function (view, rowIndex, colIndex, item, rec) {
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

        me.callParent();
    }
});