Ext.define('Pms.modules.groupRoomUse.view.roomUseFilterForm', {
    extend: "Pms.abstract.Form",
    alias: 'widget.roomUseFilterForm',

    book: false,
    initComponent: function () {
        var me = this;

        me.items = me.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return    [
            {
                xtype: 'container',
                title: l('freeRoomSearch'),
                padding: 10,
                defaults: {labelWidth: 130},
                bbar: this.bottomToolbar(),
                items: [
                    {
                        fieldLabel: l('roomType'),
                        name: 'roomType',
                        xtype: 'combobox',
                        store: Ext.create('Pms.modules.roomType.store.RoomType', {
                            filterParams: [
                                {
                                    field: 'approved',
                                    comparison: 'eq',
                                    data: {
                                        type: 'boolean',
                                        value: true
                                    }
                                }
                            ]
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        queryMode: 'remote',
                        queryParam: '',
                        selectOnFocus: true,
                        indent: true,
                        anchor: '100%',
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-arrow-trigger',
                        onTrigger1Click: function () {
                            this.clearValue();
                        }
                    },
                    {
                        fieldLabel: l('roomUse.freeFrom'),
                        xtype: 'pmsdatefield',
                        name: 'startDate',
                        format: 'd/m/y',
                        submitFormat: 'U',
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-date-trigger',
                        onTrigger1Click: function () {
                            this.setValue('');
                        },
                        listeners: {
                            change: function (field, newVal, oldVal, e) {
                                var endDate = field.nextNode();
                                if (endDate.value < newVal) endDate.setValue('');
                                var date = new Date(newVal.getTime() + 86400000);
                                endDate.setMinValue(date);
                            }
                        }
                    },
                    {
                        fieldLabel: l('roomUse.freeTo'),
                        xtype: 'pmsdatefield',
                        name: 'endDate',
                        format: 'd/m/y',
                        submitFormat: 'U',
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-date-trigger',
                        onTrigger1Click: function () {
                            this.setValue('');
                        },
                        listeners: {
                            change: function (field, e) {
                                var startDate = field.previousNode();
                                var date = new Date(startDate.value.getTime() + 86400000);
                                field.setMinValue(date);
                            }
                        }
                    }
                ]
            },
            {
                xtype: 'toolbar',
                margin: '5 0',
                items: me.bottomToolbar()
            },
            {
                xtype: 'freeRoomGrid',
                book: me.book,
                loadParams: {
                    params: {
                        filter: [
                            {
                                field: 'approved',
                                comparison: 'eq',
                                data: {
                                    type: 'boolean',
                                    value: true
                                }
                            }
                        ]
                    }
                },
                itemId: 'freeRoomGrid',
                viewConfig: {
                    plugins: {
                        ptype: 'gridviewdragdrop',
                        dragGroup: 'freeRoomGrid',
                        dropGroup: 'groupRoomUseRoomGrid'
                    },
                    listeners: {
                        drop: function (node, data, dropRec, dropPosition) {
                            var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
                        }
                    }
                }
            }
        ];
    },

    bottomToolbar: function () {
        return  [
            {
                xtype: 'button',
                iconCls: 'app-icon-ok',
                text: l('apply'),
                action: 'apply-filter',
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        form = win.down('roomUseFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('freeRoomGrid'),
                        store = grid.getStore(),
                        params = {},
                        filter = [],
                        connective = null;

                    //console.log(data);

                    if (!Ext.isEmpty(data.roomType)) {
                        data.roomType = 'roomTypeId=' + data.roomType + '&';
                    }

                    if (!data.startDate) data.startDate = Ext.Date.format(Ext.Date.clearTime(new Date()), 'U');
                    if (!data.endDate) data.endDate = Ext.Date.format(Ext.Date.add(new Date(data.startDate * 1000), Ext.Date.DAY, 1), 'U');

                    if (win.down('schPanel')) {
                        var sched = win.down('schPanel').down('grid'),
                            resourceStore = sched.getResourceStore(),
                            eventStore = sched.getEventStore();
                    }

                    data.startDate = Pms.toUTC(data.startDate);
                    data.endDate = Pms.toUTC(data.endDate);

                    Pms.Ajax.request({
                        url: 'rest/roomUse/getFreeRooms?' + data.roomType + 'start=' + data.startDate + '&end=' + data.endDate,
                        method: 'GET',
                        success: function (response) {
                            var filter = [
                                    {
                                        field: 'approved',
                                        comparison: 'eq',
                                        data: {
                                            type: 'boolean',
                                            value: true
                                        }
                                    }
                                ],
                                ids = [];
                            store.loadData(response.content);
                            if (win.down('schPanel')) {
                                resourceStore.loadData(response.content);
//                            store.load({params: {filter: filter}});
//                            resourceStore.load({params: {filter: filter}});
                                eventStore.reload();
//                            
                                if (!data.startDate) sched.scrollToDate(Ext.Date.clearTime(new Date()), true);
                                else sched.scrollToDate(new Date(data.startDate * 1000), true);
                            }
                            if (win.down('groupRoomUseRoomGrid')) {
                                var groupRoomUseStore = win.down('groupRoomUseRoomGrid').getStore(),
                                    groupRoomUseRecords = groupRoomUseStore.data.items;
                                store.filterBy(
                                    function (rec) {
                                        for (var i in groupRoomUseRecords) {
                                            if (groupRoomUseRecords[i].data.id == rec.data.id)
                                                return false
                                        }
                                        return true
                                    }
                                );
                                var startDateField = win.down('groupRoomUseRoomGrid').columns[5].editor ? win.down('groupRoomUseRoomGrid').columns[5].editor : win.down('groupRoomUseRoomGrid').columns[5].field;
                                var endDateField = win.down('groupRoomUseRoomGrid').columns[6].editor ? win.down('groupRoomUseRoomGrid').columns[6].editor : win.down('groupRoomUseRoomGrid').columns[6].field;
                                startDateField.minValue = new Date(data.startDate * 1000);
                                startDateField.maxValue = new Date(data.endDate * 1000);
                                endDateField.minValue = new Date(data.startDate * 1000);
                                endDateField.maxValue = new Date(data.endDate * 1000);
                                win.data = data;
                            }
                        },
                        failure: function (response) {
                            console.log(response);
                            Ext.Msg.alert(l('error'), l('searchingError'));
                        }
                    });
                }
            },
            {
                iconCls: 'app-icon-refund',
                text: l('resetFilter'),
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        form = win.down('roomUseFilterForm').getForm(),
                        grid = win.down('freeRoomGrid');

                    form.reset();
//                    grid.resetFilter();
                    grid.getStore().removeAll();
                    if (win.down('schPanel')) {
                        var sched = win.down('schPanel').down('grid'),
                            resourceStore = sched.getResourceStore(),
                            eventStore = sched.getEventStore();

                        resourceStore.load({params: {
                            filter: [
                                {
                                    field: 'approved',
                                    comparison: 'eq',
                                    data: {
                                        type: 'boolean',
                                        value: true
                                    }
                                }
                            ]
                        }
                        });
                        eventStore.reload();
                        sched.scrollToDate(Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, -6), true);
                    }
                }
            }
        ]
    }
})
;
