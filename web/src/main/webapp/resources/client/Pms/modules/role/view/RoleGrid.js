Ext.define("Pms.modules.role.view.RoleGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.roleGrid',
    store: "Pms.modules.role.store.Role",

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('role.name'),
                dataIndex: 'name',
                flex: 2
            },
            {
                header: l('role.permissions'),
                dataIndex: 'role',
                renderer: function (val) {
                    var str = '';
                    Ext.each(val, function (permission) {
                        str += l(permission);
                    });
                    return str
                },
                flex: 1
            }
        ];
        me.callParent(arguments);
    },
    buildToolbar: function () {
        return [
            {
                xtype: 'toolbar',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left'
                },
                items: [
                    {
                        text: l('add.btn'),
                        action: 'new',
                        iconCls: 'pms-add-icon-16'
                    },
                    {
                        text: l('edit.btn'),
                        action: 'edit',
                        iconCls: 'app-icon-edit2',
                        gridAction: true
                    }
                ]
            }
        ];
    }
});