Ext.define('Pms.modules.settings.view.allFacilities', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.allFacilities',

    overflowY: true,
    overflowX: false,
    data: {},

    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'tabpanel',
                tabPosition: 'left',
                style: {
                    float: 'left'
                },
                height: 500,
                width: 470,
                defaults: {
                    bodyPadding: 10
                },
                items: []
            },
            {
                xtype: 'tabpanel',
                tabPosition: 'right',
                style: {
                    float: 'right'
                },
                height: 500,
                width: 470,
                defaults: {
                    bodyPadding: 10
                },
                items: []
            }
        ];
        var i = 0;
        for (var groupName in Pms.allFacilities) {
            var item = {
                title: l('facility.' + groupName),
                defaults: {
                    xtype: 'checkboxfield',
                    inputValue: true,
                    fieldStyle: 'float:right;',
                    labelWidth: 300
                },
                items: [
                ]
            };
            item.items.push({
                xtype: 'checkboxfield',
                fieldLabel: l('selectAll'),
                inputValue: true,
                submitValue: false,
                cls: "select-all-checkbox",
                labelCls: "select-all-checkbox-label",
                labelWidth: 300,
                listeners: {
                    change: function (checkbox, value) {
                        var panel = checkbox.up('panel'),
                            items = panel.items.items;
                        for (var i in items) {
                            items[i].setValue(value);
                        }
                    }
                }
            });
            for (var i in Pms.allFacilities[groupName]) {
                var subItem = {
                    fieldLabel: l(Pms.allFacilities[groupName][i].name),
                    name: Pms.allFacilities[groupName][i].id
                };
                if (Ext.Array.findBy(me.data.facilities, function (el) {
                    return el.facility.id == Pms.allFacilities[groupName][i].id;
                })) {
                    subItem.checked = true;
                }
                item.items.push(subItem);
            }
            i++;
            me.items[i % 2].items.push(item);
        }

        me.items.push({
            xtype: 'hidden',
            name: 'id',
            value: me.data.hotelInfo.id
        });

        me.callParent(arguments);
    },

    bbar: ['->', {
        iconCls: 'save-action-icon',
        text: l('save.btn'),
        action: 'update',
        requestDisable: true
    }, {
        iconCls: 'app-icon-remove',
        text: l('cancel.btn'),
        handler: function (btn) {
            btn.up('window').close();
        }
    }]
});
