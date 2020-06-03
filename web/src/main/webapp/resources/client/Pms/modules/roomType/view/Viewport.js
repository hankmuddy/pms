Ext.define("Pms.modules.roomType.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.roomTypeViewport',

    autoScroll: false,

    buildItems: function () {
        return [
            {
                xtype: 'tabpanel',
                layout: 'fit',
                region: 'center',
                items: [
                    {
                        title: l('startMenu.roomType'),
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'panel',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'roomTypeGrid',
                                        withToolbar: true
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('virtualRooms'),
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'panel',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'virtualRoomGrid',
                                        withToolbar: true,
                                        storeParams: {groupField: 'roomType.id'},
                                        features: [
                                            {
                                                ftype: 'grouping',
                                                hideGroupedHeader: true,
                                                startCollapsed: false,
                                                groupHeaderTpl: '<tpl if="name==null">' + l('withoutType') + '</tpl>' +
                                                    '<tpl if="name!=null">' +
                                                    '{[values.rows[0].data.roomType.name]}' +
                                                    '</tpl>'
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('startMenu.rooms'),
                        layout: 'fit',
                        hidden: Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'panel',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'roomGrid',
                                        withToolbar: true
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('virtualRoom.ota'),
                        layout: 'fit',
                        hidden: !_('channelsEnabled'),
                        items: [
                            {
                                xtype: 'otaCalendarSch',
                                layout: 'fit'
                            }
                        ]
                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {
    }
});
