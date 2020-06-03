Ext.define('Pms.modules.groupRoomUse.view.customerGroupDiscountWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.customerGroupDiscountWindow',
    requires: ['Pms.abstract.field.Money'],
    title: l('person.setDiscount'),
    resizable: false,
    width: 290,
    height: 175,
    roomUseId: null,
    groupId: null,
    isGroupForm: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'form',
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        fieldLabel: l('discount.currencyType'),
                        xtype: 'combobox',
                        name: 'currency',
                        editable: false,
                        displayField: 'name',
                        store: Ext.create('Ext.data.Store', {
                            fields: [ 'name'],
                            data: [
                                {name: l('discount.percent')},
                                {name: l('discount.currency')}
                            ]
                        }),
                        padding: 10,
                        value: l('discount.percent'),
                        listeners: {
                            change: function (combo, val) {
                                var percent = combo.nextNode();
                                var currency = percent.nextNode();
                                var bill = currency.nextNode();
                                if (val == l('discount.currency')) {
                                    percent.hide();
                                    percent.allowBlank = true;
                                    currency.show();
                                    currency.allowBlank = false;
                                    bill.show();
                                    bill.allowBlank = false;
                                }
                                else {
                                    percent.show();
                                    percent.allowBlank = false;
                                    currency.hide();
                                    currency.allowBlank = true;
                                    bill.hide();
                                    bill.allowBlank = true;
                                }
                            }
                        }
                    },
                    {
                        fieldLabel: l('discount.valueInPercent') + Pms.requiredStatus,
                        xtype: 'numberfield',
                        name: 'discount',
                        minValue: 0,
                        maxValue: 100,
                        padding: '0 0 0 10',
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('discount.valueInCurrency') + Pms.requiredStatus,
                        xtype: 'moneyfield',
                        padding: '0 0 0 10',
                        name: 'currencyDiscount',
                        hidden: true
                    },
                    {
                        fieldLabel: l('bill') + Pms.requiredStatus,
                        xtype: 'combobox',
                        hidden: true,
                        displayField: 'id',
                        padding: '0 0 0 10',
                        editable: false,
                        name: 'bill',
                        store: Ext.create('Pms.modules.bill.store.Bill', {
                            loadParams: me.isGroupForm ? {
                                params: {
                                    connective: 'or',
                                    filter: [
                                        {
                                            field: 'group.id',
                                            comparison: 'eq',
                                            data: {
                                                type: 'numeric',
                                                value: me.groupId
                                            }
                                        },
                                        {
                                            field: 'roomUse.customerGroup.id',
                                            comparison: 'eq',
                                            data: {
                                                type: 'numeric',
                                                value: me.groupId
                                            }
                                        },
                                    ]
                                }
                            } : {
                                params: {
                                    filter: [
                                        {
                                            field: 'roomUse.id',
                                            comparison: 'eq',
                                            data: {
                                                type: 'numeric',
                                                value: me.roomUseId
                                            }
                                        }
                                    ]
                                }
                            }
                        }).load(),
                        listConfig: {
                            emptyText: l('error.noMatches'),
                            getInnerTpl: function () {
                                return '<div class="search-item">' +
                                    '<span><b>' + l('bill') + ' №{id}</b> ({totalPaid:/100}/{total:/100})</span>' +
                                    '</div>';
                            }
                        }
                    },
                ]
            }
        ];
        this.buttons = [
            {
                text: l('setDiscount'),
                iconCls: 'fa fa-reply',
                handler: function (button) {
                    var win = button.up('customerGroupDiscountWindow'),
                        form = win.down('form').getForm(),
                        values = form.getValues(),
                        billId = win.down('combobox[name=bill]').getValue(),
                        currencyType = win.down('combobox[name=currency]').getValue();
                    if (form.isValid()) {
                        if (currencyType == l('discount.percent')) {
                            Pms.Ajax.request({
                                url: 'rest/customerGroup/' + values.id + '/discount',
                                jsonData: {discount: parseInt(values.discount)},
                                method: 'PUT',
                                prevError: true,
                                success: function () {
                                    win.close();
                                    var serviceUseGrid = Ext.ComponentQuery.query('serviceUseGroupGrid')[0];
                                    serviceUseGrid.data.discount = parseInt(values.discount);
                                    serviceUseGrid.getStore().reload({
                                        callback: function () {
                                            Pms.App.showNotification({
                                                message: l('discountSaved'),
                                                icon: Pms.notificationOk
                                            });
                                        }
                                    });
                                },
                                failure: Pms.bankDetailsRequired
                            })
                        }
                        else {
                            Pms.Ajax.request({
                                url: 'rest/bill/' + billId + '/discount',
                                jsonData: {value: parseInt(values.currencyDiscount * 100)},
                                method: 'PUT',
                                prevError: true,
                                success: function () {
                                    win.close();
                                    var serviceUseGrid = Ext.ComponentQuery.query('serviceUseGroupGrid')[0];
//                                    serviceUseGrid.data.value = parseInt(values.currencyDiscout*100);
                                    serviceUseGrid.getStore().reload({
                                        callback: function () {
                                            Pms.App.showNotification({
                                                message: l('discountSaved'),
                                                icon: Pms.notificationOk
                                            });
                                        }
                                    });
                                },
                                failure: Pms.bankDetailsRequired
                            })
                        }
                    } else {
                        Ext.Msg.alert(l('error'), l('discountNotSaved'));
                    }
                }
            },
            {
                text: l('cancel.btn'),
                iconCls: 'fa fa-times',
                scope: this,
                handler: me.close
            }
        ]

        this.callParent(arguments);
    }
});