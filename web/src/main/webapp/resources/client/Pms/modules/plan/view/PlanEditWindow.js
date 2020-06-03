Ext.define('Pms.modules.plan.view.PlanEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.planEditWindow',
    title: l('plan.edit'),
    autoShow: false,
    minimizable: true,
    maximizable: true,
    resizable: false,
    overflowY: 'auto',
    data: {},

    initComponent: function () {
        var me = this;

        if (me.data.approved) {
            me.width = 800;
            me.height = 400;
        }
        me.items = [
            {
                xtype: 'planForm',
                data: me.data
            }
        ];

        me.bbar = ['->', {
            text: l('save.btn'),
            action: me.data.approved ? 'update-living' : 'update',
            iconCls: 'save-action-icon'
        }, {
            text: l('cancel.btn'),
            iconCls: 'app-icon-outgo',
            scope: this,
            handler: this.close
        }];

        me.callParent(arguments);
    }
});