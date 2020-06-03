Ext.define('Pms.modules.groupRoomUse.view.groupBookingWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupBookingWindow',

    title: l('reservation'),
    autoShow: false,
    resizable: false,
    width: 700,
    height: 550,

    initComponent: function () {
        var me = this;

        me.tbar = me.topToolbar();

        me.items = [
            {
                xtype: 'container',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupRoomUseAddTabs'
                    },
                    {
                        xtype: 'groupRoomUseForm'
                    }
                ]
            }
        ];

        me.buttons = [
            {
                text: l('book'),
                iconCls: 'fa fa-reply',
                action: 'save-booking',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                iconCls: 'fa fa-times',
                scope: me,
                handler: me.close
            }
        ]

        me.callParent(arguments);
    },

    topToolbar: function () {
        return  [l('roomUse.master'), {
            labelWidth: 80,
            xtype: 'combobox',
            hideLabel: true,
            store: Ext.create('Ext.data.ArrayStore', {
                fields: ['label', 'value'],
                data: [
                    [l('mainGuest'), 'masterPerson'],
                    [l('mainCompany'), 'masterCompany']
                ]
            }),
            displayField: 'label',
            valueField: 'value',
            triggerAction: 'all',
            value: 'masterPerson',
            queryMode: 'local',
            selectOnFocus: true,
            indent: true,
            padding: '2px 5px',
            listeners: {
                beforerender: function (combo, e) {
                    var win = combo.up('window'),
                        tabPanel = win.down('tabpanel');

                    tabPanel.insert(0, {
                        title: l('masterPerson'),
                        id: 'masterPerson',
                        // hidden: true,
                        // hideMode: 'display',
                        items: [
                            {
                                xtype: 'personBookForm'
                            }
                        ]
                    }).show();
                },
                change: function (combo, newVal, prevVal, e) {
                    var win = combo.up('window'),
                        tabPanel = win.down('tabpanel'),
                        masterTabs = {
                            masterPerson: {
                                title: l('masterPerson'),
                                id: 'masterPerson',
                                // hidden: true,
                                // hideMode: 'display',
                                items: [
                                    {
                                        xtype: 'personBookForm'
                                    }
                                ]
                            },
                            masterCompany: {
                                title: l('mainCompany'),
                                id: 'masterCompany',
                                // hidden: true,
                                // hideMode: 'display',
                                items: [
                                    {
                                        xtype: 'companyBookForm'
                                    }
                                ]
                            }
                        };

                    tabPanel.insert(0, masterTabs[newVal]).show();
                    tabPanel.down('#' + prevVal).close();
                }
            }
        }]
    }
});