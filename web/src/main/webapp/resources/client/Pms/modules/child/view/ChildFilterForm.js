Ext.define("Pms.modules.child.view.ChildFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.childFilterForm',

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.bottomToolbar();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return    {
            xtype: 'container',
            title: l('filters'),
            padding: 10,
            defaults: {labelWidth: 130},
            items: [
                {
                    fieldLabel: l('lastName'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.child.store.Child', {fromSearchCombo: true}),
                    name: 'lastName',
                    displayField: 'lastName',
                    valueField: 'id',
                    typeAhead: false,
                    hideTrigger: true,
                    anchor: '100%',
                    emptyText: l('lastName'),
                    querymode: 'remote',
                    queryParam: '',
                    minChars: 1,
                    listConfig: {
                        loadingText: l('searchText'),
                        emptyText: l('error.noMatches'),
                        getInnerTpl: function () {
                            return '<div class="search-item">{lastName} {firstName}</b></div>';
                        }
                    },
                    listeners: {
                        change: function (combo, newValue, oldValue, eOpts) {
                            if (newValue && newValue.length > 0) {
                                combo.store.filterVal = newValue;
                            }
                        }
                    }
                },
                {
                    fieldLabel: l('dateOfBirth'),
                    xtype: 'pmsdatefield',
                    name: 'dob',
                    format: 'd/m/y',
                    submitFormat: 'U',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-date-trigger',
                    onTrigger1Click: function () {
                        this.setValue('');
                    }
                },
                {
                    fieldLabel: l('yearOfBirth'),
                    xtype: 'combobox',
                    store: Ext.create('Ext.data.ArrayStore', {
                        fields: ['year', 'yearTimestamp'],
                        data: me.getYears()
                    }),
                    name: 'yob',
                    displayField: 'year',
                    valueField: 'yearTimestamp',
                    triggerAction: 'all',
                    queryMode: 'local',
                    queryParam: '',
                    selectOnFocus: true,
                    indent: true,
                    anchor: '100%',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-date-trigger',
                    onTrigger1Click: function () {
                        this.clearValue();
                    }
                }
            ]
        };
    },

    getYears: function () {
        var currentTime = new Date();
        var now = currentTime.getFullYear();
        var years = [];
        var y = now;
        while (y >= 1970) {
            years.push([y, Ext.Date.format(new Date(y, 0, 1), 'U')]);
            y--;
        }
        return years;
    },

    bottomToolbar: function () {
        return  [
            {
                xtype: 'button',
                iconCls: 'app-icon-ok',
                text: l('apply'),
                action: 'apply-filter',
                handler: function (btn, e) {
                    var win = btn.up('personViewport'),
                        form = win.down('childFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('childGrid'),
                        params = {},
                        filter = [],
                        connective = null;

                    if (!Ext.isEmpty(data.lastName)) {
                        if (isNaN(data.lastName) || parseInt(data.lastName) != data.lastName) {
                            Pms.App.showNotification({message: l('warning.invalidInput')});
                        }
                        else {
                            filter.push({
                                field: 'id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: data.lastName
                                }
                            });
                        }
                    }

                    if (data.dob) filter.push({
                        field: 'dob',
                        comparison: 'eq',
                        data: {
                            type: 'date',
                            value: Pms.toUTC(data.dob)
                        }
                    });

                    if (data.yob) {
                        filter.push({
                            field: 'dob',
                            comparison: 'gte',
                            data: {
                                type: 'date',
                                value: Pms.toUTC(data.yob)
                            }
                        });
                        filter.push({
                            field: 'dob',
                            comparison: 'lt',
                            data: {
                                type: 'date',
                                value: Pms.toUTC(Ext.Date.format(Ext.Date.add(new Date(data.yob * 1000), Ext.Date.YEAR, 1), 'U'))
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
                        form = win.down('childFilterForm').getForm(),
                        grid = win.down('childGrid');

                    form.reset();
                    grid.resetFilter();
                }
            }
        ]
    }
});