Ext.define('Pms.modules.bankDetails.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.bankDetailsViewport',

    buildItems: function () {
        var me = this;
        return [
            {
                xtype: 'panel',
                region: 'center',
                layout: 'fit',
                height: '100%',
                border: false,
                items: [
                    {
                        xtype: 'bankDetailsGrid',
                        withToolbar: true,
                        buildToolbar: me.buildGridTopButtons,
                    }
                ]
            }
        ]
    },
    buildTopButtons: function () {
    },

    buildGridTopButtons: function () {
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
                        text: l('delete.btn'),
                        action: 'delete',
                        iconCls: 'pms-delete-icon-16'
                    },
                    {
                        xtype: 'button',
                        flex: 1,
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function(b, e) {
                            b.up('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ];
    }
});
