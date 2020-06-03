Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseOutgoWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseOutgoWindow',

    title: l('departure'),
    width: 400,
    height: 235,
    data: {},

    initComponent: function () {
        this.height = this.data.customerGroup.roomUsesQuantity == 1 ? 220 : 240;
        this.items = [
            {
                xtype: 'container',
                items: [
                    {
                        xtype: 'propertygrid',
                        hideHeaders: true,
                        editable: false,
                        source: {
                            room: this.data.room.number,
                            roomType: this.data.room.roomType.name,
                            master: !Ext.isEmpty(this.data.customerGroup.company) ? this.data.customerGroup.company.name : this.data.customerGroup.customer.lastName + ' ' + this.data.customerGroup.customer.firstName,
                            startDate: Ext.Date.format(this.data.startDate, 'd/m/y'),
                            endDate: Ext.Date.format(this.data.endDate, 'd/m/y')
                        },
                        sourceConfig: {
                            room: {displayName: l('room')},
                            roomType: {displayName: l('roomType')},
                            master: {displayName: l('mainOfGroup')},
                            startDate: {displayName: l('from')},
                            endDate: {displayName: l('to')}
                        },
                        listeners: {
                            beforeedit: function () {
                                return false;
                            }
                        }
                    },
                    {
                        xtype: 'groupRoomUseOutgoForm',
                        isGroup: this.data.customerGroup.roomUsesQuantity == 1
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('outgo'),
                action: 'save',
                requestDisable: true,
                iconCls: 'fa fa-mail-forward'
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close,
                iconCls: 'fa fa-times'
            }
        ]

        this.callParent(arguments);
    }
});