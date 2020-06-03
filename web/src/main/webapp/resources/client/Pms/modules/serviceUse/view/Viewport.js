Ext.define("Pms.modules.serviceUse.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.serviceUseViewport',

    initComponent: function () {
        var me = this;

        this.tbar = this.buildTopButtons();

        this.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
//        return [
//            {
//                xtype: 'container',
//                region: 'center',
//                autoScroll: true,
//                layout: 'fit',
//                items: [
//                    {
//                        xtype: 'serviceUseGroupGrid'
//                    }
//                ]
//            }
//        ];
    }
});