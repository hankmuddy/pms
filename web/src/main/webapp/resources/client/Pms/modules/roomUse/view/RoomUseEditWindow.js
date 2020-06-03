Ext.define('Pms.modules.group.view.GroupEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupEditWindow',
    title: l('editing'),
    width: 800,
    height: 450,

    initComponent: function () {

        this.items = [
            {
                xtype: 'tabpanel',
                items: [
                    {
                        title: l('companyInfo'),
                        items: [
                            {
                                xtype: 'groupForm',
                                height: 450
                            }
                        ]
                    },
                    {
                        title: l('contacts'),
                        autoScroll: true,
                        items: [
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
                                        action: 'new-contact',
                                        iconCls: 'pms-add-icon-16',
                                        width: '60'
                                    },
                                    {
                                        text: l('delete.btn'),
                                        action: 'delete-contact',
                                        disabled: false,
                                        iconCls: 'pms-delete-icon-16',
                                        width: '60'
                                    }
                                ]
                            },
                            {
                                xtype: 'contactGrid'
                            }
                        ]
                    },
                    {
                        title: l('reservation'),
                        autoScroll: true,
                        html: l('reservation')
                    },
                    {
                        title: l('living'),
                        autoScroll: true,
                        html: l('living')
                    },
                    {
                        title: l('bills'),
                        autoScroll: true,
                        html: l('bills')
                    }
                ]
            }
        ];

        this.callParent(arguments);
    }
});