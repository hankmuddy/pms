Ext.define('Pms.modules.groupRoomUse.view.livingGrid', {
    extend: "Pms.abstract.Grid",
    alias: 'widget.livingGrid',
    store: 'Pms.modules.roomUse.store.roomUse',

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('room'),
                dataIndex: 'property_room',
                flex: 1,
                renderer: function (property_room) {
                    var room = property_room[0],
                        roomType = room.property_room_type[0].property_name;

                    return roomType + ', ' + room.property_name;
                }
            },
            {
                xtype: 'datecolumn',
                header: l('from'),
                dataIndex: 'property_startDate',
                flex: 1,
                format: 'd/m/Y'
            },
            {
                xtype: 'datecolumn',
                header: l('to'),
                dataIndex: 'property_endDate',
                flex: 1,
                format: 'd/m/Y'
            },
            {
                header: l('description'),
                dataIndex: 'property_description',
                flex: 1
            },
            {
                header: l('price'),
                dataIndex: 'property_rate',
                flex: 1
            }
        ];
        me.callParent();
    }
});