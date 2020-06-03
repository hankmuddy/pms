Ext.define('Pms.modules.bookingButton.view.BookingButtonAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.bookingButtonAddWindow',
    requires: [
        'Ext.form.Panel'
//        'Pms.abstract.field.Money'
    ],

    title: l('catalog.addWindowTitle'),
    resizable: false,
    closeAction: 'destroy',
    width: 500,
    height: 400,

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
                                fieldLabel: l('bb.title'),
                                name: 'name',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('bb.type'),
                                name: 'language',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('bb.price'),
                                name: 'color',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('bb.measure'),
                                name: 'currency',
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('bb.showHeader'),
                                name: 'showHeader',
                                allowBlank: true
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