Ext.define('Pms.modules.serviceUse.view.serviceUseForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.serviceUseForm',

    measure: '',
    customerGroup: null,
    sinceDate: null,
    toDate: null,
    service: null,
    serviceType: null,
    serviceUse: null,
    roomUse: null,
    listeners: {
        render: function () {
            if (this.serviceUse) {
                this.down('combobox').setValue(this.serviceUse.bill.id);
                this.down('hidden').setValue(this.serviceUse.service.id);
            }
        }
    },

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this,
            filterToGroup = Ext.ComponentQuery.query('button[cls=group-room-use-change-btn]')[0].filterToGroup,
            billFilterParams;

        me.customerGroup = me.customerGroup || me.serviceUse.bill.customerGroup;
        me.service = me.service || me.serviceUse.service;
        me.serviceType = me.serviceType || me.serviceUse.service.type;

        if (filterToGroup) {
            billFilterParams = {
                filter: [
                    {
                        field: 'roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: me.roomUse
                        }
                    }
                ]
            }
        }
        else {
            billFilterParams = {
                connective: 'or',
                filter: [
                    {
                        field: 'roomUse.customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: me.customerGroup.id
                        }
                    },
                    {
                        field: 'group.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: me.customerGroup.id
                        }
                    }
                ]
            }
        }
        return    {
            xtype: 'container',
            items: [
                {
                    xtype: 'hidden',
                    name: 'service',
                    value: me.service
                },
                {
                    xtype: 'hidden',
                    name: 'customerGroup'
                },
                {
                    xtype: 'hidden',
                    name: 'roomUse',
                    value: me.roomUse
                },
                {
                    fieldLabel: l('bill'),
                    xtype: 'combobox',
                    name: 'bill',
                    store: Ext.create('Pms.modules.bill.store.Bill', {
                        listeners: {
                            load: function (store, records) {
                                store.insert(0, [
                                    {
                                        id: l('serviceUse.newBillOnRoom')
                                    },
                                    {
                                        id: l('serviceUse.newBillOnCustomer')
                                    }
                                ]);
                            },
                            beforeload: function (store, operation, eOpts) {
                                if (!operation.params) {
                                    operation.params = {};
                                }
                                if (!Ext.Object.isEmpty(this.filterParams)) {
                                    operation.params.filter = this.filterParams;
                                }
                                if (!Ext.Object.isEmpty(this.loadParams)) {
                                    operation.params = this.loadParams.params;
                                }
                                if (!Ext.Object.isEmpty(operation.params)) {
                                    operation.params = Pms.Ajax.encode(operation.params);
                                }
                            }
                        },
                        loadParams: {
                            params: billFilterParams
                        }
                    }).load(),
                    displayField: 'id',
                    valueField: 'id',
                    queryMode: 'local'
                },
                {
                    fieldLabel: l('quantity') + ',&nbsp;' + l(me.measure),
                    xtype: 'numberfield',
                    name: 'quantity',
                    minValue: 1
                },
                {
                    fieldLabel: l('date'),
                    xtype: 'pmsdatefield',
                    name: 'date',
                    allowBlank: false,
                    minValue: me.sinceDate,
                    maxValue: me.toDate,
                    format: 'd/m/y',
                    submitFormat: 'U',
                    value: new Date()
                }
//                {
//                    fieldLabel: l('description'),
//                    xtype: 'textareafield',
//                    name: 'description'
//                }
            ]
        };
    }
});