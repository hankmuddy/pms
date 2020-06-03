Ext.define('Pms.modules.bankDetails.view.BankDetailsForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.bankDetailsForm',

    layout: 'hbox',

    data: {},

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return [
            {
                xtype: 'fieldset',
                name: 'primary',
                padding: 10,
                margin: 5,
                width: '45%',
                defaults: {
                    xtype: 'textfield',
                    anchor: '100%',
                    labelWidth: 120
                },
                items: [
                    { //0
                        xtype: 'hidden',
                        name: 'id'
                    },
                    { //1
                        fieldLabel: l('paymentType') + Pms.requiredStatus,
                        name: 'paymentType',
                        allowBlank: false,
                        lookupType: 'paymentType',
                        xtype: 'lookupCombobox',
                        valueNotFoundText: null,
                        listeners: {
                            change: function (combo, value) {
                                var form = combo.up('form'),
                                    fieldSet = combo.up('fieldset'),
                                    additionalFieldSet = form.down('fieldset[name=additional]'),
                                    holderField = form.down('textfield[name=holder]');

                                if (value == 'INTERNATIONAL_ACCOUNT') {
                                    for (var i = 3; i <= 9; i++) {
                                        fieldSet.items.items[i].show();
                                    }
                                    fieldSet.items.items[7].hide();
                                    fieldSet.items.items[8].hide();
                                    fieldSet.items.items[7].reset();
                                    fieldSet.items.items[8].reset();
                                    if (additionalFieldSet.isHidden()) {
                                        additionalFieldSet.show();
                                    }
                                }
                                else {
                                    if (!additionalFieldSet.isHidden()) {
                                        Ext.each(additionalFieldSet.query('field'), function (field) {
                                            field.reset();
                                        });
                                        additionalFieldSet.hide();
                                    }
                                    if (value == 'CASH') {
                                        holderField.allowBlank = true;
                                        for (var i = 3; i <= 9; i++) {
                                            fieldSet.items.items[i].hide();
                                            fieldSet.items.items[i].reset();
                                        }
                                    }
                                    else {
                                        holderField.allowBlank = false;
                                        for (var i = 3; i <= 9; i++) {
                                            fieldSet.items.items[i].show();
                                        }
                                    }
                                }
                            }
                        }
                    },
                    { //2
                        fieldLabel: l('name') + Pms.requiredStatus,
                        name: 'name',
                        allowBlank: false
                    },
                    { //3
                        fieldLabel: l('bankName'),
                        name: 'bankName'
                    },
                    { //4
                        fieldLabel: l('bankDetails.bankAddress'),
                        name: 'bankAddress'
                    },
                    { //5
                        fieldLabel: l('bankDetails.holder') + Pms.requiredStatus,
                        name: 'holder',
                        allowBlank: false
                    },
                    { //6
                        fieldLabel: l('accountNumber'),
                        name: 'account'
                    },
                    { //7
                        fieldLabel: _('hotel').country == 'RU' ? l('kpp') : l('mfo'),
                        name: 'mfo',
                        xtype: 'numberfield',
                        hideTrigger: true,
                        keyNavEnabled: false,
                        mouseWheelEnabled: false
                    },
                    { //8
                        fieldLabel: _('hotel').country == 'RU' ? l('inn') : l('edrpou'),
                        name: 'edrpou',
                        xtype: 'numberfield',
                        hideTrigger: true,
                        keyNavEnabled: false,
                        mouseWheelEnabled: false
                    },
                    { //9
                        fieldLabel: l('bankDetails.swift'),
                        name: 'swift'
                    },
                    { //10
                        fieldLabel: l('description'),
                        xtype: 'textareafield',
                        name: 'description'
                    }
                ]
            },
            {
                xtype: 'fieldset',
                padding: 10,
                margin: 5,
                name: 'additional',
                width: '55%',
                defaults: {
                    xtype: 'textfield',
                    anchor: '100%',
                    labelWidth: 200
                },
                items: [
                    {
                        fieldLabel: l('bankDetails.corrBankName'),
                        name: 'corrBankName'
                    },
                    {
                        fieldLabel: l('bankDetails.corrBankAddress'),
                        name: 'corrBankAddress'
                    },
                    {
                        fieldLabel: l('bankDetails.corrAccount'),
                        xtype: 'textfield',
                        name: 'corrAccount'
                    },
                    {
                        fieldLabel: l('bankDetails.corrSwift'),
                        xtype: 'textfield',
                        name: 'corrSwift'
                    },
                    {
                        fieldLabel: l('bankDetails.IBAN'),
                        xtype: 'textfield',
                        name: 'iban'
                    },
                    {
                        fieldLabel: l('bankDetails.additional'),
                        xtype: 'textareafield',
                        name: 'additional'
                    }
                ],
                listeners: {
                    beforerender: function (fieldset, eOpts) {
                        var form = fieldset.up('form'),
                            primaryFieldset = form.down('fieldset[name=primary]'),
                            paymentTypeCombo = form.down('lookupCombobox[name=paymentType]'),
                            paymentType = paymentTypeCombo.getValue();

                        if (paymentType == 'INTERNATIONAL_ACCOUNT') fieldset.show();
                        else fieldset.hide();
                    }
                }
            }
        ];
    }
});