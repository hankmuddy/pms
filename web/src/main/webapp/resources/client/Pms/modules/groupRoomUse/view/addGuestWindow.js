Ext.define('Pms.modules.groupRoomUse.view.addGuestWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.addGuestWindow',
    title: l('reservation'),
    autoShow: false,
    width: 700,
    height: 415,
    autoScroll: false,
    data: {},
    toGroup: false,
    layout: null,

    initComponent: function () {
        var me = this;
        Pms.Ajax.request({
            url: 'rest/roomUse/' + me.data.id,
            async: false,
            method: 'GET',
            scope: this,
            success: me.successLoadComponent
        });
        me.callParent(arguments);
    },
    successLoadComponent: function (data) {
        var me = this,
            customerId = me.data.customerGroup.customer ? me.data.customerGroup.customer.id : null,
            tabs = [],
            groupMembers;
        me.data = data.content;
        groupMembers = me.data.customerGroup.groupMembers;
        if (me.data.customerGroup.customer && me.data.customerGroup.includeCustomer) {
            tabs.push({
                title: l('masterPerson'),
                items: [
                    {
                        xtype: 'personBookForm',
                        oldPerson: true,
                        personId: me.data.customerGroup.customer.id,
                        listeners: {
                            beforerender: function (form, eOpts) {
                                form.getForm().setValues(me.data.customerGroup.customer);
                            }
                        }
                    }
                ]
            });
        }
        if (groupMembers) {
            for (var k = 0; k < groupMembers.length; k++) {
                if (groupMembers[k].person.type == 'adult') {
                    if (groupMembers[k].person.id != customerId) {

                        tabs.push({
                            title: groupMembers[k].person.firstName,
                            items: [
                                {
                                    xtype: 'personBookForm',
                                    oldPerson: true,
                                    personId: groupMembers[k].person.id,
                                    num: k,
                                    listeners: {
                                        beforerender: function (form, eOpts) {
                                            form.getForm().setValues(groupMembers[form.num].person);
                                        }
                                    }
                                }
                            ]
                        });
                    }
                }
                else if (groupMembers[k].person.type == 'child') {
                    tabs.push({
                        title: groupMembers[k].person.firstName,
                        items: [
                            {
                                xtype: 'childBookForm',
                                personId: groupMembers[k].person.id,
                                num: k,
                                listeners: {
                                    beforerender: function (form, eOpts) {
                                        form.getForm().setValues(groupMembers[form.num].person);
                                    }
                                }
                            }
                        ]
                    });
                }
            }
        }
        tabs.push({
            title: l('guest'),
            items: [
                {
                    xtype: 'personBookForm',
                }
            ]
        });

        tabs.push({
            title: l('guest'),
            iconCls: 'app-icon-add',
            id: 'addGuest'
        }, {
            title: l('child'),
            iconCls: 'app-icon-add',
            id: 'addChild'
        });
        me.items = [
            {
                xtype: 'bookDataGrid',
                data: me.data
            },
            {
                xtype: 'groupRoomUseAddTabs',
                setItems: tabs
            }
        ]
        ;

        me.buttons = [
            {
                text: l('save.btn'),
                iconCls: 'icon-save',
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
    }
})
;