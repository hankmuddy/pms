Ext.define("Pms.modules.refund.view.RefundGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.refundGrid',
    store: 'Pms.modules.refund.store.Refund',
    requires: ['Pms.abstract.field.MoneyColumn'],

    // features: [{
    //     ftype: 'summary'
    // }],

    initComponent: function () {
        var me = this,
            ioTypes = [];

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
                renderer: function (roomUse, row) {
                    var rec = row.record,
                        group = rec.data.group ? rec.data.group : roomUse.customerGroup,
                        customer = group.customer,
                        company = group.company;
                    if (company) return company.name + ' ' + l('company');
                    else if (customer) return customer.lastName + ' ' + customer.firstName;
                    else return '&mdash;';
                }
            },
            {
                header: l('date'),
                dataIndex: 'createdDate',
                xtype: 'datecolumn',
                format: 'd/m/y',
                width: 80
            },
            {
                header: l('sum'),
                dataIndex: 'total',
                xtype: 'moneycolumn',
                width: 80
            }
        ];
        me.callParent();
    },

    buildToolbar: Ext.emptyFn
});