Ext.define('Pms.modules.room.view.freeRoomGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.freeRoomGrid',
    store: 'Pms.modules.groupRoomUse.store.Resource',
    autoLoading: false,
    paging: false,
    border: true,
    itemId: 'freeRoomGrid',
    book: false,
    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('room'),
                sortable: false,
                width: 50,
                dataIndex: 'number'
            },
            {
                header: l('roomType'),
                sortable: false,
                width: 150,
                dataIndex: 'roomType',
                renderer: function (v) {
                    return v.name;
                }
            },
            {
                header: l('floor'),
                sortable: false,
                width: 50,
                dataIndex: 'floor'
            },
//            {
//                header: l('capacity'),
//                sortable: false,
//                width: 80,
//                dataIndex: 'capacity'
//            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                header: l('actions'),
                width: 50,
                hidden: me.book,
                items: [
                    {
                        iconCls: 'app-icon-book',
                        tooltip: l('book'),
                        handler: function (grid, rowIndex, colIndex) {
                            var store = grid.getStore(),
                                row = store.getAt(rowIndex),
                                rec = row.data,
                                form = grid.up('roomUseFilterForm').getForm(),
                                filterData = form.getValues(),
                                win = Ext.widget('groupRoomUseAddWindow'),
                                gruForm = win.down('groupRoomUseForm'),
                                gruData = {useType: 'BOOKING_FREE', room: rec.id};

                            if (filterData.startDate) {
                                gruData.startDate = new Date(filterData.startDate * 1000);
                            }
                            if (filterData.endDate) {
                                gruData.endDate = new Date(filterData.endDate * 1000);
                            }

                            gruForm.getForm().setValues(gruData);
                            win.show();
                        }
                    }
                ]
            }
        ];
        me.callParent();
    }
});