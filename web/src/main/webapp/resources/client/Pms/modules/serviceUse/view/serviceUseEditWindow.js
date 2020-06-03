Ext.define('Pms.modules.serviceUse.view.serviceUseEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.serviceUseEditWindow',
    title: l('serviseUse.editor'),
    width: 320,
    height: 280,
    serviceUse: null,

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'propertygrid',
                hideHeaders: true,
                editable: false,
                source: {
                    name: me.serviceUse.service.name,
                    date: Ext.Date.format(me.serviceUse.date, 'd/m/y'),
                    quantity: me.serviceUse.quantity + ' ' + me.serviceUse.service.measure,
                    price: me.serviceUse.service.price
                },
                sourceConfig: {
                    name: {displayName: l('service')},
                    date: {displayName: l('date')},
                    quantity: {displayName: l('serviceUse.booked') + me.serviceUse.quantity + ' ' + me.serviceUse.service.measure},
                    price: {displayName: l('serviceUse.price') + me.serviceUse.service.measure}
                },
                listeners: {
                    beforeEdit: function () {
                        return false
                    }
                }
            },
            {
                xtype: 'serviceUseForm',
                serviceUse: me.serviceUse,
                anchor: '100%'
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});