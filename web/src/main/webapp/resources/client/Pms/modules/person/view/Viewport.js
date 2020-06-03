Ext.define("Pms.modules.person.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.personViewport',

    layout: 'fit',

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
                xtype: 'tabpanel',
                layout: 'fit',
                items: [
                    {
                        title: l('clients'),
                        autoScroll: false,
                        layout: 'border',
                        items: [
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
                                        xtype: 'personFilterForm'
                                    }
                                ]
                            },
                            {
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'personGrid',
                                        withToolbar: true,
                                        buildToolbar: this.personTopButtons
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('children'),
                        autoScroll: false,
                        layout: 'border',
                        tbar: this.childTopButtons(),
                        items: [
                            {
                                xtype: 'panel',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'childGrid'
                                    }
                                ]
                            },
                            {
                                xtype: 'panel',
                                title: l('filters'),
                                region: 'east',
                                split: false,
                                width: 400,
                                autoScroll: true,
                                collapsible: true,
                                collapsed: true,
                                items: [
                                    {
                                        xtype: 'childFilterForm'
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];
    },

    personTopButtons: function () {
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
                        iconCls: 'app-icon-edit2',
                        gridAction: true
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete',
                        disabled: false,
                        iconCls: 'pms-delete-icon-16'
                    },
                    {
                        text: l('person.setDiscount'),
                        action: 'discount',
                        disabled: false,
                        iconCls: 'app-icon-bill'
                    },
                    {
                        text: l('person.sendMail'),
                        action: 'email',
                        disabled: false,
                        iconCls: 'fa fa-envelope-o'
                    },
                    {
                        text: l('person.importContacts'),
                        action: 'import',
                        disabled: false,
                        iconCls: 'fa fa-cloud-download'
                    },
                    {
                        xtype: 'button',
                        iconCls: 'fa fa-file-excel-o',
                        text: l('exportToExcel'),
                        handler: function(b, e) {
                            b.up('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ];
    },

    childTopButtons: function () {
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
                        action: 'new-child',
                        iconCls: 'pms-add-icon-16'
                    },
                    {
                        text: l('edit.btn'),
                        action: 'edit-child',
                        iconCls: 'app-icon-edit2'
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete-child',
                        disabled: false,
                        iconCls: 'pms-delete-icon-16'
                    },
                    {
                        xtype: 'button',
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function(b, e) {
                            b.up('window').down('grid[xtype=childGrid]').downloadExcelXml();
                        }
                    }

                ]
            }
        ];
    },

    buildTopButtons: function () {
    },
});