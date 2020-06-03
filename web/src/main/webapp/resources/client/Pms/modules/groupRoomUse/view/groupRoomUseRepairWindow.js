Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseRepairWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseRepairWindow',

    title: l('roomUse.repair'),
    width: 340,
    modal: false,
    resizable: false,

    initComponent: function () {
        this.items = [
            {
                xtype: 'container',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupRoomUseRepairForm'
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
        ]

        this.callParent(arguments);
    }
});