Ext.define("Pms.modules.group.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.groupViewport',

    initComponent: function () {
        var me = this;

        me.tbar = this.buildTopButtons();

        me.items = this.buildItems();

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
                        text: l('add.btn'),
                        action: 'new-group',
                        iconCls: 'pms-add-icon-16',
                        width: '60'
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete-group',
                        disabled: false,
                        iconCls: 'pms-delete-icon-16',
                        width: '60'
                    }
                ]
            }
        ];
    }
});