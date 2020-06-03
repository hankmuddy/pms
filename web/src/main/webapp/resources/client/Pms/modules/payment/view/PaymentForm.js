Ext.define('Pms.modules.payment.view.PaymentForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.paymentForm',
    requires: ['Pms.abstract.field.Money'],

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.callParent(arguments);
    },

    buildItems: function () {
        var me = this;

        return  {
            xtype: 'fieldset',
            title: l('payment.data'),
            items: [
                {
                    xtype: 'hidden',
                    name: 'id'
                },
                {
                    xtype: 'hidden',
                    name: 'bill'
                },
                {
                    xtype: 'pmsdatefield',
                    fieldLabel: l('date') + Pms.requiredStatus,
                    name: 'date',
                    format: 'd/m/y',
                    submitFormat: 'U',
                    value: new Date(),
                    allowBlank: false
                },
                {
                    name: 'time',
                    xtype: 'timefield',
                    fieldLabel: l('time') + Pms.requiredStatus,
                    format: 'H : i',
                    submitFormat: 'H : i',
                    increment: 5,
                    value: Pms.fromUTC(new Date(), true),
                    allowBlank: false
                },
                {
                    xtype: 'combobox',
                    fieldLabel: l('bankDetails') + Pms.requiredStatus,
                    store: Ext.create('Pms.modules.bankDetails.store.BankDetails').load({
                        params: {filter: [
                            {field: 'blocked', comparison: 'eq', data: {type: 'boolean', value: false}}
                    ]}}),
                    name: 'bankDetails',
                    displayField: 'name',
                    queryMode: 'local',
                    valueField: 'id',
                    allowBlank: false,
                    listConfig: {
                        getInnerTpl: function () {
                            return '<div class="search-item">' +
                                '<b>{name}</b>' +
                                '<tpl if="bankName">({bankName})</tpl>' +
                                '</div>';
                        }
                    },
                    listeners: {
                        afterrender: function () {
                            Pms.Ajax.request({
                                method: 'GET',
                                url: 'rest/bankDetails/default',
                                success: function (response) {
                                    var defaultBankDetails = response.content;
                                    me.down('combobox[name=bankDetails]').setValue(defaultBankDetails.id)
                                }
                            })
                        }
                    }
                },
                {
                    xtype: 'moneyfield',
                    fieldLabel: l('amount') + Pms.requiredStatus,
                    name: 'total',
//                    hideTrigger: true,
//                    allowDecimals: true,
//                    decimalPrecision: 2,
//                    decimalSeparator: ',',
//                    minValue: 0,
//                    step: 0.01,
                    allowBlank: false,
//                    mouseWheelEnabled: false
                },
                {
                    xtype: 'textareafield',
                    fieldLabel: l('description'),
                    name: 'description'
                }
            ]
        };
    }
});