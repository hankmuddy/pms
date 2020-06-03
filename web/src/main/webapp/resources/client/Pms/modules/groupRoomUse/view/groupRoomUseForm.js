Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseForm',
    title: l('roomUse.arrivalData'),

    layout: 'vbox',
    height: 220,

    items: [
        {
            xtype: 'container',
            margin: 5,
            layout: 'hbox',
            items: [
                {
                    xtype: 'container',
                    margin: 5,
                    layout: 'vbox',
                    items: [

                        {
                            xtype: 'fieldset',
                            title: l('room'),
                            defaultType: 'textfield',

                            items: [
                                {
                                    fieldLabel: l('room'),
                                    name: 'room',
                                    xtype: 'combobox',
                                    store: Ext.create('Pms.modules.room.store.Room', {filterParams: [
                                        {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                                    ]}).load(),
                                    displayField: 'number',
                                    valueField: 'id',
                                    queryMode: 'remote',
                                    listConfig: {
                                        emptyText: l('error.noMatches'),
                                        getInnerTpl: function () {
                                            return '<div class="search-item">' +
                                                '<span><b>{number}</b> ({roomType.name}) | {accommodation.name}</span>' +
                                                '</div>';
                                        }
                                    },
                                    listeners: {
                                        change: function (field) {
                                            field.nextNode().setValue('');
                                        }
                                    }
                                },
                                {
                                    fieldLabel: l('baseRoom') + Pms.requiredStatus,
                                    name: 'baseRoom',
                                    xtype: 'combobox',
                                    allowBlank: false,
                                    validateOnChange: false,
                                    store: Ext.create('Pms.modules.roomType.store.BaseRoom', {filterParams: [
                                        {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                                    ]}).load(),
                                    displayField: 'name',
                                    valueField: 'id',
                                    queryMode: 'local',
                                    listConfig: {
                                        maxHeight: 100,
                                        getInnerTpl: function () {
                                            return '<div class="search-item">' +
                                                '<span><b>{name}</b> {adults}, {children}, {additional} </span>' +
                                                '</div>';
                                        }
                                    },
                                    listeners: {
                                        expand: function (field) {
                                            var prevNode = field.previousNode(),
                                                prevVal = prevNode.getValue(),
                                                prevRecord = prevNode.findRecordByValue(prevVal);
                                            if (prevRecord && prevRecord.data) {
                                                var roomTypeId = prevRecord.data.roomType.id;
//                                                var store = Ext.create('Pms.modules.roomType.store.BaseRoom',
//                                                    {
//                                                        url: 'rest/baseRoom/byRoom/' + roomId
//                                                    });
//                                                field.bindStore(store);
                                                field.getStore().filterBy(function (rec, id) {
                                                    return ((rec.data.roomType.id == roomTypeId && rec.data.type == 'virtualRoom') || (rec.data.id == roomTypeId && rec.data.type == 'roomType'))
                                                });
//                                                store.load();
                                            }
                                        },
                                        render: function (combo) {
                                            var prevNode = combo.previousNode(),
                                                prevVal = prevNode.getValue();
                                            if(!prevVal) return
                                            var prevRecord = prevNode.findRecordByValue(prevVal),
                                                roomTypeId = prevRecord.data.roomType.id;
                                            Pms.Ajax.request({
                                                url: 'rest/baseRoom/defaultByRoomType/' + roomTypeId,
                                                method: 'GET',
                                                success: function (resp) {
                                                    combo.store.load();
                                                    combo.setValue(resp.content.id);
                                                },
                                                failure: function(){

                                                }
                                            })
                                        },
                                    }
                                },
                                {
                                    fieldLabel: l('plan') + Pms.requiredStatus,
                                    name: 'plan',
                                    xtype: 'combobox',
                                    store: Ext.create('Pms.modules.plan.store.Plan', {filterParams: [
                                        {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                                    ]}),
                                    valueField: 'id',
                                    displayField: 'name',
                                    queryMode: 'remote',
                                    validateOnChange: false,
                                    allowBlank: false,
                                    listeners: {
                                        render: function (combo) {
                                            Pms.Ajax.request({
                                                url: 'rest/plan/default',
                                                method: 'GET',
                                                success: function (resp) {
                                                    combo.store.load();
                                                    combo.setValue(resp.content.id);
                                                },
                                                failure: function(){

                                                }
                                            })
                                        }
                                    },
                                    listConfig: {
                                        emptyText: l('error.noMatches'),
                                        getInnerTpl: function () {
                                            return '<div class="search-item">' +
                                                '<span><b>{name}</b> ({board})</span>' +
                                                '</div>';
                                        }
                                    },
                                },
//                                {
//                                    xtype: 'combobox',
//                                    fieldLabel: 'Действие',
//                                    name: 'status',
////                            store: Ext.create('Pms.modules.groupRoomUse.store.useType', {bookingWarranty: false, living: false, refuse: false, outgo: false, repair: false}).load(),
//                                    displayField: 'label',
//                                    valueField: 'name',
//                                    queryMode: 'remote',
//                                    listeners: {
//                                        focus: function (combo) {
//                                            var thisForm = combo.up('groupRoomUseForm'),
//                                                dateField = thisForm.down('datefield[name=startDate]'),
//                                                since = dateField.value,
//                                                today = new Date();
//
//                                            today.setHours(23, 59, 59, 999);
//                                            if (since > today) {
//                                                combo.store.living = false;
//                                                combo.store.load();
//                                            }
//                                            else {
//                                                combo.store.living = true;
//                                                combo.store.load();
//                                            }
//                                        }
//                                    }
//                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            margin: '0 0 0 10',
                            items: [
                                {
                                    xtype: 'lookupCombobox',
                                    fieldLabel: l('source'),
                                    name: 'source',
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
                                    fieldLabel: l('rcode') + Pms.requiredStatus,
                                    hidden: true,
                                    name: 'rcode'
                                }
                            ]
                        }

                    ]
                },
                {
                    xtype: 'container',
                    margin: 5,
                    items: [
                        {
                            xtype: 'fieldset',
                            title: l('Dates'),
                            defaultType: 'textfield',
                            items: [
                                {
                                    xtype: 'pmsdatefield',
                                    fieldLabel: l('checkIn') + Pms.requiredStatus,
                                    name: 'startDate',
                                    format: 'd/m/y',
//                                    minValue: new Date(),
                                    submitFormat: 'Y-m-d',
                                    allowBlank: false,
                                    listeners: {
                                        change: function (field, newVal, oldVal, e) {
                                            var endDate = field.up('form').down('datefield[name=endDate]');
                                            if (endDate.value < newVal) endDate.setValue('');
                                            var date = new Date(newVal.getTime() + 86400000);
                                            endDate.setMinValue(date);
                                        }
                                    }
                                },
                                {
                                    xtype: 'pmsdatefield',
                                    fieldLabel: l('checkOut') + Pms.requiredStatus,
                                    name: 'endDate',
                                    format: 'd/m/y',
                                    submitFormat: 'Y-m-d',
                                    allowBlank: false,
                                    listeners: {
                                        beforerender: function (field, e) {
                                            var startDate = field.up('form').down('datefield[name=startDate]');
                                            var date = new Date(startDate.value.getTime() + 86400000);
                                            field.setMinValue(date);
                                        }
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            items: [
                                {
                                    xtype: 'checkboxfield',
                                    fieldLabel: l('addToGroup'),
                                    name: 'isGroupMember',
                                    labelWidth: 200,
                                    inputValue: true,
                                    checked: true,
                                    value: true,
                                },
                                {
                                    xtype: 'checkboxfield',
                                    name: 'customerPays',
//                                    marginLeft: 25,
                                    fieldLabel: l('customerPays'),
//                                    hidden: true,
                                    labelWidth: 200,
                                    inputValue: true
                                }
                            ]
                        },
                    ]
                }
            ]
        }
    ]
});