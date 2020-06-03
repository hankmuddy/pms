Ext.define("Pms.modules.otaCalendar.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.otaCalendarViewport',

    initComponent: function () {
        var me = this;
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
                        xtype: 'otaCalendarSch'
                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {
        return [
            {
                xtype: 'buttongroup',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left',
                    width: '60'
                },
                items: [
                    {
                        text: l('ota.setPrices'),
                        action: 'new',
                        iconCls: 'pms-add-icon-16',
                        width: '60'
                    }
                ]
            }
        ];
    }
});