Ext.define('Pms.modules.virtualRoom.view.VirtualRoomAddWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.virtualRoomAddWindow',
    title: l('virtualRoom.addWinTitle'),
    width: 400,
    height: 365,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=add-roomType]').fireHandler()
                },
                scope: this
            });
        }
    },

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
                action: 'add-virtualRoom',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});