Ext.define("Pms.modules.booking.view.BookingFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.bookingFilterForm',
    bookingGridName: null,
    requires: ['Pms.abstract.field.lookup.Picker'],

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.bottomToolbar();

        me.callParent();
    },

    buildItems: function () {
        return    {
            xtype: 'container',
            title: l('filters'),
            padding: 10,
            defaults: {labelWidth: 130},
            items: [
//                {
//                    fieldLabel: l('guest'),
//                    xtype: 'combobox',
//                    store: Ext.create('Pms.modules.person.store.Person', {fromSearchCombo: true}),
//                    name: 'customer',
//                    displayField: 'lastName',
//                    valueField: 'id',
//                    typeAhead: false,
//                    anchor: '100%',
//                    emptyText: l('lastName'),
//                    querymode: 'remote',
//                    queryParam: '',
//                    minChars: 1,
//                    listConfig: {
//                        loadingText: l('searchText'),
//                        emptyText: l('error.noMatches'),
//                        getInnerTpl: function () {
//                            return '<div class="search-item">' +
//                                '<span><b>{lastName} {firstName} {patronymic}</b></span>' +
//                                '<tpl if="property_birth">' +
//                                '<span style="float:right;color:#999999"><i>' +
//                                'Дата рождения: {[fm.date(values.dob,"d/m/Y")]}' +
//                                '<tpl if="city"><br />{city}, </tpl>' +
//                                '<tpl if="adress">{address}</tpl>' +
//                                '</i></span>' +
//                                '</tpl>' +
//                                '<br /><i>{email}</i><br /><i>{phone}</i>' +
//                                '</div>';
//                        }
//                    },
//                    listeners: {
//                        change: function (combo, newValue, oldValue, eOpts) {
//                            if (newValue && newValue.length > 0) {
//                                combo.store.filterParams = [
//                                    {
//                                        field: 'lastName',
//                                        comparison: 'like',
//                                        data: {
//                                            type: 'string',
//                                            value: newValue
//                                        }
//                                    }
//                                ];
//                            }
//                        }
//                    },
//                    trigger1Cls: 'x-form-clear-trigger',
//                    onTrigger1Click: function () {
//                        this.clearValue();
//                    }
//                },
//                {
//                    fieldLabel: l('company'),
//                    xtype: 'combobox',
//                    store: Ext.create('Pms.modules.company.store.Company', {fromSearchCombo: true}),
//                    name: 'company',
//                    displayField: 'name',
//                    valueField: 'id',
//                    typeAhead: false,
//                    anchor: '100%',
//                    emptyText: l('company'),
//                    querymode: 'remote',
//                    queryParam: '',
//                    minChars: 1,
//                    listConfig: {
//                        loadingText: l('searchText'),
//                        emptyText: l('error.noMatches'),
//                        getInnerTpl: function () {
//                            return '<div class="search-item">' +
//                                '<b>{name}</b>' +
//                                '</div>';
//                        }
//                    },
//                    listeners: {
//                        change: function (combo, newValue, oldValue, eOpts) {
//                            if (newValue && newValue.length > 0) {
//                                combo.store.filterParams = [
//                                    {
//                                        field: 'name',
//                                        comparison: 'like',
//                                        data: {
//                                            type: 'string',
//                                            value: newValue
//                                        }
//                                    }
//                                ];
//                            }
//                        }
//                    },
//                    trigger1Cls: 'x-form-clear-trigger',
//                    onTrigger1Click: function () {
//                        this.clearValue();
//                    }
//                },
                {
                    fieldLabel: l('room'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.room.store.Room'),
                    name: 'room',
                    displayField: 'number',
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
                    },
                    hidden: Pms.App.isExtranet()
                },
                {
                    fieldLabel: l('roomType'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.roomType.store.RoomType'),
                    name: 'roomType',
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
                    fieldLabel: l('approved'),
                    xtype: 'combobox',
                    name: 'status',
                    store: Ext.create('Pms.modules.roomUse.store.useType'),
                    displayField: 'label',
                    valueField: 'id',
                    triggerAction: 'all',
                    queryMode: 'local',
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
                    fieldLabel: l('paid'),
                    xtype: 'combobox',
                    store: Ext.create('Ext.data.ArrayStore', {
                        fields: ['label', 'value'],
                        data: [
                            [l('yes'), true],
                            [l('no'), false]
                        ]
                    }),
                    name: 'paid',
                    displayField: 'label',
                    valueField: 'value',
                    triggerAction: 'all',
                    queryMode: 'local',
                    queryParam: '',
                    selectOnFocus: true,
                    indent: true,
                    anchor: '100%',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-arrow-trigger',
                    onTrigger1Click: function () {
                        this.clearValue();
                    },
                    hidden: Pms.App.isExtranet()
                },
                {
                    fieldLabel: l('roomUse.source'),
                    name: 'source',
                    allowBlank: false,
                    lookupType: 'source',
                    xtype: 'lookupCombobox',
                    valueNotFoundText: null
                },
                {
                    fieldLabel: l('booking.date'),
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
        var me = this;
        return  [
            {
                xtype: 'button',
                iconCls: 'app-icon-ok',
                text: l('apply'),
                action: 'apply-filter',
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        filterForm = btn.up('bookingFilterForm'),
                        form = filterForm.getForm(),
                        data = form.getValues(),
                        grid = win.down('bookingGrid'),
                        params = {},
                        filter = [],
                        connective = null;

                    if (filterForm.bookingGridName) {
                        grid = win.down('#' + filterForm.bookingGridName);
                    }

                    if (!Ext.isEmpty(data.customer)) {
                        if (isNaN(data.customer) || parseInt(data.customer) != data.customer) {
                            Pms.App.showNotification({message: l('warning.invalidInput')});
                        }
                        else {
                            filter.push({
                                field: 'customerGroup.customer.id',
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
                                field: 'customerGroup.company.id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: data.company
                                }
                            });
                        }
                    }

                    if (data.room) filter.push({
                        field: 'room',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: data.room
                        }
                    });

                    if (data.roomType) filter.push({
                        field: 'room.roomType.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: data.roomType
                        }
                    });

                    if (data.status) filter.push({
                        field: 'status',
                        comparison: 'eq',
                        data: {
                            type: 'room_use_status',
                            value: data.status
                        }
                    });

                    if(data.paid === true) {
                        filter.push({
                            field: 'total',
                            comparison: 'eq',
                            data: {
                                type: 'field',
                                value: 'totalPaid'
                            }
                        });
                    }
                    else if(data.paid === false) {
                        filter.push({
                            field: 'total',
                            comparison: 'neq',
                            data: {
                                type: 'field',
                                value: 'totalPaid'
                            }
                        });
                    }

                    if (data.source) filter.push({
                        field: 'source',
                        comparison: 'eq',
                        data: {
                            type: 'room_use_source',
                            value: data.source
                        }
                    });

                    if (data.createdDate) {
                        filter.push({
                            field: 'createdDate',
                            comparison: 'gte',
                            data: {
                                type: 'datetime',
                                value: Pms.toUTC(Ext.Date.format(Ext.Date.clearTime(new Date(data.createdDate * 1000)), 'U'))
                            }
                        });
                        filter.push({
                            field: 'createdDate',
                            comparison: 'lte',
                            data: {
                                type: 'datetime',
                                value: Pms.toUTC(Ext.Date.format(Ext.Date.add(Ext.Date.clearTime(new Date(data.createdDate * 1000)), Ext.Date.DAY, 1), 'U'))
                            }
                        });
                    }

                    if (data.startDate) {
                        filter.push({
                            field: 'startDate',
                            comparison: 'gte',
                            data: {
                                type: 'date',
                                value: Pms.toUTC(data.startDate)//Ext.Date.format(Ext.Date.clearTime(new Date(data.startDate * 1000)), 'U')
                            }
                        });
                    }

                    if (data.endDate) {
                        filter.push({
                            field: 'endDate',
                            comparison: 'lte',
                            data: {
                                type: 'date',
                                value: Pms.toUTC(data.endDate)//Ext.Date.format(Ext.Date.add(Ext.Date.clearTime(new Date(data.endDate * 1000)), Ext.Date.DAY, 1), 'U')
                            }
                        });
                    }

                    grid.applyFilter(params, filter, connective);
                }
            },
            {
                iconCls: 'app-icon-refund',
                text: l('resetFilter'),
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        filterForm = btn.up('bookingFilterForm'),
                        form = filterForm.getForm(),
                        grid = win.down('bookingGrid');

                    if (filterForm.bookingGridName) {
                        grid = win.down('#' + filterForm.bookingGridName);
                    }
                    form.reset();
                    grid.resetFilter();
                }
            }
        ]
    }
});