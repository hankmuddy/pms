Ext.define('Pms.modules.role.view.RoleForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.roleForm',
    requires: ['Pms.abstract.field.lookup.Picker'],
    layout: 'vbox',

    data: {},

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
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
                        fieldLabel: l('role.name') + Pms.requiredStatus,
                        name: "name",
                        allowBlank: false
                    },
                ]
            },
            {
                xtype: 'fieldset',
                padding: 10,
                defaults: {
                    xtype: 'checkbox',
                    anchor: '100%'
                },
                items: [

                ]
            }
        ];
    }
});