Ext.define('Pms.modules.settings.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.settingsViewport',

    autoScroll: false,
    overflowY: false,
    overflowX: false,
    width: 1800,
    height: 1000,

    data: {},

    buildItems: function () {
        me = this;

        return [
            {
                xtype: 'tabpanel',
                region: 'center',
                defaults: {
                    layout: 'fit',
                    overflowY: true,
                    overflowX: false,
                    hint: l('hint.forTab')
                },
                items: [
                    {
                        title: l('settings.review'),
                        items: [
                            {
                                xtype: 'review',
                                data: me.data
                            }
                        ]
                    },
                    {
                        title: l('settings.CommonInfo'),
                        items: [
                            {
                                xtype: 'commonForm',
                                data: me.data
                            }
                        ]
                    },
                    {
                        title: l('settings.accommodations'),
                        hidden: Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'accommodationGrid',
                                withToolbar: true
                            }
                        ]
                    },
                    {
                        title: l('bankDetails'),
                        hidden: Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'bankDetailsGrid',
                                withToolbar: true,
                                buildToolbar: me.buildBankDetailsToolbar
                            }
                        ]
                    },
                    {
                        title: l('facilities'),
                        items: [
                            {
                                xtype: 'allFacilities',
                                data: me.data
                            }
                        ]
                    },
                    {
                        title: l('settings.hotelPhoto'),
                        layout: null,
                        autoScroll: true,
                        items: [
                            {
                                xtype: 'gallery',
                                galleryId: 'hotelPhoto',
                                type: 'hotel',
                                border: false,
                                photoUpload: true,
                                data: me.data
                            }
                        ]
                    },
                    {
                        title: l('settings.channelManager'),
                        hidden: !_('channelsEnabled'),
                        items: [
                            {
                                xtype: 'channelManagerForm'
                            }
                        ]
                    }
//                    {
//                        title: l('settings.bookingButton'),
//                        items: [
//                            {
//                                xtype: 'bookingButtonForm'
//                            }
//                        ]
//                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {
    },

    buildBankDetailsToolbar: function () {
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
                        text: l('add.btn'),
                        action: 'new',
                        iconCls: 'pms-add-icon-16'
                    },
                    {
                        text: l('edit.btn'),
                        action: 'edit',
                        iconCls: 'app-icon-edit2'
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete',
                        disabled: false,
                        iconCls: 'pms-delete-icon-16'
                    },
                    {
                        text: l('block.btn'),
                        action: 'block'
//                        iconCls: 'pms-delete-icon-16'
                    },
                    {
                        text: l('bankDetails.setDefault'),
                        action: 'default',
                        iconCls: 'app-icon-key'
                    }
                ]
            }
        ];
    }
});
