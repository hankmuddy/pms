Ext.define('Pms.modules.roomType.view.facilities', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.facilities',

    overflowY: true,
    overflowX: false,

    data: {},

    initComponent: function() {
        var me = this;
        me.items = [];
        for(var groupName in Pms.facilities) {
            var item = {
                xtype: 'fieldset',
                title: l('facility.' + groupName),
                margin: 4,
                width: 330,
                style: {float: 'left'},
                defaults: {
                    xtype: 'checkboxfield',
                    inputValue: true,
                    fieldStyle: 'float:right;',
                    labelWidth: 300
                },
                items: []
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
                        var fieldset = checkbox.up('fieldset'),
                            items = fieldset.items.items;
                        for (var i in items) {
                            items[i].setValue(value);
                        }
                    }
                }
            });

            for(var i in Pms.facilities[groupName]) {
                var subItem = {
                    fieldLabel: l(Pms.facilities[groupName][i].name),
                    name: Pms.facilities[groupName][i].id
                };
                if(Ext.Array.findBy(me.data.facilities, function(el) {
                    return el == Pms.facilities[groupName][i].id;
                })) {
                    subItem.checked = true;
                }
                item.items.push(subItem);
            }
            me.items.push(item);
        }

        me.callParent(arguments);
    }
});