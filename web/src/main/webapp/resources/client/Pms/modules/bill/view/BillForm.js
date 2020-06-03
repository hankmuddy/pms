Ext.define('Pms.modules.bill.view.BillForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.billForm',

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
                    fieldLabel: l('guestPayer'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.person.store.Person').load(),
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
                    fieldLabel: l('companyPayer'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.company.store.Company').load(),
                    name: 'company',
                    displayField: 'name',
                    valueField: 'id'
                },
                {
                    fieldLabel: l('description'),
                    xtype: 'textareafield',
                    name: 'description'
                }
            ]
        };
    }
});