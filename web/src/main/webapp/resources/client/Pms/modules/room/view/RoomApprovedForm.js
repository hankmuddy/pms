Ext.define('Pms.modules.room.view.RoomApprovedForm', {
    extend: 'Pms.abstract.ApprovedForm',
    alias: 'widget.roomApprovedForm',

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },
    layout: 'fit',
    border: false,
    autoscroll: true,

    buildItems: function () {
        var me = this;
        return  [
            {
                xtype: "fieldset",
                padding: 10,
                layout: 'vbox',
                defaults: {
                    anchor: '100%',
                    xtype: 'displayfield'
                },
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: l('room'),
                        name: "number",
                        allowBlank: false
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: l('floor'),
                        name: 'floor',
                        regex: /^[0-9]+$/,
                    },
                    {
                        fieldLabel: l('accommodation'),
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
                        fieldLabel: l('roomType'),
                        name: "roomType",
                        submitValue: false,
                        width: 300,
                        listeners: {
                            resize: function (field,width,height) {
                                var win = this.up('window');
                                win.setHeight(190 + height);
                            }
                        }
                    }
                ]
            }
        ];
    }
});