Ext.define("Pms.abstract.Viewport", {
    extend: "Ext.panel.Panel",
    layout: "border",
    height: 400,
    autoScroll: true,

    initComponent: function () {
        this.tbar = this.buildTopButtons();
        this.items = this.buildItems();
        this.callParent();
    },

    buildItems: function () {
        return [
            {
                region: "center",
                border: false
            }
        ];
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
                        text: l('actions.title'),
                        iconCls: 'app-icon-actions',
                        menu: [
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
                ]
            }
        ];
    }
});