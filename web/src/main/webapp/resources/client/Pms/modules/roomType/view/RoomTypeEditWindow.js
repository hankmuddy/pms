Ext.define('Pms.modules.roomType.view.RoomTypeEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.roomTypeEditWindow',
    title: l('edit.btn'),
    width: 1000,
    height: 600,
    data: {},

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=save-roomType]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'tabpanel',
                region: 'center',
                defaults: {
                    layout: 'fit'
                },
                items: [
                    {
                        title: l('description'),
                        items: [
                            {
                                xtype: 'roomTypeForm',
                                approved: me.data.approved,
                                defaults: {
                                    xtype: 'displayfield',
                                    anchor: '100%',
                                    labelWidth: 300
                                }
                            }
                        ]
                    },
                    {
                        title: l('virtualRooms'),
                        items: [
                            {
                                xtype: 'panel',
                                layout: "border",
                                items: [
                                    {
                                        xtype: 'virtualRoomGrid',
                                        region: "center",
                                        height: '100%',
                                        layout: 'fit',
                                        withToolbar: true,
                                        loadParams: {
                                            params: {
                                                filter: [
                                                    {
                                                        field: 'roomType.id',
                                                        comparison: 'eq',
                                                        data: {
                                                            type: 'numeric',
                                                            value: me.data.id
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
                    {
                        title: l('periodInfo'),
                        hidden: Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'periodInfoGrid',
                                withToolbar: true,
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'roomType.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.id
                                                }
                                            }
                                        ]
                                    }
                                },
                                buildToolbar: function () {
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
                                                    iconCls: 'app-icon-edit2',
                                                    gridAction: true
                                                },
                                                {
                                                    text: l('delete.btn'),
                                                    action: 'delete',
                                                    iconCls: 'pms-delete-icon-16',
                                                    gridAction: true
                                                }
                                            ]
                                        }
                                    ]
                                },
                            }
                        ]
                    },
                    {
                        title: l('facilities'),
                        items: [
                            {
                                xtype: 'facilities',
                                data: me.data
                            }
                        ]
                    },
                    {
                        title: l('roomType.roomPhotos'),
                        tabId: 'gallery',
                        layout: 'border',
                        items: [
//                            {
//                                xtype: 'galleryUploadForm',
//                                region: 'north',
//                                height: 42,
//                                style: {
//                                    'border': '0px',
//                                    'border-bottom': '1px solid #ddd'
//                                }
//                            },
                            {
                                xtype: 'gallery',
                                region: 'center',
                                border: false,
                                data: me.data,
                                roomTypeId: me.data.id,
                                photoSelect: true
                            }
                        ]
                    }
                ]
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'update',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});