Ext.define('Pms.modules.virtualPlan.view.VirtualPlanEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.virtualPlanEditWindow',
    title: l('virtualPlan.editWinTitle'),
    width: 300,
    height: 210,
    data: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'virtualPlanForm'
            }
        ];
        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save-virtualPlan',
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