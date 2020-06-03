Ext.define('Pms.modules.groupRoomUse.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.groupRoomUseViewport',

    autoScroll: false,

    initComponent: function () {
        var me = this;

        me.tbar = me.buildTopButtons();

        me.items = me.buildItems();

        me.callParent();
    },

    buildItems: function () {
        return [
            {
                layout: 'fit',
                region: 'center',
                border: false,
                items: [
                    {
                        xtype: 'schPanel',
                        layout: 'fit'
                    }
                ]
            },
            {
                xtype: 'panel',
                title: l('freeRoomSearch'),
                region: 'east',
                split: false,
                collapsible: true,
                collapsed: true,
                overflowY: 'scroll',
                width: 500,
                items: [
                    {
                        xtype: 'roomUseFilterForm'
                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {

    }
});