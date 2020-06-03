Ext.define('Pms.modules.plan.view.PlanView', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.planView',
    title: l('plan'),
    width: 300,
    height: 300,
    data: {},

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'propertygrid',
//            title: 'Тарифный план',
                hideHeaders: true,
                editable: false,
                source: {
                    name: me.data.name,
                    board: me.data.board,
                    approved: me.data.approved
                },
                sourceConfig: {
                    name: {displayName: l('title')},
                    board: {displayName: l('roomAndBoard')},
                    approved: {displayName: l('confirmedEntity'), renderer: function (v) {
                        return v ? Pms.iconOk : Pms.iconRemove;
                    }}
                },
                listeners: {
                    beforeedit: function () {
                        return false
                    }
                }
            }
        ];

        me.buttons = [
            {
                text: l('close.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent(arguments);
    }
});