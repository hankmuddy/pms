Ext.define('Pms.modules.user.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.userViewport',

    autoScroll: false,

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this;
        return [

            {
                xtype: 'tabpanel',
                region: 'center',
                items: [
                    {
                        title: l('user.userTab'),
                        layout: 'border',
                        items: [
                            {
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'userGrid',
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