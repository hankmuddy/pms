Ext.define('Pms.modules.company.view.CompanyDiscountWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.companyDiscountWindow',
    title: l('setDicscount'),
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
                        fieldLabel: l('company.discount'),
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
                text: l('setDiscount'),
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
        ];

        this.callParent(arguments);
    }
});