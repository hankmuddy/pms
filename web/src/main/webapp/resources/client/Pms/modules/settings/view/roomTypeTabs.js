Ext.define('Pms.modules.settings.view.roomTypeTabs', {
    extend: 'Pms.abstract.Panel',
    alias: 'widget.roomTypeTabs',

    autoScroll: true,
//    overflowY: false,
//    overflowX: false,

    data: {},

    initComponent: function () {
        var me = this;

        me.items = me.buildItems();

        me.callParent();
    },

    getRoomTypes: function () {
        var records = [];
        Pms.Ajax.request({
            url: 'rest/roomType',
            method: 'GET',
            async: false,
            success: function (response) {
                records = response.content;
            },
            failure: function () {
                Pms.App.showNotification({
                    message: l('review.noRoomTypeData')
                });
            }
        });
        return records;
    },

    buildItems: function () {
        var me = this,
            roomTypes = me.getRoomTypes(),
            tabpanel = {
                xtype: 'tabpanel',
                defaults: {
                    layout: 'fit',
                    overflowY: true,
                    overflowX: false
                },
                items: []
            };

        for (var i in roomTypes) {
            tabpanel.items.push({
                title: roomTypes[i].name,
                items: [
                    {
                        xtype: 'imgDataView',
                        store: Ext.create('Pms.modules.settings.store.Gallery', {roomTypeId: roomTypes[i].id}).load(),
                        data: me.data
                    }
                ]
            });
        }

        return [tabpanel];
    }
});
