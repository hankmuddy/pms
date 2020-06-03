Ext.define('Pms.modules.room.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.roomViewport',

    buildItems: function () {
        return [
            {
                xtype: 'panel',
                region: 'center',
                layout: 'fit',
                height: '100%',
                border: false,
                items: [
                    {
                        xtype: 'roomGrid'
                    }
                ]
            }
        ]
    },

    buildTopButtons: function () {
        return [
            {
                xtype: 'toolbar',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left',
                    width: '60'
                },
                items: [
                    {
                        text: l('add.btn'),
                        action: 'new',
                        iconCls: 'pms-add-icon-16'
                    },
                    {
                        text: l('edit.btn'),
                        action: 'edit',
                        iconCls: 'app-icon-edit2'
                    },
                    {
                        text: l('commit.btn'),
                        action: 'commit',
                        disabled: false,
                        iconCls: 'pms-success-icon-16'
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete',
                        disabled: false,
                        iconCls: 'pms-delete-icon-16'
                    }
                ]
            }
        ];
    }
});
