Ext.define("Pms.modules.plan.view.PlanGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.planGrid',
    store: 'Pms.modules.plan.store.Plan',

    data: {},

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                xtype: 'rownumberer',
                header: '№',
                width: 35,
                sortable: false,
                shrinkWrap: 3,
                renderer: function (value, meta, record) {
                    return record.index + 1;
                }
            },
            {
                header: l('title'),
                dataIndex: 'name'
            },
            {
                header: l('roomAndBoard'),
                dataIndex: 'board'
            },
            {
                header: l('confirmedEntity'),
                xtype: 'booleancolumn',
                dataIndex: 'approved',
                trueText: Pms.iconOk,
                falseText: Pms.iconRemove
            }
        ];

        me.callParent();
    },

    buildActions: function () {
        return {
            header: l('actions.title'),
            xtype: 'actioncolumn',
                sortable: false,
            width: 100,
            items: [{
                    iconCls: 'app-icon-edit2',
                    tooltip: l('tooltip.edit'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('editbuttonclick', grid, rowIndex, colIndex);
                    }
                },{
                    iconCls: 'app-icon-commit',
                    tooltip: l('tooltip.commit'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('commitbuttonclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return rec.data.approved ? true : false;
                    }
                },{
                    iconCls: 'app-icon-remove',
                    tooltip: l('tooltip.delete'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('deletebuttonclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return rec.data.approved ? true : false;
                    }
            }]
        };
    }
});