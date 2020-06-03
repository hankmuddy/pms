Ext.define('Pms.modules.person.view.PersonDiscountWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.personDiscountWindow',
    title: l('person.setDiscount'),
    width: 290,
    height: 110,

    initComponent: function () {
        this.items = [
            {
                xtype: 'form',
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        fieldLabel: l('discount.valueInPercent'),
                        xtype: 'numberfield',
                        name: 'discount',
                        minValue: 0,
                        maxValue: 100,
                        padding: 10
                    }
                ]
            }
        ];
        this.buttons = [
            {
                text: l('person.setDiscount'),
                iconCls: 'fa fa-reply',
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                iconCls: 'fa fa-times',
                scope: this,
                handler: this.close
            }
        ]

        this.callParent(arguments);
    }
});