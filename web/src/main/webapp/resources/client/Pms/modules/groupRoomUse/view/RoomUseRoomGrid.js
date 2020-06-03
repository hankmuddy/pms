Ext.define('Pms.modules.groupRoomUse.view.RoomUseRoomGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.roomUseRoomGrid',
    store: 'Pms.modules.groupRoomUse.store.bookingRoomUse',

    initComponent: function() {
        var me = this;

        me.columns = [
//            {
//                xtype: 'rownumberer',
//                header: '№',
//                width: 35,
//                sortable: false,
//                shrinkWrap: 3,
//                renderer: function (value, meta, record) {
//                    return record.index + 1;
//                }
//            },
            {
                header: l('room'),
                dataIndex: 'room',
                renderer: function(val) {
                    return val.number
                },
                flex: 1
            },
            {
                header: l('floor'),
                dataIndex: 'room',
                renderer: function(val) {
                    return val.floor
                },
                flex: 1
            },
            {
                header: l('roomType'),
                dataIndex: 'room',
                renderer: function(val) {
                    return val.roomType.name
                },
                flex: 1,
            },
            {
                header: l('accommodation'),
                dataIndex: 'room',
                renderer: function(val) {
                    return val.accommodation.name
                },
                flex: 1,
            },
            {
                header: l('startDate'),
                dataIndex: 'startDate',
                xtype: 'datecolumn',
                format: 'd M Y',
                flex: 1,
            },
            {
                header: l('endDate'),
                dataIndex: 'endDate',
                xtype: 'datecolumn',
                format: 'd M Y',
                flex: 1,
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                header: l('actions'),
                width: 50,
                items: [
                    {
                        iconCls: 'app-icon-income',
                        tooltip: l('roomUse.RoomIncome'),
                        handler: function(grid, rowIndex, colIndex) {
                            var store = grid.getStore(),
                                rec = store.getAt(rowIndex),
                                recData = rec.data,
                                groupMembersStore = Ext.create('Pms.modules.person.store.GroupMember',
                                    {
                                        loadParams: {params: {filter: [
                                            {field: 'customerGroup.id', comparison: 'eq', data: {type: 'numeric', value: recData.customerGroup.id}, },
                                            {field: 'groupMemberToRoomUses', comparison: 'is_empty_list'}
                                        ]}},
                                    });
                            groupMembersStore.addListener(
                                {load: function(store, records, success) {
                                    if(records.length == 0) {
                                        Pms.App.showNotification({
                                            message: l('customerGroup.AllPeopleHaveAnalytics')
                                        });
                                    }
                                    else {
                                        var win = Ext.widget('personRoomIncomeWindow', {
                                            groupId: recData.customerGroup.id,
                                            roomUseId: recData.id,
                                            groupMembersStore: groupMembersStore
                                        });
                                        win.show();
                                    }
                                }
                                });
                            groupMembersStore.load();

                        }
                    },
                    {
                        iconCls: 'app-icon-info',
                        tooltip: l('roomUse.roomUseInfo'),
                        handler: function (grid, rowIndex, colIndex) {
                            var win = grid.up('window'),
                                buttonChangeView = win.down('button[cls=group-room-use-change-btn]'),
                                store = grid.getStore(),
                                rec = store.getAt(rowIndex),
                                form = win.down('groupRoomUseEditForm'),
                                recData = rec.data;
                            win.down('catalogOrderGrid').data = recData;
                            win.down('serviceUseGroupGrid').data = recData;
                            win.data = recData;
                            form.data = recData;
                            form.removeAll();
                            form.initComponent(form);
                            form.dockedItems.items[0].hide();
                            form.down('pmsdatefield[name=endDate]').setValue(rec.data.endDate);
                            form.down('textareafield[name=description]').setValue(rec.data.description);
                            buttonChangeView.handler(buttonChangeView);
                        }
                    }
                ]
            }
        ];
        me.callParent();
    }
});