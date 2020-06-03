Ext.define("Pms.modules.company.view.CompanyFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.companyFilterForm',

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.bottomToolbar();

        me.callParent();
    },

    buildItems: function () {
        return [
            {
                fieldLabel: l('company.name'),
                xtype: 'combobox',
                store: Ext.create('Pms.modules.company.store.Company', {fromSearchCombo: true}),
                name: 'name',
                displayField: 'name',
                valueField: 'id',
                typeAhead: false,
                anchor: '100%',
                emptyText: l('company.emptyText'),
                querymode: 'remote',
                queryParam: '',
                minChars: 1,
                listConfig: {
                    loadingText: l('searchText'),
                    emptyText: l('company.searchEmptyText'),
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
                fieldLabel: l('country'),
                xtype: 'combobox',
                store: 'Pms.modules.person.store.Country',
                name: 'country',
                displayField: 'country',
                valueField: 'abbr',
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
                fieldLabel: l('discount'),
                xtype: 'combobox',
                store: Ext.create('Ext.data.ArrayStore', {
                    fields: ['label', 'value'],
                    data: [
                        [l('yes'), true],
                        [l('no'), false]
                    ]
                }),
                name: 'discount',
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
            }
        ]
    },

    bottomToolbar: function () {
        return [
            {
                xtype: 'button',
                iconCls: 'app-icon-ok',
                text: l('apply'),
                action: 'apply-filter',
                handler: function (btn, e) {
                    var win = btn.up('companyViewport'),
                        form = win.down('companyFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('companyGrid'),
                        params = {},
                        filter = [],
                        connective = null;

                    if (!Ext.isEmpty(data.name)) {
                        if (isNaN(data.name) || parseInt(data.name) != data.name) {
                            Pms.App.showNotification({message: l('warning.invalidInput')});
                        }
                        else {
                            filter.push({
                                field: 'id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: data.name
                                }
                            });
                        }
                    }

                    if (data.country) filter.push({
                        field: 'country',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: data.country
                        }
                    });

                    if(data.discount === true) {
                        filter.push({
                            field: 'discount',
                            comparison: 'gt',
                            data: {
                                type: 'numeric',
                                value: 0
                            }
                        });
                    }
                    else if(data.discount === false) {
                        filter.push({
                            field: 'discount',
                            comparison: 'eq',
                            data: {
                                type: 'numeric',
                                value: 0
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
                        form = win.down('companyFilterForm').getForm(),
                        grid = win.down('companyGrid');

                    form.reset();
                    grid.resetFilter();
                }
            }
        ]
    }
});