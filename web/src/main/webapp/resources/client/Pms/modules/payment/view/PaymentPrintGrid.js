Ext.define("Pms.modules.payment.view.PaymentPrintGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.paymentPrintGrid',

    // store: 'Pms.modules.payment.store.Payment',
    // requires: ['Ext.ux.RowExpander'],
    // features: [{
    //       ftype: 'summary',
    //    },{
    //       ftype: 'grouping',
    //       groupHeaderTpl: '{name}',//'№{name}',//'<tpl if="name == null">Без счетов</tpl><tpl if="name != null">№{name}</tpl>',
    //       hideGroupedHeader: true,
    //       enableGroupingMenu: false
    // }],

    border: false,
    editable: true,
    paging: false,
    store: 'Pms.modules.payment.store.Payment',

//    bill: [],
    payment: null,
//    moneyTypeStore: Ext.create('Pms.modules.payment.store.moneyType'),

    initComponent: function () {
        var me = this
//        moneyTypes = [];

//        this.moneyTypeStore.each(function (rec) {
//            moneyTypes[rec.data.name] = rec.data.label;
//        });

        me.columns = [
            /*Ext.create('Ext.grid.RowNumberer'),*/
            {
                header: l('bill.printCheck'),
                dataIndex: 'bill',
                width: 80,
                renderer: function (val) {
                    return val.id;
                }
            },
            {
                header: l('date'),
                xtype: 'datecolumn',
                dataIndex: 'date',
                format: 'd/m/y',
                flex: 1
                //  summaryRenderer: function(){
                //     return '<b>ИТОГО</b>';
                // }
            },
            {
                header: l('amount'),
                dataIndex: 'total',
                xtype: 'numbercolumn',
                flex: 1
                // summaryType: 'sum'
            },
            {
                header: l('paymentType'),
                dataIndex: 'method',
                flex: 1,
//            renderer: function (val) {
//                return moneyTypes[val];
//            }
            }
        ];

        me.callParent();
        me.getStore().filter([
            {filterFn: function (item) {
                return item.getId() == me.payment.getId();
            }}
        ]);
    }
});