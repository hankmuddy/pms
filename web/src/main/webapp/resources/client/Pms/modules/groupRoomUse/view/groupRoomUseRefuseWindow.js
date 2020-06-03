Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseRefuseWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseRefuseWindow',

    title: l('refuse'),
    width: 340,
    height: 200,
    modal: false,

    data: {},

    initComponent: function () {
        var me = this;

        if (typeof this.data.customerGroup != 'undefined') {
            this.height = this.data.customerGroup.roomUsesQuantity == 1 ? 125 : 165;
        } else {
            this.height = 165;
        }

        console.log(this.data);

        me.items = [
            {
                xtype: 'container',
                layout: 'fit',
                items: [
                    {
                        xtype: 'groupRoomUseRefuseForm',
                        data: me.data
                    }
                ]
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