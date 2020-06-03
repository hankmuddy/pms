Ext.define("Pms.modules.catalog.controller.Catalog", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.catalog.view.Viewport",
        "Pms.modules.catalog.view.CatalogGrid",
        "Pms.modules.catalog.view.CatalogAddWindow",
        "Pms.modules.catalog.view.CatalogEditWindow"
    ],
    stores: [
        "Pms.modules.catalog.store.Catalog"
    ],
    models: [
        "Pms.modules.catalog.model.Catalog"
    ],

    refs: [
        {
            ref: 'catalogGrid',
            selector: 'catalogGrid'
        },
        {
            ref: 'catalogStore',
            selector: 'catalogStore'
        }
    ],

    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.create("Pms.modules.catalog.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            "catalogViewport button[action=new]": {
                click: this.newItem
            },
            "catalogViewport button[action=delete]": {
                click: this.deleteItem
            },
            'catalogGrid': {
                itemdblclick: this.editItem
            },
            'catalogEditWindow button[action=update]': {
                click: this.updateItem
            },
            'catalogAddWindow button[action=add]': {
                click: this.addItem
            }
        });
    },

    newItem: function () {
        var win = Ext.widget('catalogAddWindow');
        win.show();
    },

    addItem: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.catalog.model.Catalog', values),
            grid = this.getCatalogGrid(),
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
            var win = grid.up('catalogViewport'),
                catalogGrid = win.down('catalogGrid'),
                store = catalogGrid.getStore(),
                selected = catalogGrid.getSelectionModel().getSelection();

            if (record.get('approved')) {
                Pms.App.showNotification({
                    message: l('notEditableItem')
                });
                return
            }

            win = Ext.widget('catalogEditWindow');
            win.down('form').loadRecord(record);
        } else {
            if (record.get('approved')) {
                Pms.App.showNotification({
                    message: l('notEditableItem')
                });
                return
            }
            record.data.title = l(record.data.title);
            record.data.measure = l(record.data.measure);
            var view = Ext.widget('catalogEditWindow');
            view.down('form').loadRecord(record);
        }
    },

    updateItem: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getCatalogGrid(),
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
        var win = button.up('catalogViewport'),
            grid = win.down('catalogGrid'),
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
