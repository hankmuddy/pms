Ext.define('Pms.modules.accommodation.view.AccommodationAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.accommodationAddWindow',
    width: 300,
    height: 170,
    border: 0,
    title: l('accommodation.add'),

    initComponent: function () {
        var me = this;

        me.items = {
            xtype: 'panel',
            region: 'center',
            items: [
                {
                    xtype: 'accommodationForm'
                }
            ]
        };

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                action: 'close',
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});