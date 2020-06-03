Ext.define('Pms.modules.room.view.RoomEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.roomEditWindow',

    title: l('roomInfo'),
    width: 340,
    height: 220,
    resizable: false,
    approved: false,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=save-room]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'form',
                layout: 'fit',
                border: false,
                autoscroll: true,
                fileupload: true,

                items: [
                    {
                        xtype: me.approved ? 'roomApprovedForm' : 'roomForm'
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-room',
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