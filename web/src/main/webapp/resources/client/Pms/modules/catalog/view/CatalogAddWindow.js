Ext.define('Pms.modules.catalog.view.CatalogAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.catalogAddWindow',
    requires: [
        'Ext.form.Panel',
        'Pms.abstract.field.Money'
    ],

    title: l('catalog.addWindowTitle'),
    resizable: false,
    closeAction: 'destroy',
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
                                fieldLabel: l('catalog.title') + Pms.requiredStatus,
                                name: 'title',
                                allowBlank: false
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
                text: l('add.btn'),
                action: 'add',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent();
    },

});