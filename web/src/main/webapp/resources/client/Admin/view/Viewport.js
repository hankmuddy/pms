Ext.define('Admin.view.Viewport', {
    extend: 'Ext.container.Viewport',
    hidden: 'true',
    layout: 'fit',
    initComponent: function () {
        this.items = {
            xtype: 'panel',
            layout: 'border',
            margin: 5,
            items: {
                itemId: 'contentwrapper',
                xtype: 'panel',
                layout: 'card',
                region: 'center',
                items: [
                    {
                        xtype: 'panel',
                        layout: 'fit',
                        itemId: 'error'
                    },
                    {
                        xtype: 'panel',
                        layout: 'fit',
                        itemId: 'home',
                        items: Ext.create('Admin.' + Admin.getApplication().appName + '.view.NavTabPanel')
                    },
                    {
                        xtype: 'panel',
                        layout: 'fit',
                        itemId: 'settings',
                        items: Ext.create('Admin.settings.view.NavTabPanel')
                    }
                ]
            }
        };
        this.callParent();
    }
});
