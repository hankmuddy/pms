Ext.define('Pms.modules.plan.view.PlanForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.planForm',

    data: {},

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'fieldset',
                margin: 5,
                padding: 5,
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        xtype: me.data.approved ? 'displayfield' : 'textfield',
                        fieldLabel: l('name') + Pms.requiredStatus,
                        name: 'name',
                        allowBlank: false,
                    },
                    {
                        fieldLabel: l('roomAndBoard') + Pms.requiredStatus,
                        xtype: 'lookupCombobox',
                        name: 'board',
                        lookupType: 'boardType',
                        valueNotFoundText: null,
                        allowBlank: false
//                        readOnly: me.data.approved
                    }
                ]
            }
        ];

        if (me.data.approved) {
            me.items.push({
                xtype: 'fieldset',
                title: l('startMenu.prices'),
                margin: 5,
                padding: 5,
                items: [
                    {
                        xtype: 'livingGrid',
                        cellEditing: true,
                        paging: false,
                        overflowX: false,
                        overflowY: true,
                        loadParams: {
                            params: {
                                filter: [
                                    {
                                        field: 'plan.id',
                                        comparison: 'eq',
                                        data: {
                                            type: 'numeric',
                                            value: me.data.id
                                        }
                                    }
                                ]
                            }
                        },
                        data: me.data
                    }
                ]
            });
        }

        me.callParent(arguments);
    }
});