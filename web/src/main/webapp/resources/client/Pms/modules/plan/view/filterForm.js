Ext.define('Pms.modules.plan.view.filterForm', {
    extend: "Pms.abstract.Form",
    alias: 'widget.filterForm',

    book: false,
    initComponent: function () {
        var me = this;

        me.items = me.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return    [
            {
                xtype: 'container',
                title: l('seasonSearch'),
                padding: 10,
                defaults: {labelWidth: 130},
                bbar: this.bottomToolbar(),
                items: [
                    {
                        fieldLabel: l('plan'),
                        name: 'plan',
                        xtype: 'combobox',
                        store: Ext.create('Pms.modules.plan.store.Plan').load({
                            params: {
                                filter: [
                                    {
                                        field: 'approved',
                                        comparison: 'eq',
                                        data: {
                                            type: 'boolean',
                                            value: true
                                        }
                                    }
                                ]
                            }
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        queryMode: 'local',
                        allowBlank: false,
                        editable: false
                    },
                    {
                        fieldLabel: l('season.begin'),
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
                        fieldLabel: l('season.end'),
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
            },
            {
                xtype: 'toolbar',
                margin: '5 0',
                items: me.bottomToolbar()
            },
            {
                xtype: 'seasonGrid',
                loadParams: {
                    params: {
                        filter: [
                            {
                                field: 'approved',
                                comparison: 'eq',
                                data: {
                                    type: 'boolean',
                                    value: true
                                }
                            }
                        ]
                    }
                }
            }
        ];
    },

    bottomToolbar: function () {
        return  [
            {
                xtype: 'button',
                iconCls: 'app-icon-ok',
                text: l('apply'),
                action: 'apply-filter',
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        form = win.down('filterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('seasonGrid'),
                        store = grid.getStore(),
                        filter = [];

                    filter.push({
                        field: 'approved',
                        comparison: 'eq',
                        data: {
                            type: 'boolean',
                            value: true
                        }
                    });

                    if (data.plan) {
                        filter.push({
                            field: 'plan',
                            comparison: 'eq',
                            data: {
                                type: 'numeric',
                                value: data.plan
                            }
                        });
                    }

                    if (data.startDate) {
                        data.startDate = Pms.toUTC(data.startDate);
                        filter.push({
                            field: 'start',
                            comparison: 'gte',
                            data: {
                                type: 'date',
                                value: data.startDate
                            }
                        })
                    } else {
                        data.startDate = Ext.Date.format(Ext.Date.clearTime(new Date()), 'U');
                    }

                    if (data.endDate) {
                        data.endDate = Pms.toUTC(data.endDate);
                        filter.push({
                            field: 'until',
                            comparison: 'lte',
                            data: {
                                type: 'date',
                                value: data.endDate
                            }
                        });
                    }

                    store.load({
                        params: {
                            filter: filter
                        },
                        success: function () {
                        },
                        failure: function () {
                            Ext.Msg.alert(l('error'), l('searchingError'));
                        }
                    });
                }
            },
            {
                iconCls: 'app-icon-refund',
                text: l('resetFilter'),
                handler: function (btn, e) {
                    var win = btn.up('window'),
                        form = win.down('filterForm').getForm(),
                        grid = win.down('seasonGrid');
                    form.reset();
                    grid.getStore().removeAll();
                }
            }
        ]
    }
})
;
