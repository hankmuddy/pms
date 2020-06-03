Ext.define("Pms.modules.bookingButton.controller.BookingButtonController", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.bookingButton.view.Viewport",
        "Pms.modules.bookingButton.view.BookingButtonGrid",
        "Pms.modules.bookingButton.view.BookingButtonAddWindow",
        "Pms.modules.bookingButton.view.BookingButtonEditWindow"
    ],
    stores: [
        "Pms.modules.bookingButton.store.BookingButtonStore"
    ],
    models: [
        "Pms.modules.bookingButton.model.BookingButtonModel"
    ],

    refs: [
        {
            ref: 'bookingButtonGrid',
            selector: 'bookingButtonGrid'
        },
        {
            ref: 'bookingButtonStore',
            selector: 'bookingButtonStore'
        }
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create("Pms.modules.bookingButton.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            "bookingButtonViewport button[action=new]": {
                click: this.newItem
            },
            "bookingButtonViewport button[action=delete]": {
                click: this.deleteItem
            },
            'bookingButtonGrid': {
                itemdblclick: this.editItem
            },
            'bookingButtonEditWindow button[action=update]': {
                click: this.updateItem
            },
            'bookingButtonAddWindow button[action=add]': {
                click: this.addItem
            }
        });
    },

    newItem: function () {
        var win = Ext.widget('bookingButtonAddWindow');
        win.show();
    },

    addItem: function (button) {
        console.log('ok');
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.bookingButton.model.BookingButtonModel', values),
            grid = this.getBookingButtonGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            store.add(rec);
            store.sync({
                success: function () {
                    store.reload();
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
            win.close();
        } else {
            Ext.Msg.alert(l('error'), l('error.incorrect_form_data'));
        }
    },

    editItem: function (grid, record) {
        if (typeof grid.action !== "undefined") {
            var win = grid.up('bookingButtonViewport'),
                bookingButtonGrid = win.down('bookingButtonGrid'),
                store = bookingButtonGrid.getStore(),
                selected = bookingButtonGrid.getSelectionModel().getSelection();


            if (selected.length) {
                record = selected[0];
            }
            win = Ext.widget('bookingButtonEditWindow');
            win.down('form').loadRecord(record);

        } else {
            record.data.title = l(record.data.title);
            record.data.measure = l(record.data.measure);
            var view = Ext.widget('bookingButtonEditWindow');
            view.down('form').loadRecord(record);
        }
    },

    updateItem: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getBookingButtonGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.sync({
                success: function () {
                    store.reload();
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('error.incorrect_form_data'));
        }
    },

    deleteItem: function (button) {
        var win = button.up('bookingButtonViewport'),
            grid = win.down('bookingButtonGrid'),
            store = grid.getStore(),
            record = grid.getSelectionModel().getSelection();

        Ext.MessageBox.confirm(l('catalog.delete'), l('confirmation'), function (btn) {
            if (btn === 'yes') {
                store.remove(record);
                store.sync({
                    success: function () {
                        store.reload();
                    },
                    failure: function () {
                        store.reload();
                    }
                });
            }
        });
    }
});
