Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseIncomeWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseIncomeWindow',

    title: l('arrival'),
    autoShow: false,
    width: 340,
    modal: false,
    data: {},

    initComponent: function () {
        var me = this;
        this.height = this.data.customerGroup.roomUsesQuantity == 1 ? 215 : 250;
        this.items = [
            {
                xtype: 'container',
                items: [
                    {
                        xtype: 'propertygrid',
                        hideHeaders: true,
                        editable: false,
                        source: {
                            room: this.data.room.number,
                            roomType: this.data.room.roomType.name,
                            master: !Ext.isEmpty(this.data.customerGroup.company) ? this.data.customerGroup.company.name : this.data.customerGroup.customer.lastName + ' ' + this.data.customerGroup.customer.firstName,
                            startDate: Ext.Date.format(this.data.startDate, 'd/m/y'),
                            endDate: Ext.Date.format(this.data.endDate, 'd/m/y')
                        },
                        sourceConfig: {
                            room: {displayName: l('room')},
                            roomType: {displayName: l('roomType')},
                            master: {displayName: l('mainOfGroup')},
                            startDate: {displayName: l('from')},
                            endDate: {displayName: l('to')}
                        },
                        listeners: {
                            beforeedit: function () {
                                return false;
                            }
                        }
                    },
                    {
                        xtype: 'groupRoomUseIncomeForm',
                        isGroup: this.data.customerGroup.roomUsesQuantity == 1
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('addGuests'),
                handler: function (grid, rowIndex, colIndex) {
                    var win = Ext.widget('addGuestWindow', {data: me.data});
                    win.show();
                },
                iconCls: 'fa fa-group'
            },
            {
                text: l('income.btn'),
//                action: 'save',
                requestDisable: true,
                handler: function () {
                    var recData = me.data,
                        groupMembersStore = Ext.create('Pms.modules.person.store.GroupMember',
                            {
                                loadParams: {params: {filter: [
                                    {field: 'customerGroup.id', comparison: 'eq', data: {type: 'numeric', value: recData.customerGroup.id}, },
                                    {field: 'groupMemberToRoomUses', comparison: 'is_empty_list'}
                                ]}},
                            }),
                        btn = this;
//                    console.log(this);
                    groupMembersStore.addListener(
                        {load: function (store, records, success) {
                            if (records.length == 0) {
//                                Pms.App.showNotification({
//                                    message: l('roomUse.noGuestInGr')
//                                });
                                me.fireEvent('incomeWithGuest',btn);
                            }
                            else {
                                var win = Ext.widget('personRoomIncomeWindow', {
                                    groupId: recData.customerGroup.id,
                                    roomUseId: recData.id,
                                    groupMembersStore: groupMembersStore,
                                    toRoomUseIncome: true,
                                    btn: btn
                                });
                                win.show();
//                                Pms.App.showNotification({
//                                    message: l('roomUse.checkIn.addGuests')
//                                });
                            }
                        }
                        });
                    groupMembersStore.load();
                },
                iconCls: 'fa fa-suitcase'
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close,
                iconCls: 'fa fa-times'
            }
        ];

        this.callParent(arguments);
    }
});