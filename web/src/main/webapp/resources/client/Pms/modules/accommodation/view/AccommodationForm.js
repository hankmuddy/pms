Ext.define('Pms.modules.accommodation.view.AccommodationForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.accommodationForm',

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        return    {
            xtype: 'fieldset',
            padding: 5,
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
                    fieldLabel: l('name') + Pms.requiredStatus,
                    name: 'name',
                    allowBlank: false
                },
                {
                    fieldLabel: l('shortname'),
                    name: 'shortName',
                    maxLength: 2,
                    enforceMaxLength: 2
                }
            ]
        };
    }
});