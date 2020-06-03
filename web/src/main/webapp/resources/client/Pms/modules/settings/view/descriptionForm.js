Ext.define('Pms.modules.settings.view.descriptionForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.descriptionForm',

    data: {},

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        return {
            xtype: 'fieldset',
            layout: 'fit',
            title: l('hotel.description'),
            items: [
                {
                    xtype: 'hidden',
                    name: 'id'
                },
                {
                    xtype: 'textareafield',
                    fieldLabel: l('hotel.description'),
                    hideLabel: true,
                    name: 'description',
                    margin: '3px 0 10px 0'
                }
            ]
        };
    },

    bbar: ['->', {
        iconCls: 'save-action-icon',
        text: l('save.btn'),
        action: 'save',
        requestDisable: true
    }, {
        iconCls: 'app-icon-remove',
        text: l('cancel.btn'),
        handler: function (btn) {
            btn.up('window').close();
        }
    }]
});