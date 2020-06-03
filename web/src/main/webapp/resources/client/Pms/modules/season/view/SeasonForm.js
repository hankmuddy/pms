Ext.define('Pms.modules.season.view.SeasonForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.seasonForm',

    data: {},
    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'fieldset',
                title: l('period'),
                margin: 5,
                padding: 5,
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        xtype: 'hidden',
                        name: 'plan'
                    },
                    {
                        fieldLabel: l('periodStart') + Pms.requiredStatus,
                        xtype: 'pmsdatefield',
                        name: 'start',
                        labelWidth: 120,
                        width: 220,
                        allowBlank: false,
                        format: 'd/m/Y',
                        submitFormat: 'U',
                        readOnly: me.data.approved
                    },
                    {
                        fieldLabel: l('periodEnd') + Pms.requiredStatus,
                        xtype: 'pmsdatefield',
                        name: 'until',
                        labelWidth: 120,
                        width: 220,
                        allowBlank: false,
                        format: 'd/m/Y',
                        submitFormat: 'U',
                        readOnly: me.data.approved
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
                        loadParams: {
                            params: {
                                filter: [
                                    {
                                        field: 'season.id',
                                        comparison: 'eq',
                                        data: {
                                            type: 'numeric',
                                            value: me.data.id
                                        }
                                    }
                                ]
                            }
                        }
                    }
                ]
            });
        }

        me.callParent(arguments);
    }
});