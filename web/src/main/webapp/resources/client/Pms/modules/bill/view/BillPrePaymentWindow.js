Ext.define('Pms.modules.bill.view.BillPrePaymentWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billPrePaymentWindow',
    width: 300,
    bill: null,
    settings: null,
    bankDetails: null,
    groupMember: null,
    income: false,
    groupMemberStore: null,
//    disableSelection: true,

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'form',
                region: 'center',
                items: [
                    {
                        xtype: 'combobox',
                        fieldLabel: l('payer'),
                        store: Ext.create('Pms.modules.person.store.GroupMember', {
                            filterParams: [
                                {
                                    field: 'customerGroup.id',
                                    comparison: 'eq',
                                    data: {
                                        type: 'numeric',
                                        value: this.bill.roomUse ? this.bill.roomUse.customerGroup.id : this.bill.group.id
                                    }
                                },
                                {
                                    field: 'person.type',
                                    comparison: 'eq',
                                    data: {
                                        type: 'string',
                                        value: 'adult'
                                    }
                                }
                            ]
                        }),
                        name: 'payer',
                        displayField: 'lastName',
                        valueField: 'person.id',
                        anchor: '100%',
                        emptyText: l('lastName'),
                        querymode: 'remote',
                        listConfig: {
                            loadingText: l('searchText'),
                            emptyText: l('error.noMatches'),
                            getInnerTpl: function () {
                                return '<div class="search-item"><span><b>{lastName} {firstName}</b></span></div>';
                            }
                        }
                    },
                    {
                        xtype: 'numberfield',
                        name: 'prePayment',
                        fieldLabel: l('bill.prePayment') + ', %',
                        step: 1,
                        minValue: 0,
                        maxValue: 100,
                        anchor: '100%'
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
                    }
                ]
            }
        ];

        me.buttons = [
            {
                text: l('ok.btn'),
                scope: this,
                handler: function () {
                    var me = this,
                        data = {bill: me.bill},
                        prePayment = me.down('numberfield').getValue() ? 0 : me.down('numberfield').getValue(),
                        bankDetailsCombo = me.down('combobox[name=bankDetails]'),
                        payerCombo = me.down('combobox[name=payer]');

                    data.bankDetails = bankDetailsCombo.value ? bankDetailsCombo.getStore().getById(bankDetailsCombo.getValue()).data : me.bankDetails;
                    data.prePayment = prePayment;
                    if (payerCombo.getValue()) data.payerId = payerCombo.getValue();

                    if (me.down('form').isValid()) {
                        this.close();
//                        Ext.widget('billViewWindow', {
//                            bill: me.bill,
//                            settings: me.settings,
//                            bankDetails: bankDetails,
//                            groupMember: me.groupMember,
//                            prePayment: prePayment
//                        });
                        Ext.widget('billPdfWindow', {data: data});
                        Pms.createPreloader();
                    }
                    else {
                        Ext.Msg.alert(l('error'), Pms.iconError + l('requestParsing.incorrectValue'));
                    }
                }
            }
        ];
        this.callParent();
    }
});