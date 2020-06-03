Ext.define('Pms.modules.user.view.UserEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.userEditWindow',
    title: l('user.editWinTittle'),
    width: 340,
    height: 400,
    data: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'userForm',
                editForm: true
            }
        ];
        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-user',
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