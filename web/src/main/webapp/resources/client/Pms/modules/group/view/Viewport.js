Ext.define("Pms.modules.group.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.groupViewport',

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
                height: '100%',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupGrid'
                    }
                ]
            }
        ];
    }
});