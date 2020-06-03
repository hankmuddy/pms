Ext.define('Pms.modules.analytics.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.analyticsViewport',

    autoScroll: false,

    initComponent: function () {
        var me = this;

        me.tbar = this.buildTopButtons();

        me.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        return [
            {
                xtype: 'panel',
                region: 'center',
                layout: 'vbox',
                defaults: {
                    width: '100%'
                },
                items: [
                    {
                        xtype: 'analyticsDateForm'
                    },
                    {
                        xtype: 'bookingChart',
                        loadParams: {
                            params: {
                                filter: [
                                    {
                                        field: 'startDate',
                                        comparison: 'lte',
                                        data: {
                                            type: 'date',
                                            value: parseInt(new Date().getTime() / 1000)
                                        }
                                    },
                                    {
                                        field: 'endDate',
                                        comparison: 'gte',
                                        data: {
                                            type: 'date',
                                            value: parseInt(new Date().getTime() / 1000 - 604800)
                                        }
                                    },
                                    {
                                        field: 'status',
                                        comparison: 'neq',
                                        data: {
                                            type: 'ROOM_USE_STATUS',
                                            value: 'REFUSE'
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        xtype: 'analyticsInfoPanel'
                    },
                    {
                        xtype: 'analyticsSourceGrid',
                        loadParams: {
                            params: {
                                filter: [
                                    {
                                        field: 'startDate',
                                        comparison: 'lte',
                                        data: {
                                            type: 'date',
                                            value: parseInt(new Date().getTime() / 1000)
                                        }
                                    },
                                    {
                                        field: 'endDate',
                                        comparison: 'gte',
                                        data: {
                                            type: 'date',
                                            value: parseInt(new Date().getTime() / 1000 - 604800)
                                        }
                                    },
                                    {
                                        field: 'status',
                                        comparison: 'neq',
                                        data: {
                                            type: 'ROOM_USE_STATUS',
                                            value: 'REFUSE'
                                        }
                                    }
                                ]
                            }
                        }
                    }
                ]
            }
        ]

    },

    buildTopButtons: function () {
        return [
//            {
//                xtype: 'toolbar',
//                defaults: {
//                    scale: 'small',
//                    iconAlign: 'left',
//                    width: '60'
//                },
//                items: [
//                    {
//                        text: l('newGroupReservation'),
//                        action: 'new-group-booking',
//                        cls: 'btn-bigger-font',
//                        iconCls: 'pms-chess-toolbar-icon-group-booking'
//                    },
//                    {
//                        text: l('newArrival'),
//                        action: 'new-income',
//                        cls: 'btn-bigger-font',
//                        iconCls: 'pms-chess-toolbar-icon-new-arrival'
//                    },
//                    {
//                        text: l('newReservation'),
//                        action: 'new-booking',
//                        cls: 'btn-bigger-font',
//                        iconCls: 'pms-chess-toolbar-icon-new-booking'
//                    }
//                ]
//            }
        ];
    }
});
