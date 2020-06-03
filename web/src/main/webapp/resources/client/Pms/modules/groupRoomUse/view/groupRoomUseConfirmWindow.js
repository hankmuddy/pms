Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseConfirmWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseConfirmWindow',

    title: l('booking.approve'),
    autoShow: false,
    width: 400,
    height: 210,
    modal: false,

    initComponent: function () {
        this.items = [
            {
                xtype: 'container',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupRoomUseConfirmForm'
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
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