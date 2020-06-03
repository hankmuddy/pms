Ext.define('Pms.modules.periodInfo.view.PeriodInfoForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.periodInfoForm',
    requires: ['Pms.abstract.field.Money'],

    data: {},

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return  [
            {
                xtype: 'fieldset',
                padding: 10,
                defaults: {
                    xtype: 'textfield',
                    anchor: '100%'
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        fieldLabel: l("periodInfo.roomType") + Pms.requiredStatus,
                        xtype: 'combobox',
                        store: Ext.create('Pms.modules.roomType.store.RoomType').load(),
                        name: "roomType",
                        valueField: 'id',
                        displayField: 'name',
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('periodInfo.dateStart') + Pms.requiredStatus,
                        name: "dateStart",
                        allowBlank: false,
                        xtype: 'pmsdatefield',
                        submitFormat: 'U'
                    },
                    {
                        fieldLabel: l('periodInfo.livingPrice'),
                        name: "livingPrice",
                        xtype: 'moneyfield',
                        regex: /^[0-9]+$/
                    },
                ]
            }
        ];
    }
});