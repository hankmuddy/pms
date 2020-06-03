Ext.define('Pms.modules.serviceUse.view.serviceUseAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.serviceUseAddWindow',
    title: l('serviceOrder'),
    width: 330,
    resizable: false,
    height: 175,
    measure: '',
    sinceDate: null,
    toDate: null,
    price: '',
    customerGroup: null,
    serviceUseTitle: null,
    service: null,
    serviceType: null,
    roomUse: null,
    layout: null,

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'propertygrid',
                hideHeaders: true,
                editable: false,
                source: {
                    title: l(me.serviceUseTitle),
                    price: me.price
                },
                sourceConfig: {
                    title: {
                        displayName: l('serviceUse.name')
                    },
                    price: {
                        displayName: l('serviceUse.price') + '&nbsp;' + me.measure
                    }
                },
                listeners: {
                    beforeEdit: function () {
                        return false
                    }
                }
            },
            {
                xtype: 'serviceUseForm',
                measure: me.measure,
                sinceDate: me.sinceDate,
                toDate: me.toDate,
                customerGroup: me.customerGroup,
                service: me.service,
                serviceType: me.serviceType,
                roomUse: me.roomUse,
                anchor: '100%'
            }
        ];

        me.buttons = [
            {
                text: l('order'),
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