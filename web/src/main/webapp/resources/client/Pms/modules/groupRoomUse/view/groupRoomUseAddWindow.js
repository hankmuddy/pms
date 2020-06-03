Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseAddWindow',
    title: l('reservation'),
    autoShow: false,
    resizable: false,
    width: 700,
    height: 540,
    layout: null,
    data: {},
    listeners: {
        beforerender: function (win, e) {
//            if (!Ext.isEmpty(win.data.masterCompany)) {
//                win.down('tabpanel').insert(0, {
//                    title: l('mainCompany'),
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
        },
        afterrender: function (win, options) {
//            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
//                enter: function () {
//                    if (!this.down('combobox[isExpanded=true]'))
//                        this.down('button[action=save]').fireHandler()
//                },
//                scope: this
//            });
        }
    },

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
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                iconCls: 'fa fa-times',
                scope: me,
                handler: me.close
            }
        ];
        me.callParent(arguments);
    },

    topToolbar: function () {
        return  [
            l('roomUse.master'), {
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
                        var isGroupMember = combo.up('window').down('checkboxfield[name=isGroupMember]'),
                            customerPays = combo.up('window').down('checkboxfield[name=customerPays]');
                        if (newVal == 'masterPerson') {
                            //                        chbox.show();
                            customerPays.enable();
                            isGroupMember.enable();
                        }
                        else {
                            //                        chbox.hide();
                            customerPays.disable();
                            isGroupMember.disable();
                        }
                        if (tabPanel.down('#companyCustomer'))
                            tabPanel.down('#companyCustomer').close();
                    }
                }
            },
            ' ',
            {
                labelWidth: 80,
                fieldLabel: l('purposeOfVisit') + Pms.requiredStatus,
                name: 'pov',
                allowBlank: false,
                lookupType: 'pov',
                xtype: 'lookupCombobox',
                valueNotFoundText: null,
                value: 'TOURISM'
            },
            {
                xtype: 'hiddenfield',
                name: 'group_booking',
                value: false
            }
        ];
    }
});