Ext.define('Pms.modules.bankDetails.view.BankDetailsGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.bankDetailsGrid',
    store: 'Pms.modules.bankDetails.store.BankDetails',
    requires: ['Pms.abstract.field.lookup.Column'],

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('name'),
                dataIndex: 'name',
                flex: 1
            },
            {
                header: l('accountNumber'),
                dataIndex: 'account',
                flex: 1,
                renderer: function (val) {
                    return val ? val : '_'
                }
            },
            {
                header: l('paymentType'),
                dataIndex: 'paymentType',
                flex: 1,
                xtype: 'lookupcolumn',
                lookupType: 'paymentType'
            },
            {
                header: _('hotel').country == 'RU' ? l('kpp') : l('mfo'),
                dataIndex: 'mfo',
                flex: 1,
                renderer: function (val) {
                    return val ? val : '_'
                }
            },
            {
                header: _('hotel').country == 'RU' ? l('inn') : l('edrpou'),
                dataIndex: 'edrpou',
                flex: 1,
                renderer: function (val) {
                    return val ? val : '_'
                }
            },
            {
                header: l('bankDetails.blocked'),
                dataIndex: 'blocked',
                xtype: 'booleancolumn',
                trueText: Pms.iconCross,
                falseText: Pms.iconOk,
                width: 60
            },
            {
                header: l('bankDetails.default'),
                dataIndex: 'defaultDetails',
                xtype: 'booleancolumn',
                trueText: Pms.iconOk,
                falseText: '',
                width: 60
            }
        ];
        me.callParent();
    }
});