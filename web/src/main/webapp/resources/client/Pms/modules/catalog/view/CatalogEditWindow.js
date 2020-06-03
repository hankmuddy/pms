Ext.define('Pms.modules.catalog.view.CatalogEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.catalogEditWindow',
    requires: ['Pms.abstract.field.Money'],

    title: l('catalog.addWindowTitle'),
    resizable: false,
    width: 350,
    height: 200,

    initComponent: function () {
        this.items = [
            {
                xtype: 'form',
                layout: 'fit',
                border: false,
                autoscroll: true,
                fileupload: false,

                items: [
                    {
                        xtype: "fieldset",
                        padding: 10,
                        defaults: {
                            xtype: "textfield",
                            anchor: '100%'
                        },
                        items: [
                            {
                                xtype: "hidden",
                                name: "id"
                            },
                            {
                                fieldLabel: l('catalog.title'),
                                name: 'title',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('catalog.type'),
                                name: 'type',
                                xtype: 'hidden',
                                value: 'simpleService'
                            },
                            {
                                fieldLabel: l('catalog.price'),
                                xtype: 'moneyfield',
                                name: 'price',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('catalog.measure'),
                                name: 'measure',
                                allowBlank: false,
                                value: l('catalog.pieces')
                            }
                        ]
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'update',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);
    }
});
