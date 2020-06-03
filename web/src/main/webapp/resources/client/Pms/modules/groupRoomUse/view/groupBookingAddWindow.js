Ext.define('Pms.modules.groupRoomUse.view.groupBookingAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupBookingAddWindow',
    title: l('reservation'),
    autoShow: false,
    width: 700,
    resizable: false,
    height: 550,
    autoScroll: 'auto',
    overflowY: 'scroll',
    storeData: [],

    initComponent: function () {
        var me = this;

        me.tbar = me.topToolbar();

        me.items = [
            {
                xtype: 'container',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupRoomUseAddTabs',
                        setItems: [
                            {
                                title: l('masterPerson'),
                                id: 'masterPerson',
                                items: [
                                    {
                                        xtype: 'personBookForm'
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        xtype: 'groupBookingForm',
                        storeData: me.storeData
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
                change: function (combo, newVal, prevVal, e) {
                    var win = combo.up('window'),
                        tabPanel = win.down('tabpanel'),
                        masterTabs = {
                            masterPerson: {
                                title: l('masterPerson'),
                                id: 'masterPerson',
                                items: [
                                    {
                                        xtype: 'personBookForm'
                                    }
                                ]
                            },
                            masterCompany: {
                                title: l('mainCompany'),
                                id: 'masterCompany',
                                items: [
                                    {
                                        xtype: 'companyBookForm'
                                    }
                                ]
                            }
                        };

                    tabPanel.insert(0, masterTabs[newVal]).show();
                    tabPanel.down('#' + prevVal).close();

                    // control the checkbox[name=is_group_member]
                    // var chbox = combo.up('toolbar').down('checkboxfield');
                    // if(newVal == 'masterPerson') {
                    //     chbox.show();
                    //     chbox.disabled = false;
                    // }
                    // else {
                    //     chbox.hide();
                    //     chbox.disabled = true;
                    // }
                }
            }
        }, '|', {
            xtype: 'checkboxfield',
            fieldLabel: l('addToGroup'),
            name: 'is_group_member',
            labelWidth: 100,
            inputValue: true,
            checked: true,
            value: true,
            hidden: true
        }, {
            xtype: 'hiddenfield',
            name: 'group_booking',
            value: true
        }]
    }
});