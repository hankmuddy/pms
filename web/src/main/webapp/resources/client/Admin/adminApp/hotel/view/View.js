Ext.define('Admin.adminApp.hotel.view.View', {
    extend: 'Admin.modules.hotel.view.View',
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'button',
                    text: l('hotel.deleteTenant'),
                    iconCls: 'pms-delete-icon-16',
                    handler: function () {
                        var panel = this.up('panel'),
                            record = panel.getRecord();
                        panel.fireEvent('delete', record)
                    },
                }
            ]
        }
    ]
});
