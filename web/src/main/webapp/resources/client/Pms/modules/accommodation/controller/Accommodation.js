Ext.define('Pms.modules.accommodation.controller.Accommodation', {
    extend: "Pms.abstract.Controller",
    views: [
        'Pms.modules.accommodation.view.Viewport',
        'Pms.modules.accommodation.view.AccommodationGrid',
        'Pms.modules.accommodation.view.AccommodationForm',
        'Pms.modules.accommodation.view.AccommodationAddWindow',
        'Pms.modules.accommodation.view.AccommodationEditWindow',
        'Pms.modules.room.view.RoomGrid'
    ],
    stores: [
        'Pms.modules.accommodation.store.Accommodation',
        'Pms.modules.room.store.Room'
    ],
    models: [
        'Pms.modules.accommodation.model.Accommodation'
    ],
    refs: [
        {
            ref: 'accommodationGrid',
            selector: 'accommodationGrid'
        },
        {
            ref: 'accommodationStore',
            selector: 'accommodationStore'
        }
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.widget('accommodationViewport');
            this.buildItems(view);
        }

        this.control({
            'accommodationGrid button[action=new]': {
                click: this.newAccommodation
            },
            'accommodationAddWindow button[action=save]': {
                click: this.addAccommodation
            },
            'accommodationGrid button[action=edit]': {
                click: this.editAccommodation
            },
            'accommodationGrid': {
                itemdblclick: this.editAccommodation
            },
            'accommodationEditWindow button[action=save]': {
                click: this.updateAccommodation
            },
            'accommodationEditWindow': {
                close: this.onCloseEditWindow
            },
            'accommodationAddWindow': {
                close: this.onCloseEditWindow
            },
            'accommodationGrid button[action=commit]': {
                click: this.commitAccommodation
            },
            'accommodationGrid button[action=delete]': {
                click: this.deleteAccommodation
            }
        });
    },

    newAccommodation: function () {
        var win = Ext.widget('accommodationAddWindow');
        win.show();
    },

    addAccommodation: function (button) {
        var win = button.up('accommodationAddWindow'),
            form = win.down('accommodationForm'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.accommodation.model.Accommodation', values),
            grid = this.getAccommodationGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            store.add(rec);
            store.sync({
                success: function () {
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('incorrectForm'));
        }
    },

    editAccommodation: function (button, e) {
        var grid = button.up('accommodationGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0];

            var win = Ext.widget('accommodationEditWindow', {
                    data: rec.data
                }),
                accommodationStore = grid.getStore(),
                form = win.down('accommodationForm'),
                rooms = rec.data.rooms;

            if (!rooms) rooms = [];

            form.loadRecord(rec);

            if (rec.get('approved')) {
                var tabPanel = win.down('tabpanel'),
                    tabBar = tabPanel.getTabBar();
                tabPanel.setActiveTab(1);
                tabBar.items.items[0].disable();
            }
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    updateAccommodation: function (button) {
        var win = button.up('accommodationEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = Ext.ComponentQuery.query('accommodationGrid[rendered=true]')[0],
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.sync({
                success: function () {
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('incorrectForm'));
        }
    },

    onCloseEditWindow: function () {
        var grid = Ext.ComponentQuery.query('accommodationGrid[rendered=true]')[0];
        if (typeof(grid) != 'undefined') {
            grid.getStore().reload();
            grid.getSelectionModel().clearSelections();
        }
    },

    deleteAccommodation: function (button) {
        var grid = button.up('accommodationGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();

        var record;
        if (selected.length > 0) {
            record = selected[0];
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return
        }
        if (record.get('approved')) {
            Ext.Msg.alert(l('warning'), l('warning.cantDeleteCommited'));
        }
        else {
            Ext.Msg.confirm(l('accommodation.delete'), l('confirmation'), function (btn) {
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
            }, this);
        }
    },

    commitAccommodation: function (button, e) {
        var grid = button.up('accommodationGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection(),
            idArray = '';

        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('commitAccommodation'), function (btn) {
                if (btn === 'yes') {
                    Ext.each(selected, function (data, index) {
                        idArray += data.internalId;
                        if (index != selected.length - 1)
                            idArray += ',';
                    }, this);
                    Pms.Ajax.request({
                        url: "rest/accommodation/" + idArray + '/approved',
                        method: 'PUT',
                        success: function (response) {
                            store.reload();
                            grid.getSelectionModel().clearSelections();
                            Pms.App.showNotification({
                                message: l('commitSuccess'),
                                icon: Pms.notificationOk
                            });
                        },
                        failure: function (response) {
                            Pms.App.showNotification({
                                message: l('accommodationCommitError')
                            });
                        }
                    });
                }
            }, this);
        } else {
            Ext.Msg.alert(l('warning'), l('needSelectAccommodation'));
        }
    }
});