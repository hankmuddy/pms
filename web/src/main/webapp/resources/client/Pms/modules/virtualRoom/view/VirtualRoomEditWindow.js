Ext.define('Pms.modules.virtualRoom.view.VirtualRoomEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.virtualRoomEditWindow',
    title: l('virtualRoom.editWinTitle'),
    width: 700,
    height: 500,
    data: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'virtualRoomForm'
            }
        ];
        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-virtualRoom',
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
})
;