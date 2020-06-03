Ext.define('Pms.modules.living.view.LivingGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.livingGrid',
    requires: ['Pms.abstract.field.MoneyColumn', 'Pms.abstract.field.Money'],
    store: 'Pms.modules.living.store.Living',

    cellEditing: true,
    overflowY: 'auto',

    data: {},

    initComponent: function () {
        var me = this;

        me.columns = [{
                header: l('living.gridBaseRoom'),
                dataIndex: 'room',
                flex: 2,
                editing: false,
                renderer: function (val, row) {
                    return val.name + ' (' + val.adults + '/' + val.children + '/' + val.additional + ')';
                }
            },{
                header: l('plan'),
                dataIndex: 'plan',
                flex: 2,
                editing: false,
                renderer: function (val, row) {
                    if (!val) return row.record.data.season.plan.name;
                    else return val.name;
                }
            },{
                header: l('living.gridBoard'),
                dataIndex: 'plan',
                width: 55,
                editing: false,
                renderer: function (val, row) {
                    if (!val) return row.record.data.season.plan.board;
                    else return val.board;
                }
            },{
                header: l('season'),
                dataIndex: 'season',
                width: 130,
                editing: false,
                renderer: function (val, row) {
                    if (!val) return '&mdash;';
                    else return Ext.Date.format(new Date(val.start * 1000), 'd/m/Y') + ' - ' + Ext.Date.format(new Date(val.until * 1000), 'd/m/Y');
                }
            },{
            header: l('week.mon'),
                dataIndex: 'mon',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
            },{
            header: l('week.tue'),
                dataIndex: 'tue',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
            },{
            header: l('week.wed'),
                dataIndex: 'wed',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
            },{
            header: l('week.thu'),
                dataIndex: 'thu',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
            },{
            header: l('week.fri'),
                dataIndex: 'fri',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
            },{
            header: l('week.sat'),
                dataIndex: 'sat',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
            },{
            header: l('week.sun'),
                dataIndex: 'sun',
                width: 50,
                editing: true,
                editor: Pms.moneyEditor
        }];

        me.callParent(arguments);
    },

    listeners: {
        cellcontextmenu: function(grid, td, cellIndex, rec, tr, rowIndex, e, eOpts) {
            this.onEventContextMenu(grid, td, cellIndex, rec, tr, rowIndex, e, eOpts);
        }
    },

    onEventContextMenu: function (grid, td, cellIndex, rec, tr, rowIndex, e, eOpts) {
        e.stopEvent();

        var me = this,
            cellDays = {4: 'mon', 5: 'tue', 6: 'wed', 7: 'thu', 8: 'fri', 9: 'sat', 10: 'sun'},
            field = cellDays[cellIndex],
            val = rec.data[field] * 100;

        if(cellIndex >= 4 && cellIndex <= 10) {
            grid.ctx = new Ext.menu.Menu({
                items: [{
                    text: l('tooltip.applyToAll'),
                    iconCls: 'app-icon-commit',
                    handler: function (btn, e) {
                        rec.set({mon: val, tue: val, wed: val, thu: val, fri: val, sat: val, sun: val});
                    }
                }],
                listeners: {
                    hide: function () {
                        grid.ctx.destroy();
                    }
                }
            });
            grid.ctx.rec = rec;
            grid.ctx.showAt(e.getXY());
        }
    }
});