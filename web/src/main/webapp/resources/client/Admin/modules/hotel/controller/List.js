Ext.define('Admin.modules.hotel.controller.List', {
    extend: 'Admin.generic.app.controller.List',
    viewClass: 'Admin.modules.hotel.view.List',
    storeClass: 'Admin.modules.hotel.Store',
    setHandlersOnViewEvents: function () {
        this.callParent(arguments);
        this.view.on('block', this.onBlock, this);
        this.view.on('paidUntil', this.onPaidUntil, this);
        this.view.on('pms', this.onPms, this);
    },
    onBlock: function (record) {
        var block = !record.get('blocked'),
            id = record.getId(),
            me = this;
        Ext.Ajax.request({
            url: 'admin/hotel/' + id + '/blocked/' + block,
            method: 'PUT',
            success: function () {
                var msg = block ? l('hotel.success.blocked') : l('hotel.success.unBlocked')
                me.view.getStore().reload();
                Ext.Msg.alert(l('success'), msg);
            }
        })
    },
    onPaidUntil: function (record) {
        Admin.getApplication().navigateTo(this.links['edit'] + '/' + record.getId() + '/paidUntil')
    },
    onPms: function (record) {
        var hotelId = record.get('hotelId');
        websocket.close();

        Ext.Ajax.request({
            url: 'admin/authentication/hotel/' + hotelId,
            method: 'PUT',
            success: function () {
//                setTimeout(function () {
//                    window.location.reload();
//                }, 10000);
//                window.onbeforeunload = function() {
//                    websocket.onclose = function () {}; // disable onclose handler first
//                };
//                return;
                window.location.pathname = 'app/home';
            }
        })
    }
});