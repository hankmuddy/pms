Ext.define('Pms.modules.serviceUse.view.serviceUsePrintGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.serviceUsePrintGrid',
    store: 'Pms.modules.serviceUse.store.serviceUseBill',

    // features: [{
    //     ftype: 'summary'
    // }],

    paging: false,
//    bill: null,

    initComponent: function () {
        var me = this,
            serviceTypes = [],
            measures = [];
//
//        Ext.create('Pms.modules.catalog.store.ServiceType').load({
//            callback: function (records, operation, success) {
//                for (var i in records) serviceTypes[records[i].data.name] = records[i].data.label;
//            }
//        });
//        Ext.create('Pms.modules.catalog.store.Measure').load({
//            callback: function (records, operation, success) {
//                for (var i in records) measures[records[i].data.name] = records[i].data.label;
//            }
//        });

        me.columns = [
            {
                xtype: 'rownumberer',
                header: '№',
                width: 35,
                sortable: false,
                shrinkWrap: 3
//                renderer: function (value, meta, record) {
//                    return record.index + 1;
//                }
            },
            {
                header: l('service'),
                dataIndex: 'type',
                width: '40%'
//                renderer: function (val) {
//                    return val[0].property_service_type == 'room_rate' ? serviceTypes[val[0].property_service_type] + ' (' + val[0].property_name + ')' : val[0].property_name;
//                }
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
                dataIndex: 'price',
                flex: 1
            },
            {
                header: l('quantity'),
                dataIndex: 'quantity',
                flex: 1
                // renderer: function (val, row) {
                //     var rec = row.record.data,
                //         measure = measures[rec.property_catalog_item[0].property_measure];

                //     return val + ' ' + measure;
                // },
                // summaryRenderer: function () {
                //     return '<b>ИТОГО</b>';
                // }
            },
            {
                header: l('total'),
                dataIndex: 'total',
                flex: 1,
                summaryType: 'sum',
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return '<b>' + value + '</b>';
                }
            }
        ];

        me.callParent();
    }
});