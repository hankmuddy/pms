Ext.define('Pms.modules.user.view.UserAddWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.userAddWindow',
    title: l('user.addWinTittle'),
    width: 340,
    height: 440,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=add-user]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'userForm'
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'add-user',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent();
    }
});