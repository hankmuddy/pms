Ext.define('Pms.modules.periodInfo.view.PeriodInfoEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.periodInfoEditWindow',
    title: l('periodInfo.editWinTitle'),
    width: 700,
    height: 500,
    data: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'periodInfoForm'
            }
        ];
        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-periodInfo',
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