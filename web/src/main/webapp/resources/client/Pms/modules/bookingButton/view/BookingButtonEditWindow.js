Ext.define('Pms.modules.bookingButton.view.BookingButtonEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.bookingButtonEditWindow',
    requires: ['Pms.abstract.field.Money'],

    title: l('catalog.addWindowTitle'),
    resizable: false,
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
                                xtype: "hidden",
                                name: "id"
                            },
                            {
                                fieldLabel: l('bb.name'),
                                name: 'name',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('bb.language'),
                                name: 'language',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('bb.color'),
                                name: 'color',
                                allowBlank: true
                            },
                            {
                                fieldLabel: l('bb.currency'),
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
