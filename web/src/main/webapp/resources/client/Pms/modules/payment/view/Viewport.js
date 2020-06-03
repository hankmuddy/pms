Ext.define("Pms.modules.payment.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.paymentViewport',

    autoScroll: false,

    initComponent: function () {
        var me = this;

        this.tbar = this.buildTopButtons();

        this.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return [
            {
                xtype: 'panel',
                title: l('filters'),
                region: 'east',
                split: false,
                autoScroll: true,
                collapsible: true,
                collapsed: true,
                width: 400,
                items: [
                    {
                        xtype: 'paymentFilterForm'
                    }
                ]
            },
            {
                // xtype: 'panel',
                region: 'center',
                layout: 'fit',
                // height: '65%',
                items: [
                    {
                        xtype: 'paymentGrid',
                        withToolbar: true,
//                        periodSelection: true
                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {
    }
});