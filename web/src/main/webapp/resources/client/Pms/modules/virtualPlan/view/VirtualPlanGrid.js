Ext.define('Pms.modules.virtualPlan.view.VirtualPlanGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.virtualPlanGrid',
    requires: ['Pms.abstract.field.MoneyColumn', 'Pms.abstract.field.Money'],
    store: 'Pms.modules.virtualPlan.store.VirtualPlan',

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('plan'),
                dataIndex: 'plan',
                flex: 2,
                renderer: function (val, row) {
                    return val.name + ' (' + val.board + ')';
                }
            },
            {
                header: l('virtualPlan.name'),
                dataIndex: 'name',
                flex: 1
            },
            {
                header: l('virtualPlan.variation'),
                dataIndex: 'variation',
                renderer: function (val, row) {
                    return l('variation.' + val)
                },
                flex: 1
            },
            {
                header: l('virtualPlan.value'),
                dataIndex: 'value',
                renderer: function (val) {
                    return val / 100
                },
                flex: 1
            },
            {
                header: l('virtualRoom.approved'),
                xtype: 'booleancolumn',
                dataIndex: 'approved',
                flex: 1,
                trueText: Pms.iconAccept,
                falseText: Pms.iconNotAccept
            }
        ];

        me.callParent(arguments);
    }
});