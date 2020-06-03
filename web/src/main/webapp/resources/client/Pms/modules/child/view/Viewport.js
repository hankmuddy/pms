Ext.define("Pms.modules.child.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.childViewport',

    initComponent: function () {
        var me = this;
        this.tbar = this.buildTopButtons();
        this.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        return [
            {
                xtype: 'container',
                region: 'center',
                layout: 'fit',
                height: '100%',
                items: [
                    {
                        xtype: 'childGrid'
                    }
                ]
            }
        ];

    }
});