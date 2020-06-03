Ext.define("Pms.modules.plan.view.PlanFilterForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.planFilterForm',
    requires: ['Pms.abstract.field.lookup.Picker'],

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.bottomToolbar();

        me.callParent();
    },

    buildItems: function () {
        return [
            {
                xtype: 'container',
                title: l('filters'),
                padding: 10,
                defaults: {labelWidth: 130},
                items: [
                    {
                        fieldLabel: l('title'),
                        xtype: 'combobox',
                        store: 'Pms.modules.plan.store.Plan',
                        name: 'name',
                        displayField: 'name',
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
                        fieldLabel: l('roomAndBoard'),
                        xtype: 'lookupCombobox',
                        name: 'boardType',
                        lookupType: 'boardType',
                        valueNotFoundText: null,
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-arrow-trigger',
                        onTrigger1Click: function () {
                            this.clearValue();
                        }
                    }
                ]
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
                    var win = btn.up('planViewport'),
                        form = win.down('planFilterForm').getForm(),
                        data = form.getValues(),
                        grid = win.down('planGrid'),
                        params = {},
                        filter = [],
                        connective = null;

                    if (data.name) filter.push({
                        field: 'id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: data.name
                        }
                    });

                    if (data.board) filter.push({
                        field: 'board',
                        comparison: 'eq',
                        data: {
                            type: 'boardenum',
                            value: data.board
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
                        form = win.down('planFilterForm').getForm(),
                        grid = win.down('planGrid');

                    form.reset();
                    grid.resetFilter();
                }
            }
        ]
    }
});