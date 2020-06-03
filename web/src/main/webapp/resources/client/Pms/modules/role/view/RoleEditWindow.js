Ext.define('Pms.modules.role.view.RoleEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.roleEditWindow',
    title: l('role.editWinTitle'),
    width: 380,
    height: 440,
    data: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'roleForm'
            }
        ];
        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-role',
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