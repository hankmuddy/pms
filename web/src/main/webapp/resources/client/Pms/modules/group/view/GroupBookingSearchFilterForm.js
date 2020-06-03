Ext.define("Pms.modules.group.view.GroupBookingSearchFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.groupBookingSearchFilterForm',

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
                                l('Дата рождения') + ': {[fm.date(values.dob,"d/m/Y")]}' +
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
                    }
                },
                {
                    fieldLabel: l('pov'),
                    xtype: 'lookupCombobox',
                    name: 'pov',
                    lookupType: 'pov',
                    valueNotFoundText: null,
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-arrow-trigger',
                    onTrigger1Click: function () {
                        this.clearValue();
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
                        form = win.down('groupBookingSearchFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('groupBookingSearchGrid'),
                        params = {},
                        filter = [],
                        connective = null;

                    if (!Ext.isEmpty(data.customer)) {
                        if (isNaN(data.customer) || parseInt(data.customer) != data.customer) {
                            Pms.App.showNotification({message: l('warning.invalidInput')});
                        }
                        else {
                            filter.push({
                                field: 'customer.id',
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
                                field: 'company.id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: data.company
                                }
                            });
                        }
                    }

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

                    if (!Ext.isEmpty(data.pov)) {
                        filter.push({
                            field: 'pov',
                            comparison: 'eq',
                            data: {
                                type: 'pov',
                                value: data.pov
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
                        form = win.down('groupBookingSearchFilterForm').getForm(),
                        grid = win.down('groupBookingSearchGrid');

                    form.reset();
                    grid.resetFilter();
                }
            }
        ]
    }
});