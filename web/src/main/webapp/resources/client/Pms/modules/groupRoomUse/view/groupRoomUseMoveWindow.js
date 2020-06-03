Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseMoveWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseMoveWindow',

    title: l('move'),
    width: 400,
    height: 250,
    modal: false,
    data: null,
    initComponent: function () {
        this.items = [
            {
                xtype: 'container',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupRoomUseMoveForm',
                        data: this.data
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