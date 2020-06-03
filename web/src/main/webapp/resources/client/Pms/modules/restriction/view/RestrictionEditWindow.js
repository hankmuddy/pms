Ext.define('Pms.modules.restriction.view.RestrictionEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.restrictionEditWindow',
    title: l('restriction.editWinTittle'),
    width: 400,
    height: 280,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'restrictionForm'
            }
        ];
        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-restriction',
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