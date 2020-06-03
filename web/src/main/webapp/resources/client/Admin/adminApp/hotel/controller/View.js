Ext.define('Admin.adminApp.hotel.controller.View', {
    extend: 'Admin.modules.hotel.controller.View',
    viewClass: 'Admin.adminApp.hotel.view.View',
    setHandlersOnViewEvents: function () {
        this.callParent(arguments);
        this.view.on('delete', this.onTenantDelete, this);
    },
    onTenantDelete: function (record) {
        var hotelId = record.data.hotelId;
        Ext.MessageBox.confirm(l('hotel.delete'), l('confirmation'), function (btn) {
            if (btn === 'yes') {
                Ext.Ajax.request({
                    url: 'admin/hotel/dropTenant',
                    params: {
                        hotelId: hotelId
                    },
                    method: 'POST',
                    success: function () {
                        Admin.getApplication().navigateTo('hotel/list');
                    }
                })
            }
        });
    },
    onSuccessLoad: function (record, operation) {
        if(!record.data.blocked){
            this.view.dockedItems.items[0].hide()
        }
        this.callParent(arguments);
    }
});
