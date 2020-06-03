Ext.define('Pms.modules.virtualRoom.view.VirtualRoomForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.virtualRoomForm',
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
                        fieldLabel: l("virtualRoom.roomType") + Pms.requiredStatus,
                        xtype: 'combobox',
                        store: Ext.create('Pms.modules.roomType.store.RoomType').load(),
                        name: "roomType",
                        valueField: 'id',
                        displayField: 'name',
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('virtualRoom.name') + Pms.requiredStatus,
                        name: "name",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('shortname') + Pms.requiredStatus,
                        name: "shortname",
                        maxLength: 4,
                        enforceMaxLength: 4,
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('virtualRoom.adults') + Pms.requiredStatus,
                        name: "adults",
                        allowBlank: false,
                        regex: /^[0-9]+$/
                    },
                    {
                        fieldLabel: l('virtualRoom.children'),
                        name: "children",
                        regex: /^[0-9]+$/
                    },
                    {
                        fieldLabel: l('virtualRoom.additional'),
                        name: "additional",
                        regex: /^[0-9]+$/
                    },
                    {
                        fieldLabel: l('virtualRoom.defaultPrice') + Pms.requiredStatus,
                        name: "defaultPrice",
                        xtype: 'moneyfield',
                        allowBlank: false,
                        hideTrigger: true
                    }
                ]
            }
        ];
    }
});