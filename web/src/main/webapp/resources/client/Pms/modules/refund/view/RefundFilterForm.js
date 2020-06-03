Ext.define("Pms.modules.refund.view.RefundFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.refundFilterForm',

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.bottomToolbar();

        me.callParent();
    },

    buildItems: function () {
        return {
            xtype: 'container',
            title: l('filters'),
            padding: 10,
            defaults: {labelWidth: 150},
            items: [
                {
                    fieldLabel: l('guest'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.person.store.Person', {fromSearchCombo: true}),
                    name: 'customer',
                    displayField: 'lastName',
                    valueField: 'id',
                    typeAhead: false,
                    anchor: '100%',
                    emptyText: l('lastName'),
                    querymode: 'remote',
                    queryParam: '',
                    minChars: 1,
                    listConfig: {
                        loadingText: l('searchText'),
                        emptyText: l('error.noMatches'),
                        getInnerTpl: function () {
                            return '<div class="search-item">' +
                                '<span><b>{lastName} {firstName} {patronymic}</b></span>' +
                                '<tpl if="property_birth">' +
                                '<span style="float:right;color:#999999"><i>' +
                                l('dateOfBirth') + ': {[fm.date(values.dob,"d/m/Y")]}' +
                                '<tpl if="city"><br />{city}, </tpl>' +
                                '<tpl if="adress">{address}</tpl>' +
                                '</i></span>' +
                                '</tpl>' +
                                '<br /><i>{email}</i><br /><i>{phone}</i>' +
                                '</div>';
                        }
                    },
                    listeners: {
                        change: function (combo, newValue, oldValue, eOpts) {
                            if (newValue && newValue.length > 0) {
                                combo.store.filterParams = [
                                    {
                                        field: 'lastName',
                                        comparison: 'like',
                                        data: {
                                            type: 'string',
                                            value: newValue
                                        }
                                    }
                                ];
                            }
                        }
                    },
                    trigger1Cls: 'x-form-clear-trigger',
                    onTrigger1Click: function () {
                        this.clearValue();
                    }
                },
                {
                    fieldLabel: l('company'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.company.store.Company', {fromSearchCombo: true}),
                    name: 'company',
                    displayField: 'name',
                    valueField: 'id',
                    typeAhead: false,
                    anchor: '100%',
                    emptyText: l('company'),
                    querymode: 'remote',
                    queryParam: '',
                    minChars: 1,
                    listConfig: {
                        loadingText: l('searchText'),
                        emptyText: l('error.noMatches'),
                        getInnerTpl: function () {
                            return '<div class="search-item">' +
                                '<b>{name}</b>' +
                                '</div>';
                        }
                    },
                    listeners: {
                        change: function (combo, newValue, oldValue, eOpts) {
                            if (newValue && newValue.length > 0) {
                                combo.store.filterParams = [
                                    {
                                        field: 'name',
                                        comparison: 'like',
                                        data: {
                                            type: 'string',
                                            value: newValue
                                        }
                                    }
                                ];
                            }
                        }
                    },
                    trigger1Cls: 'x-form-clear-trigger',
                    onTrigger1Click: function () {
                        this.clearValue();
                    }
                },
                {
                    fieldLabel: l('date'),
                    xtype: 'pmsdatefield',
                    name: 'createdDate',
                    format: 'd/m/y',
                    submitFormat: 'U',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-date-trigger',
                    onTrigger1Click: function () {
                        this.setValue('');
                    }
                },
                {
                    fieldLabel: l('from'),
                    xtype: 'pmsdatefield',
                    name: 'startDate',
                    format: 'd/m/y',
                    submitFormat: 'U',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-date-trigger',
                    onTrigger1Click: function () {
                        this.setValue('');
                    }
                },
                {
                    fieldLabel: l('to'),
                    xtype: 'pmsdatefield',
                    name: 'endDate',
                    format: 'd/m/y',
                    submitFormat: 'U',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-date-trigger',
                    onTrigger1Click: function () {
                        this.setValue('');
                    }
                }
            ]
        };
    },

    bottomToolbar: function () {
        return  [
            {
                xtype: 'button',
                iconCls: 'app-icon-ok',
                text: l('apply'),
                action: 'apply-filter',
                handler: function (btn, e) {
                    var win = btn.up('billViewport'),
                        form = win.down('refundFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('refundGrid'),
                        params = {},
                        filter = [],
                        connective = null;

                    if (!Ext.isEmpty(data.customer)) {
                        if (isNaN(data.customer) || parseInt(data.customer) != data.customer) {
                            Pms.App.showNotification({message: l('warning.invalidInput')});
                        }
                        else {
                            filter.push({
                                field: 'group.customer.id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: data.customer
                                }
                            });
                        }
                    }

                    if (!Ext.isEmpty(data.company)) {
                        if (isNaN(data.company) || parseInt(data.company) != data.company) {
                            Pms.App.showNotification({message: l('warning.invalidInput')});
                        }
                        else {
                            filter.push({
                                field: 'group.company.id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: data.company
                                }
                            });
                        }
                    }

                    if (data.createdDate) {
                        filter.push({
                            field: 'createdDate',
                            comparison: 'gte',
                            data: {
                                type: 'datetime',
                                value: Ext.Date.format(Ext.Date.clearTime(new Date(data.createdDate * 1000)), 'U')
                            }
                        });
                        filter.push({
                            field: 'createdDate',
                            comparison: 'lte',
                            data: {
                                type: 'datetime',
                                value: Ext.Date.format(Ext.Date.add(Ext.Date.clearTime(new Date(data.createdDate * 1000)), Ext.Date.DAY, 1), 'U')
                            }
                        });
                    }

                    if (data.startDate) {
                        filter.push({
                            field: 'createdDate',
                            comparison: 'gte',
                            data: {
                                type: 'datetime',
                                value: data.startDate
                            }
                        });
                    }

                    if (data.endDate) {
                        filter.push({
                            field: 'createdDate',
                            comparison: 'lt',
                            data: {
                                type: 'datetime',
                                value: Ext.Date.format(Ext.Date.add(Ext.Date.clearTime(new Date(data.endDate * 1000)), Ext.Date.DAY, 1), 'U')
                            }
                        });
                    }

                    grid.applyFilter(params, filter, connective);
                }
            },
            {
                iconCls: 'app-icon-refund',
                text: l(''),
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        form = win.down('refundFilterForm').getForm(),
                        grid = win.down('refundGrid');

                    form.reset();
                    grid.resetFilter();
                }
            }
        ];
    }
});