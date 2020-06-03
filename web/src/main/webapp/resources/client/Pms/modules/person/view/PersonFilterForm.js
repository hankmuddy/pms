Ext.define("Pms.modules.person.view.PersonFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.personFilterForm',

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.bottomToolbar();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return {
            // xtype: 'fieldset',
            xtype: 'container',
            title: l('filters'),
            padding: 10,
            defaults: {labelWidth: 130},
            items: [
                {
                    fieldLabel: l('lastName'),
                    xtype: 'combobox',
                    store: Ext.create('Pms.modules.person.store.Person', {fromSearchCombo: true}),
                    name: 'lastName',
                    displayField: 'lastName',
                    valueField: 'id',
                    typeAhead: false,
                    // hideLabel: true,
                    // hideTrigger:true,
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
                                '<tpl if="dob">' +
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
                    fieldLabel: l('dateOfBirth'),
//                    xtype: 'combobox',
//                    store: Ext.create('Ext.data.ArrayStore', {
//                        fields: ['year', 'yearTimestamp'],
//                        data: me.getYears()
//                    }),
                    xtype: 'datefield',
                    name: 'dob',
//                    displayField: 'year',
//                    valueField: 'yearTimestamp',
                    triggerAction: 'all',
//                    queryMode: 'local',
//                    queryParam: '',
                    selectOnFocus: true,
                    format: 'd/m/Y',
                    indent: true,
                    anchor: '100%',
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-date-trigger',
                    onTrigger1Click: function () {
                        this.clearValue();
                    }
                },
                {
                    fieldLabel: l('language'),
                    xtype: 'combobox',
                    store: 'Pms.modules.person.store.Language',
                    name: 'language',
                    displayField: 'language',
                    valueField: 'code',
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
                    fieldLabel: l('city'),
                    xtype: 'textfield',
                    name: 'city'
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
                        form = win.down('personFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('personGrid'),
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

                    if (data.dob) {
                        filter.push({
                            field: 'dob',
                            comparison: 'eq',
                            data: {
                                type: 'date',
                                value: Pms.toUTC(Ext.Date.parse(data.dob, "d/m/Y"))
                            }
                        });
//                        filter.push({
//                            field: 'dob',
//                            comparison: 'lt',
//                            data: {
//                                type: 'date',
//                                value: Pms.toUTC(Ext.Date.format(Ext.Date.add(new Date(data.dob * 1000), Ext.Date.YEAR, 1), 'U'))
//                            }
//                        });
                    }

                    if (data.language) filter.push({
                        field: 'language',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: data.language
                        }
                    });

                    if (data.country) filter.push({
                        field: 'country',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: data.country
                        }
                    });

                    if (data.city) filter.push({
                        field: 'city',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: data.city
                        }
                    });

                    grid.applyFilter(params, filter, connective);
                }
            },
            {
                iconCls: 'app-icon-refund',
                text: l('resetFilter'),
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        form = win.down('personFilterForm').getForm(),
                        grid = win.down('personGrid');

                    form.reset();
                    grid.resetFilter();
                }
            }
        ]
    }
});