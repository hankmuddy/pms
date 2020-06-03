Ext.define('Pms.modules.room.view.RoomForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.roomForm',

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return  [
            {
                xtype: 'form',
                layout: 'fit',
                border: false,
                autoscroll: true,
                fileupload: true,

                items: [
                    {
                        xtype: "fieldset",
                        padding: 10,
                        defaults: {
                            xtype: "textfield",
                            anchor: '100%'
                        },
                        items: [
                            {
                                xtype: "hidden",
                                name: "id"
                            },
                            {
                                fieldLabel: l('room') + Pms.requiredStatus,
                                name: "number",
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('floor'),
                                regex: /^[0-9]+$/,
                                name: 'floor'
                            },
                            {
                                fieldLabel: l('accommodation') + Pms.requiredStatus,
                                name: "accommodation",
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.accommodation.store.Accommodation').load({
                                    params: {
                                        filter: [
                                            {
                                                field: 'approved',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'boolean',
                                                    value: true
                                                }
                                            }
                                        ]
                                    }
                                }),
                                displayField: "name",
                                valueField: "id",
                                queryMode: "remote",
                                editable: false,
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('roomType') + Pms.requiredStatus,
                                name: "roomType",
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.roomType.store.RoomType').load({
                                    params: {
                                        filter: [
                                            {
                                                field: 'approved',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'boolean',
                                                    value: true
                                                }
                                            }
                                        ]
                                    }
                                }),
                                displayField: "name",
                                editable: false,
                                valueField: "id",
                                queryMode: "local",
                                allowBlank: false
                            }
                        ]
                    }
                ]
            }
        ];
    }
});