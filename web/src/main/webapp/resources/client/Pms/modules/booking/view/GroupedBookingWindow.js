Ext.define('Pms.modules.booking.view.GroupedBookingWindow', {
    extend: 'Pms.abstract.Window',
    requires: [
        'Ext.form.Panel',
        'Pms.abstract.field.Money'
    ],
    alias: 'widget.groupedBookingWindow',
    width: 1100,
    height: 580,
    overflowY: true,
    overflowX: false,
    closeAction: 'destroy',
    customerGroupId: null,

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'bookingGrid',
                overflowY: true,
                overflowX: false,
                loadParams: {
                    params: {
                        filter: [
                            {
                                field: 'customerGroup.id',
                                comparison: 'eq',
                                data: {
                                    type: 'numeric',
                                    value: this.customerGroupId
                                }
                            }
                        ]
                    }
                },
            },
        ];

        this.buttons = [
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});