Ext.define("Pms.modules.user.view.UserGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.userGrid',
    store: "Pms.modules.user.store.User",

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('user.username'),
                dataIndex: 'username',
                flex: 2
            },
            {
                header: l('user.firstName'),
                dataIndex: 'firstName',
                renderer: function (val, raw) {
                    var rec = raw.record;
                    return val + ' ' + rec.data.lastName;
                },
                flex: 2
            },
            {
                header: l('user.role'),
                dataIndex: 'role',
                renderer: function (val, rec) {
                    return l(val.name);
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