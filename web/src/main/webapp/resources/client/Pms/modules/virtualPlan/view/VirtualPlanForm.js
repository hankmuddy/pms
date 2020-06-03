Ext.define('Pms.modules.virtualPlan.view.VirtualPlanForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.virtualPlanForm',
    requires: [
        'Pms.abstract.field.Money',
        'Pms.abstract.field.lookup.Picker'
    ],

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
                        fieldLabel: l("virtualPlan.plan") + Pms.requiredStatus,
                        xtype: 'combobox',
                        store: Ext.create('Pms.modules.plan.store.Plan').load(),
                        name: "plan",
                        valueField: 'id',
                        displayField: 'name',
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('virtualPlan.name') + Pms.requiredStatus,
                        name: "name",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('virtualPlan.variation') + Pms.requiredStatus,
                        name: "variation",
                        xtype: 'lookupCombobox',
                        lookupType: 'variation',
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('virtualPlan.value'),
                        name: "value",
                        xtype: 'moneyfield'
//                        allowBlank: false
                    }
                ]
            }
        ];
    }
});