Ext.define("Pms.modules.company.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.companyViewport',
    border: false,
    autoScroll: false,

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
                title: l('filters'),
                region: 'east',
                split: false,
                autoScroll: true,
                collapsible: true,
                collapsed: true,
                width: 400,
                items: [
                    {
                        xtype: 'companyFilterForm'
                    }
                ]
            },
            {
                region: 'center',
                layout: 'fit',
                items: [
                    {
                        xtype: 'companyGrid'
                    }
                ]
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
                        text: l('delete.btn'),
                        action: 'delete',
                        disabled: false,
                        iconCls: 'pms-delete-icon-16'
                    },
                    {
                        text: l('setDiscount.btn'),
                        action: 'discount',
                        disabled: false,
                        iconCls: 'app-icon-bill'
                    },
                    {
                        xtype: 'button',
                        flex: 1,
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function(b, e) {
                            b.up('window').down('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ];
    }
});