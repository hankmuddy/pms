Ext.define("Pms.modules.contact.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.contactViewport',

    initComponent: function () {
        var me = this;
        this.tbar = this.buildTopButtons();
        this.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        return [
            {
                xtype: 'panel',
                region: 'center',
                layout: 'fit',
                border: false,
                items: [
                    {
                        xtype: 'contactGrid'
                    }
                ]
            },
            {
                xtype: 'panel',
                region: 'east',
                split: false,
                width: 400,
                autoScroll: true,
                collapsible: true,
                collapsed: true,
                items: [
                    {
                        xtype: 'contactForm'
                    }
                ]
            }
        ];
    }
});