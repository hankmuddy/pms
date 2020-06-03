Ext.define("Pms.modules.bookingButton.view.BookingButtonGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.bookingButtonGrid',
    store: 'Pms.modules.bookingButton.store.BookingButtonStore',
    border: false,
    editable: false,
    paging: true,
    autoScroll: true,
    forceFit: true,
    multiSelect: true,

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('catalog.title'),
                header: l('catalog.title'),
                dataIndex: 'name',
                flex: 4
            },
            {
                header: l('catalog.price'),
                dataIndex: 'language',
                flex: 1
            },
            {
                header: l('catalog.measure'),
                dataIndex: 'color',
                flex: 1
            },
            {
                header: l('catalog.measure'),
                dataIndex: 'currency',
                flex: 1
            }
        ];

        me.callParent();
    }
});