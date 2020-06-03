Ext.define('Pms.modules.refund.view.RefundForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.refundForm',

    persons: {},

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this;
        return  {
            xtype: 'fieldset',
            title: l('accountDetails'),
            items: [
                {
                    xtype: 'hidden',
                    name: 'id'
                },
                {
                    xtype: 'hidden',
                    name: 'customerGroup'
                },
                {
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.person.store.Person').load(),
                    fieldLabel: l('guestPayer'),
                    name: 'customer',
                    displayField: 'lastName',
                    valueField: 'id',
                    querymode: 'remote',
                    queryParam: '',
                    listConfig: {
                        getInnerTpl: function () {
                            return  '{lastName} {firstName} ';
                        }
                    }
                },
                {
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.company.store.Company').load(),
                    fieldLabel: l('companyPayer'),
                    name: 'company',
                    displayField: 'name',
                    valueField: 'id'
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