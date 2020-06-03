Ext.define('Pms.modules.roomUse.store.useType', {
    extend: 'Ext.data.JsonStore',
    alias: 'widget.useTypeStore',
    fields: ['id', 'label', 'color', 'iconUrl', 'iconCls'],
    data: [
        {
            id: 'BOOKING_FREE',
            label: l('filter.bookingStatus.NotApproved'),
            color: 'F78181',
            iconUrl: Pms.grayIcons + 'User-red-icon.png',
            iconCls: null
        },
        {
            id: 'BOOKING_WARRANTY',
            label: l('filter.bookingStatus.Approved'),
            color: 'F4FA58',
            iconUrl: Pms.grayIcons + 'User-orange-icon.png',
            iconCls: null
        },
        {
            id: 'LIVING',
            label: l('filter.bookingStatus.residence'),
            color: '64FE2E',
            iconUrl: Pms.grayIcons + 'User-green-icon.png',
            iconCls: null
        },
        {
            id: 'OUTGO',
            label: l('filter.bookingStatus.departure'),
            color: 'FE9A2E',
            iconUrl: Pms.grayIcons + 'rotate.png',
            iconCls: null
        },
        {
            id: 'NOT_ARRIVED',
            label: l('filter.bookingStatus.notArrived'),
            color: '7401DF',
            iconUrl: Pms.grayIcons + 'error.png',
            iconCls: null
        },
        {
            id: 'REFUSE',
            label: l('filter.bookingStatus.rejection'),
            color: 'EEEEEE',
            iconUrl: Pms.grayIcons + 'remove.png',
            iconCls: null
        }
    ]
});