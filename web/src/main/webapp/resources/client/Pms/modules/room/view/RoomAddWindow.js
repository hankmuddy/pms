Ext.define('Pms.modules.room.view.RoomAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.roomAddWindow',
    title: l('roomInfo'),
    border: false,
    width: 340,
    height: 210,
    resizable: false,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=add-room]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        this.items = [
            {
                xtype: 'form',
                layout: 'fit',
                border: false,
                autoscroll: true,
                fileupload: true,

                items: [
                    {
                        xtype: "roomForm",
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'add-room',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent();
    }

});