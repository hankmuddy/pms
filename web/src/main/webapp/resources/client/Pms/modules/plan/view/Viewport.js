Ext.define('Pms.modules.plan.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.planViewport',

    autoScroll: false,
//    layout: 'fit',

//    initComponent: function () {
//        var me = this;
//
//        me.tbar = me.buildTopButtons();
//
//        me.items = me.buildItems();
//
//        me.callParent();
//    },

    buildItems: function () {
        return [
            {
                xtype: 'tabpanel',
                layout: 'fit',
                region: 'center',
                items: [
                    {
                        title: l('plan'),
                        layout: 'fit',
                        items: [
                            {
                                layout: 'fit',
                                region: 'center',
                                border: false,
                                items: [
                                    {
                                        xtype: 'planSch',
                                        layout: 'fit'
                                    }
                                ]
                            },
                            {
                                xtype: 'panel',
                                title: l('seasonSearch'),
                                region: 'east',
                                split: false,
                                collapsible: true,
                                collapsed: true,
                                overflowY: 'scroll',
                                width: 500,
                                items: [
                                    {
                                        xtype: 'filterForm'
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('virtualPlans'),
                        layout: 'fit',
                        hidden: !_('channelsEnabled') || Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'panel',
                                layout: 'fit',
//                                region: 'center',
                                items: [
                                    {
                                        xtype: 'virtualPlanGrid',
                                        withToolbar: true,
                                        storeParams: {groupField: 'plan.id'},
                                        features: [
                                            {
                                                ftype: 'grouping',
                                                hideGroupedHeader: true,
                                                startCollapsed: false,
                                                groupHeaderTpl: '<tpl if="name==null">' + l('withoutType') + '</tpl>' +
                                                    '<tpl if="name!=null">' +
                                                    '{[values.rows[0].data.plan.name]}' +
                                                    '</tpl>'
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('restriction'),
                        layout: 'fit',
                        hidden: !_('channelsEnabled') || Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'panel',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'restrictionGrid',
                                        withToolbar: true
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {
    }
});