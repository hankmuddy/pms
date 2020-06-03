Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseMoveForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseMoveForm',

    // title: 'Данные отказа',
    layout: 'vbox',
    autoscroll: true,
    fileupload: true,
    height: 450,
    data: null,
    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'hidden',
                name: 'id',
                value: me.data.id
            },
            {
                xtype: 'pmsdatefield',
                fieldLabel: l('moveDate') + Pms.requiredStatus,
                name: 'sinceDate',
                format: 'd/m/y',
                submitFormat: 'U',
                maxValue: me.data.endDate,
                minValue: /*(new Date() > */me.data.startDate/*) ? new Date() : me.data.startDate*/,
                value: (new Date() > me.data.startDate) ? new Date() : me.data.startDate,
                allowBlank: false
            },
            {
                fieldLabel: l('room') + Pms.requiredStatus,
                name: 'room',
                allowBlank: false,
                xtype: 'combobox',
                store: Ext.create('Pms.modules.room.store.Room').load({params: {filter: [
                    {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}},
                    {field: 'id', comparison: 'neq', data: {type: 'numeric', value: me.data.room.id}}
                ]}
                }),
                displayField: 'number',
                valueField: 'id',
                queryMode: 'local',
                listConfig: {
                    emptyText: l('error.noMatches'),
                    getInnerTpl: function () {
                        return '<div class="search-item">' +
                            '<span><b>{number}</b>({roomType.name}) | {accommodation.name}</span>' +
                            '</div>';
                    }
                },
                listeners: {
                    change: function (field) {
                        field.nextNode().setValue('');
                    },
                    expand: function (field) {
                        var prevNode = field.previousNode(),
                            startDate = parseInt(prevNode.getSubmitValue()) - new Date().getTimezoneOffset() * 60,
                            endDate = Pms.toUTC(me.data.endDate),
                            store = field.getStore();
                        Pms.Ajax.request({
                            url: 'rest/roomUse/getFreeRooms?start=' + startDate + '&end=' + endDate,
                            method: 'GET',
                            success: function (response) {
                                store.loadData(response.content);
                            },
                            failure: function (response) {
                                Ext.Msg.alert(l('error'), l('searchingError'));
                            }
                        });
                    }
                }
            },
            {
                name: 'baseRoom',
                xtype: 'combobox',
                allowBlank: false,
                fieldLabel: l('baseRoom') + Pms.requiredStatus,
                store: Ext.create('Pms.modules.roomType.store.BaseRoom', {filterParams: [
                    {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                ]}).load(),
                displayField: 'name',
                valueField: 'id',
                queryMode: 'local',
                validateOnChange: false,
                listConfig: {
                    getInnerTpl: function () {
                        return '<div class="search-item">' +
                            '<span><b>{name}</b> {adults}, {children}, {additional} </span>' +
                            '</div>';
                    }
                },
                listeners: {
                    expand: function (field) {
                        if (field.previousNode().findRecord() && field.previousNode().findRecord().data) {
                            var prevNode = field.previousNode(),
                                val = parseInt(prevNode.getValue()),
                                roomTypeId = prevNode.findRecordByValue(val).data.roomType.id;
                            field.getStore().filterBy(function (rec, id) {
                                return ((rec.data.roomType.id == roomTypeId && rec.data.type == 'virtualRoom') || (rec.data.id == roomTypeId && rec.data.type == 'roomType'))
                            });
                        }
                    },
                    change: function (combo, value) {
                        var comboRecord = combo.findRecordByValue(value);
                        me.baseRoomType = comboRecord.data.type;
                    }
                }
            },
            {
                xtype: 'checkboxfield',
                name: 'customerPays',
//                                    marginLeft: 25,
                anchor: '100%',
                fieldLabel: l('customerPays'),
//                                    hidden: true,
//                                    labelWidth: 100,
                inputValue: true,
                uncheckedValue: false,

            },
            {
                xtype: 'checkboxfield',
                name: 'upgrade',
                fieldLabel: l('groupRoomUse.upgrade'),
                anchor: '100%',
                inputValue: true,
                uncheckedValue: false,
                listeners: {
                    change: function (checkBox, newValue) {
                        !newValue ? checkBox.nextNode().show() :
                            checkBox.nextNode().hide();
                    }
                }
            }
            ,
            {
                fieldLabel: l('plan'),
                name: 'plan',
                xtype: 'combobox',
                store: Ext.create('Pms.modules.plan.store.Plan').load(),
                loadParams: {
                    params: {
                        filter: [
                            {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                        ]
                    }
                },
                valueField: 'id',
                displayField: 'name',
                queryMode: 'local',
                listConfig: {
                    emptyText: l('company.searchEmptyText'),
                    getInnerTpl: function () {
                        return '<div class="search-item">' +
                            '<span><b>{name}</b> ({board})</span>' +
                            '</div>';
                    }
                },
            }

//        {
//            xtype: 'textareafield',
//            grow: true,
//            name: 'description',
//            fieldLabel: l('description'),
//            anchor: '100%'
//        }
        ]
        this.callParent(arguments);
    }


})
;
