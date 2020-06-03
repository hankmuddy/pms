Ext.define('Pms.modules.restriction.view.RestrictionForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.restrictionForm',
    requires: ['Pms.abstract.field.lookup.Picker'],
    layout: 'vbox',

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
                    anchor: '100%',
                },
                items: [
                    {
                        fieldLabel: l('restriction.name') + Pms.requiredStatus,
                        name: "name",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('restriction.minStay'),
                        name: "minStay",
                        value: 0,
                        regex: /^[0-9]+$/
                    },
                    {
                        fieldLabel: l('restriction.minStayArrival'),
                        name: "minStayArrival",
                        value: 0,
                        regex: /^[0-9]+$/
                    },
                    {
                        fieldLabel: l('restriction.maxStay'),
                        name: "maxStay",
                        value: 0,
                        regex: /^[0-9]+$/
                    },
                    {
                        fieldLabel: l('restriction.closed'),
                        name: "closed",
                        lookupType: 'closed',
                        xtype: 'lookupCombobox',
                        valueNotFoundText: null
                    }
                ]
            }
        ];
    }
});