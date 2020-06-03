Ext.define('Pms.modules.bankDetails.view.BankDetailsSelectWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.bankDetailsSelectWindow',
    autoShow: true,
    width: 280,
    modal: true,
    tittle: l('bankDetails.haveToSelect'),
    income: false,
    data: {},
    url: '',
    method: null,
    success: function () {
    },
    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'form',
                region: 'center',
                items: [
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
                                        me.down('combobox').setValue(defaultBankDetails.id)
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
                    var bankDetails = me.down('combobox[name=bankDetails]').getValue();
                    me.data.bankDetails = {id: bankDetails};
                    Pms.Ajax.request({
                        url: this.url,
                        jsonData: me.data,
                        method: me.method,
                        success: function () {
                            me.close();
                            me.success();
                        }
                    })
                }
            }
        ];
        this.callParent();
    }
});