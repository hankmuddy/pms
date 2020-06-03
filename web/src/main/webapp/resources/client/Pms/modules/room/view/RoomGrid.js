Ext.define('Pms.modules.room.view.RoomGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.roomGrid',
    store: 'Pms.modules.room.store.Room',
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            dragGroup: 'roomGrid',
            dropGroup: 'roomGrid'
        },
        listeners: {
            drop: function (node, data, dropRec, dropPosition) {
                var id = data.records[0].getId(),
                    position = dropRec.index + 1,
                    me = this;
                Ext.Ajax.request({
                    url: 'rest/room/' + id + '/position/' + position,
                    method: 'PUT',
                    success: function(){
                        me.getStore().reload();
                    }
                })
            }
        }
    },

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('room'),
                dataIndex: 'number',
                flex: 1
            },
            {
                header: l('floor'),
                dataIndex: 'floor',
                flex: 1
            },
            {
                header: l('roomType'),
                dataIndex: 'roomType',
                flex: 1,
                renderer: function (value) {
                    return value.name
                }
            },
            {
                header: l('accommodation'),
                dataIndex: 'accommodation',
                flex: 1,
                renderer: function (value) {
                    return value.name
                }
            },
            {
                header: l('approved'),
                xtype: 'booleancolumn',
                dataIndex: 'approved',
                flex: 1,
                trueText: Pms.iconAccept,
                falseText: Pms.iconNotAccept
            }
        ];
        me.callParent();
    }
});