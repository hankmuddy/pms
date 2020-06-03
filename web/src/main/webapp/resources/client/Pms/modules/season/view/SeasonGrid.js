Ext.define('Pms.modules.season.view.SeasonGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.seasonGrid',
    store: 'Pms.modules.season.store.Season',
    autoLoading: false,
    paging: false,
    border: true,
    book: false,
    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('season.begin'),
                width: 50,
                dataIndex: 'start',
                sortable: false,
                renderer: function (v) {
                    return Ext.Date.format(v, 'd/m/Y');
                }
            },
            {
                header: l('season.end'),
                width: 150,
                dataIndex: 'until',
                sortable: false,
                renderer: function (v) {
                    return Ext.Date.format(v, 'd/m/Y');
                }
            },
            {
                header: l('approved'),
                xtype: 'booleancolumn',
                width: 50,
                dataIndex: 'approved',
                trueText: Pms.iconOk,
                falseText: Pms.iconRemove
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                header: l('actions'),
                width: 50,
                hidden: me.book,
                items: [
                    {
                        iconCls: 'app-icon-book',
                        tooltip: l('editing'),
                        handler: function (grid, rowIndex, colIndex) {
                            var me = this,
                                store = grid.getStore(),
                                row = store.getAt(rowIndex),
                                data = row.data,
                                win = Ext.widget('seasonEditWindow', {data: data}),
                                form = win.down('form');

                            form.getForm().setValues(data);
                            win.show();
                        }
                    }
                ]
            }
        ];
        me.callParent();
    }
});