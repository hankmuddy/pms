Ext.define('Pms.modules.quota.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.quotaViewport',

    autoScroll: false,
    layout: 'fit',

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
        return [{
            xtype: 'tabpanel',
            layout: 'fit',
            items: [{
                    title: l('quota'),
                    layout: 'fit',
                    items: [{
                        xtype: 'rtvSch',
                        layout: 'fit'
                    }]
                },{
                    title: l('quota.Restriction'),
                    layout: 'fit',
                    items: [{
                        xtype: 'brvSch',
                        layout: 'fit'
                    }]
            }]
        }];
    },


    buildTopButtons: function () {
    }
});