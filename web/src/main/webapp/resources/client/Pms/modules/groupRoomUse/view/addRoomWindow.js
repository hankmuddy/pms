Ext.define('Pms.modules.groupRoomUse.view.addRoomWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.addRoomWindow',
    title: l('groupBooking'),
    autoScroll: false,
    width: '100%',
    height: '100%',
    data: {},
    modelData: null,

    listeners: {
        afterrender: function (win, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!this.down('combobox[isExpanded=true]'))
                        this.down('button[action=save]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;
        me.customerGroup = me.modelData[0].data.customerGroup;
        me.items = [
            {
                xtype: 'container',
                layout: 'border',
                items: [
                    {
                        xtype: 'panel',
                        title: l('freeRoomSearch'),
                        region: 'east',
                        split: false,
                        collapsible: true,
                        collapsed: false,
                        overflowY: 'scroll',
                        width: 500,
                        items: [
                            {
                                xtype: 'roomUseFilterForm',
                                book: true,
                                listeners: {
                                    afterrender: function (form) {
                                        var values = {
                                                startDate: me.modelData[0].data.startDate,
                                                endDate: me.modelData[0].data.endDate
                                            },
                                            button = form.down('button[action=apply-filter]');
                                        form.getForm().setValues(values);
                                        setTimeout(function () {
                                            button.handler(button)
                                        }, 1);
                                    },
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'container',
                        region: 'center',
                        layout: 'border',
                        items: [
//                            {
//                                xtype: 'groupRoomUseAddTabs',
//                                region: 'north'
//                            },
                            {
                                xtype: 'groupRoomUseRoomGrid',  //Pms.modules.room.view.GroupRoomUseRoomGrid
                                region: 'center',
                                modelData: me.modelData
                            }
                        ]
                    }
                ]
            }
        ];

        me.buttons = [
            {
                text: l('book'),
                iconCls: 'fa fa-reply',
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel'),
                iconCls: 'fa fa-times',
                scope: me,
                handler: me.close
            }
        ];
        me.callParent(arguments);
    },

});