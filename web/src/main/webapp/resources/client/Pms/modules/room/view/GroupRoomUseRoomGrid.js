Ext.define('Pms.modules.room.view.GroupRoomUseRoomGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupRoomUseRoomGrid',
    store: 'Pms.abstract.Store',

    storeParams: {
        model: 'Pms.modules.room.model.Room',
        proxy: {
            type: 'memory',
            reader: 'array'
        },
        data: null
    },
    modelData: null,
    itemId: 'groupRoomUseRoomGrid',
    paging: false,

    initComponent: function () {
        var me = this;
        var dataArr = [],
            room;
        me.plugins = [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1,
            listeners: {
                beforeedit: function (editor, context, eOpts) {
                    if (context.record.data.exist)
                        return false
                }
            }
        })];
        if (me.modelData) {
            Ext.each(me.modelData, function (rec) {
                room = Ext.create('Pms.modules.room.model.Room', rec.data.room);
                room.data.startDate = rec.data.startDate;
                room.data.endDate = rec.data.endDate;
                room.data.plan = rec.data.plan;
                room.data.baseRoom = rec.data.baseRoom;
                room.data.exist = true;
                dataArr.push(room);
            });
            me.storeParams.data = dataArr;
            delete me.modelData
        }
        else me.storeParams.data = null;

        me.viewConfig = {
            plugins: {
                ptype: 'gridviewdragdrop',
                dragGroup: 'groupRoomUseRoomGrid',
                dropGroup: 'freeRoomGrid'
            },
            listeners: {
                drop: function (node, data, dropRec, dropPosition) {
                    var win = this.up('window'),
                        startDate = win.data.startDate ? new Date(win.data.startDate * 1000) : new Date(),
                        endDate = win.data.endDate ? new Date(win.data.endDate * 1000) : new Date(),
                        records = data.records;
                    for (var i = 0; i < records.length; i++) {
                        records[i].data.startDate = startDate;
                        records[i].data.endDate = endDate;
                    }
                    this.refresh()
                }
            }
        };
        me.columns = [
            {
                header: l('room'),
                sortable: true,
                width: 20,
                dataIndex: 'number'
            },
            {
                header: l('floor'),
                sortable: false,
                width: 20,
                dataIndex: 'floor'
            },
            {
                header: l('roomType'),
                sortable: false,
                width: 80,
                dataIndex: 'roomType',
                renderer: function (v) {
                    return v.name;
                }
            },
            {
                header: l('baseRoom'),
                sortable: false,
                width: 80,
                dataIndex: 'baseRoom',
                renderer: function (value, raw, rec) {
                    if (rec.data.exist) {
                        return value.name;
                    }
                    var roomTypeId = rec.data.roomType.id,
                        defaultBaseRoom = null;
                    Pms.Ajax.request({
                        url: 'rest/baseRoom/defaultByRoomType/' + roomTypeId,
                        method: 'GET',
                        async: false,
                        success: function (resp) {
                            defaultBaseRoom = resp.content
                            rec.data.baseRoom = defaultBaseRoom.id;
                            rec.data.type = defaultBaseRoom.type;
                        },
                        failure: function(){

                        }
                    })
                    if (!value){
                        return defaultBaseRoom ?  defaultBaseRoom.name : '<span style="color:red;font-weight:bold">' + l('enterValue') + '</span>';
                    }
                    var combo = this.down('combobox[name=baseRoom]');
                    if(!combo){
                        return defaultBaseRoom.name
                    }
                    //В отфильтрованой сторе не может найти индекс нужного размещения
                    combo.store.removeFilter();
                    var idx = combo.store.find(combo.valueField, value);
                    var rec = combo.store.getAt(idx);
                    combo.store.filterBy(combo.store.filFun);
                    return (rec === null ? '' : rec.get(combo.displayField) );
                },
                editor: {
                    xtype: 'combobox',
                    allowBlank: false,
                    name: 'baseRoom',
                    typeAhead: true,
                    value: l('enterValue'),
                    triggerAction: 'all',
                    selectOnTab: true,
                    store: Ext.create('Pms.modules.roomType.store.BaseRoom', {filterParams: [
                        {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                    ]}).load(),
                    displayField: 'name',
                    valueField: 'id',
                    queryMode: 'local',
                    listConfig: {
                        getInnerTpl: function () {
                            return '<div class="search-item">' +
                                '<span><b>{name}</b> {adults}, {children}, {additional} </span>' +
                                '</div>';
                        }
                    },
                    listeners: {
                        expand: function (field) {
                            var rec = field.up('grid').getSelectionModel().getSelection()[0],
                                roomTypeId = rec.data.roomType.id,
                                store = field.getStore();
                            store.filFun = function (rec, id) {
                                return ((rec.data.roomType.id == roomTypeId && rec.data.type == 'virtualRoom') || (rec.data.id == roomTypeId && rec.data.type == 'roomType'))
                            }
                            field.getStore().filterBy(store.filFun);
                        },
                        change: function (combo, value) {
                            var grid = combo.up('grid');
                            if (grid) {
                                var rec = combo.up('grid').getSelectionModel().getSelection()[0],
                                    comboRecord = combo.findRecordByValue(value);
                                rec.data.type = comboRecord.data.type;
                            }
                        }
                    }
                }
            },
            {
                header: l('plan'),
                sortable: false,
                width: 100,
                dataIndex: 'plan',
                renderer: function (value, raw, rec) {
                    value = rec.data.plan;
                    var defaultPlan = null;
                    if (rec.data.exist) {
                        return value.name;
                    }
                    Pms.Ajax.request({
                        url: 'rest/plan/default',
                        method: 'GET',
                        async: false,
                        success: function (resp) {
                            defaultPlan = resp.content;
                            rec.data.plan = defaultPlan.id
                        },
                        failure: function () {

                        }
                    });

                    if (!value) return '<span style="color:red;font-weight:bold">' + l('enterValue') + '</span>';

                    var combo = this.down('combobox[name=plan]');
                    if(!combo) return defaultPlan.name
                    var idx = combo.store.find(combo.valueField, value);
                    var rec = combo.store.getAt(idx);
                    return (rec === null ? '' : rec.get(combo.displayField) );
                },
                editor: {
                    xtype: 'combobox',
                    typeAhead: true,
                    allowBlank: false,
                    store: Ext.create('Pms.modules.plan.store.Plan', {filterParams: [
                        {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                    ]}).load(),
                    displayTpl: '',
                    value: l('enterValue'),
                    triggerAction: 'all',
                    name: 'plan',
                    selectOnTab: true,
                    displayField: 'name',
                    valueField: 'id',
                    queryMode: 'local',
                    listConfig: {
                        getInnerTpl: function () {
                            return '<div class="search-item">' +
                                '<span><b>{name}</b> ({board})</span>' +
                                '</div>';
                        }
                    }
                }
            },
            {
                header: l('startDate'),
                dataIndex: 'startDate',
                renderer: Ext.util.Format.dateRenderer('d M, Y'),
                editor: {
                    xtype: 'pmsdatefield',
                    allowBlank: false,
                    disabledDaysText: l('BookingAreNotAvailable')
                }
            },
            {
                header: l('endDate'),
                dataIndex: 'endDate',
                renderer: Ext.util.Format.dateRenderer('d M, Y'),
                editor: {
                    xtype: 'pmsdatefield',
                    allowBlank: false,
                    disabledDaysText: l('BookingAreNotAvailable')
                }
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                header: l('actions'),
                width: 25,
                items: [
                    {
                        iconCls: 'app-icon-delete',
                        tooltip: l('delete'),
                        isDisabled: function (store, index, row, obj, rec) {
                            if (rec.data.exist) return true
                        },
                        handler: function (grid, rowIndex, colIndex) {
                            grid.getStore().removeAt(rowIndex);
                        }
                    }
                ]
            }
        ];
        me.callParent();
    }
})
;