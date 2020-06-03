Ext.define('Pms.modules.bill.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.billViewport',

    autoScroll: false,

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return [{
                xtype: 'tabpanel',
                region: 'center',
                items: [
                    {
                        title: l('company.billsTab'),
                        layout: 'border',
                        items: [
                            {
                                title: l('filters'),
                                xtype: 'panel',
                                region: 'east',
                                split: false,
                                autoScroll: true,
                                collapsible: true,
                                collapsed: true,
                                width: 350,
                                items: [
                                    {
                                        xtype: 'billFilterForm'
                                    }
                                ]
                            },
                            {
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'billGrid',
                                        withToolbar: true
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('roomUse.refunds'),
                        layout: 'border',
                        items: [
                            {
                                title: l('filters'),
                                xtype: 'panel',
                                region: 'east',
                                split: false,
                                autoScroll: true,
                                collapsible: true,
                                collapsed: true,
                                width: 350,
                                items: [
                                    {
                                        xtype: 'refundFilterForm'
                                    }
                                ]
                            },
                            {
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'refundGrid',
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