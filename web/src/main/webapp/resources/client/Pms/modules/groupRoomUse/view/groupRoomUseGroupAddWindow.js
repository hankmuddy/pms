Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseGroupAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseGroupAddWindow',

    title: l('groupBooking'),
    autoScroll: false,
    width: 1200,
    height: 600,
    data: {},

    listeners: {
//        beforerender: function (win, e) {
//            if (!Ext.isEmpty(win.data.masterCompany)) {
//                win.down('tabpanel').insert(0, {
//                    title: 'Главная компания',
//                    id: 'masterCompany',
//                    items: [
//                        {
//                            xtype: 'companyBookForm'
//                        }
//                    ]
//                }).show();
//                win.down('tabpanel').down('#masterCompany').down('companyBookForm').getForm().setValues(win.data.masterCompany);
//            }
//            else {
//                win.down('tabpanel').insert(0, {
//                    title: l('masterPerson'),
//                    id: 'masterPerson',
//                    items: [
//                        {
//                            xtype: 'personBookForm'
//                        }
//                    ]
//                }).show();
//                if (!Ext.isEmpty(win.data.masterPerson)) {
//                    win.down('tabpanel').down('#masterPerson').down('personBookForm').getForm().setValues(win.data.masterPerson);
//                }
//            }
//        },
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

        me.tbar = me.topToolbar();

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
                        autoScroll: true,
//                        overflowY: 'scroll',
                        width: 360,
                        items: [
                            {
                                xtype: 'roomUseFilterForm',
                                book: true,
                                width: 350,
                            }
                        ]
                    },
                    {
                        xtype: 'container',
                        region: 'center',
                        layout: 'border',
                        items: [
                            {
                                xtype: 'groupRoomUseAddTabs',
                                region: 'north'
                            },
                            {
                                xtype: 'groupRoomUseRoomGrid',  //Pms.modules.room.view.GroupRoomUseRoomGrid
                                region: 'center'
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

    topToolbar: function () {
        return  [l('roomUse.master'), {
            labelWidth: 80,
            xtype: 'combobox',
            name: 'master',
            hideLabel: true,
            store: Ext.create('Ext.data.ArrayStore', {
                fields: ['label', 'value'],
                data: [
                    [l('masterPerson'), 'masterPerson'],
                    [l('masterCompany'), 'masterCompany']
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
                                title: l('masterCompany'),
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
                    var chbox = combo.up('toolbar').down('checkboxfield');
                    if (newVal == 'masterPerson') {
                        chbox.show();
                        chbox.disabled = false;
                    }
                    else {
                        chbox.hide();
                        chbox.disabled = true;
                    }
                    if (tabPanel.down('#companyCustomer'))
                        tabPanel.down('#companyCustomer').close();
                }
            }
        }, ' ', {
            xtype: 'checkboxfield',
            fieldLabel: l('addToGroup'),
            name: 'isGroupMember',
            labelWidth: 100,
            inputValue: true,
            checked: true,
            value: true
        }, ' ', {
            xtype: 'checkboxfield',
            name: 'customerPays',
            marginLeft: 25,
//            width: 150,
            fieldLabel: l('customerPays'),
//                                    hidden: true,
//                                    labelWidth: 100,
            inputValue: true
        }, '|',
            {
                labelWidth: 80,
                fieldLabel: l('purposeOfVisit') + Pms.requiredStatus,
                name: 'pov',
                allowBlank: false,
                lookupType: 'pov',
                xtype: 'lookupCombobox',
                valueNotFoundText: null,
                value: 'TOURISM'
            },'|',
            {
                xtype: 'lookupCombobox',
                labelWidth: 60,
                fieldLabel: l('source'),
                name: 'source',
                width: 200,
                lookupType: 'source',
                listeners: {
                    change: function (combo, val) {
                        var win = combo.up('window');
                        if (win) {
                            var rcode = win.down('textfield[name=rcode]');
                            if (val == 'FRONT_DESK') {
                                rcode.hide();
                                rcode.allowBlank = true;
                            }
                            else {
                                rcode.show();
                                rcode.allowBlank = false
                            }
                        }
                    },
                    afterrender: function (combo) {
                        combo.setValue('FRONT_DESK');
                    }
                }
            },
            {
                xtype: 'textfield',
                labelWidth: 50,
                width: 150,
                margin: '0 0 0 10',
                fieldLabel: l('rcode') + Pms.requiredStatus,
                hidden: true,
                name: 'rcode'
            },
            {
                xtype: 'hiddenfield',
                name: 'group_booking',
                value: true
            }
        ]
    }
});