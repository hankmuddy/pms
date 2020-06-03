Ext.define('Pms.modules.console.controller.Console', {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.console.view.Viewport',
        'Pms.modules.serviceUse.view.serviceUseGroupGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseConfirmWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseIncomeWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseOutgoWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseRefuseWindow',
        'Pms.modules.booking.view.consoleGrid',
        'Pms.modules.booking.view.BookingGrid',
        'Pms.modules.booking.view.BookingFilterForm'
    ],
    stores: [
        'Pms.modules.roomUse.store.roomUse',
        'Pms.modules.serviceUse.store.serviceUse'
    ],
    models: [],

    refs: [
        {ref: 'consoleViewport', selector: 'consoleViewport'},
        {ref: 'consoleGrid', selector: 'consoleGrid'},

        {ref: 'roomUseStore', selector: 'roomUseStore'}
    ],

    subControllers: [
        'Pms.modules.groupRoomUse.controller.groupRoomUse',
        'Pms.modules.booking.controller.Booking'
    ],

    init: function (contr, subController) {
        if (!subController) {
            this.buildItems();
        }

        this.control({
            'groupRoomUseEditWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseIncomeWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseOutgoWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseConfirmWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseRefuseWindow': {
                close: this.refreshOnClose
            }
        });
    },

    refreshOnClose: function () {
        var consoleViewport = this.getConsoleViewport();
        if (!Ext.isEmpty(consoleViewport)) {
            var incomeGrid = consoleViewport.down('consoleGrid[gridName=income]')/*, // all console grids use same store, it's enough to refresh only one console Store
                outgoGrid = consoleViewport.down('consoleGrid[gridName=outgo]'),
                bookingsGrid = consoleViewport.down('consoleGrid[gridName=bookings]'),
                livingGrid = consoleViewport.down('consoleGrid[gridName=living]'),
                skippedGrid = consoleViewport.down('consoleGrid[gridName=skipped]')*/;

            if (!Ext.isEmpty(incomeGrid)) {
                incomeGrid.getStore().reload();
                incomeGrid.getSelectionModel().clearSelections();
            }
//            if (!Ext.isEmpty(outgoGrid)) {
//                outgoGrid.getStore().reload();
//                outgoGrid.getSelectionModel().clearSelections();
//            }
//            if (!Ext.isEmpty(bookingsGrid)) {
//                bookingsGrid.getStore().reload();
//                bookingsGrid.getSelectionModel().clearSelections();
//            }
//            if (!Ext.isEmpty(livingGrid)) {
//                livingGrid.getStore().reload();
//                livingGrid.getSelectionModel().clearSelections();
//            }
//            if (!Ext.isEmpty(skippedGrid)) {
//                skippedGrid.getStore().reload();
//                skippedGrid.getSelectionModel().clearSelections();
//            }
        }
    },

    buildItems: function () {
        if (!Ext.isEmpty(this.win)) {
            this.win.maximized = true;
            this.win.add(Ext.widget('consoleViewport'));
        }
    }
});