Ext.define('Pms.modules.booking.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.bookingViewport',

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
                xtype: 'container',
                region: 'center',
                layout: 'fit',
                items: [
                    {
                        xtype: 'bookingGrid'
                    }
                ]
            },
            {
                xtype: 'panel',
                title: l('filters'),
                region: 'east',
                split: false,
                width: 400,
                autoScroll: true,
                collapsible: true,
                collapsed: true,
                items: [
                    {
                        xtype: 'bookingFilterForm'
                    }
                ]
            }
//            {
//                xtype: 'tabpanel',
//                region: 'center',
//                items: [
//                    {
//                        title: l('roomUseSearch'),
//                        layout: 'border',
//                        items: [
//                            {
//                                xtype: 'container',
//                                region: 'center',
//                                layout: 'fit',
//                                items: [
//                                    {
//                                        xtype: 'bookingGrid'
//                                    }
//                                ]
//                            },
//                            {
//                                xtype: 'panel',
//                                title: l('filters'),
//                                region: 'east',
//                                split: false,
//                                width: 400,
//                                autoScroll: true,
//                                collapsible: true,
//                                collapsed: true,
//                                items: [
//                                    {
//                                        xtype: 'bookingFilterForm'
//                                    }
//                                ]
//                            }
//                        ]
//                    },
//                    {
//                        title: l('groupSearch'),
//                        layout: 'border',
//                        items: [
//                            {
//                                xtype: 'container',
//                                region: 'center',
//                                layout: 'fit',
//                                items: [
//                                    {
//                                        xtype: 'groupBookingSearchGrid'
//                                    }
//                                ]
//                            },
//                            {
//                                xtype: 'panel',
//                                title: l('filters'),
//                                region: 'east',
//                                split: false,
//                                width: 400,
//                                autoScroll: true,
//                                collapsible: true,
//                                collapsed: true,
//                                items: [
//                                    {
//                                        xtype: 'groupBookingSearchFilterForm'
//                                    }
//                                ]
//                            }
//                        ]
//                    }
//                ]
//            }
        ]

    },

    buildTopButtons: function () {
        if(Pms.App.isExtranet() !== true) {
            return [
                {
                    xtype: 'toolbar',
                    defaults: {
                        scale: 'small',
                        iconAlign: 'left',
                        width: '60'
                    },
                    items: [
                        {
                            text: l('newGroupReservation'),
                            action: 'new-group-booking',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-group-booking'
                        },
                    {
                        text: l('newArrival'),
                        action: 'new-income',
                        cls: 'btn-bigger-font',
                        iconCls: 'pms-chess-toolbar-icon-new-arrival'
                    },
                    {
                        text: l('newReservation'),
                        action: 'new-booking',
                        cls: 'btn-bigger-font',
                        iconCls: 'pms-chess-toolbar-icon-new-booking',
                    },
                    {
                        xtype: 'button',
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function(b, e) {
                            b.up('window').down('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ];
    }
    }
});
