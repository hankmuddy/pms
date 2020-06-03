Ext.define("Pms.modules.roomType.controller.RoomType", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.roomType.view.Viewport",
        "Pms.modules.roomType.view.RoomTypeGrid",
        "Pms.modules.roomType.view.RoomTypeEditWindow",
        "Pms.modules.roomType.view.RoomTypeAddWindow",
        "Pms.modules.roomType.view.RoomTypeForm",
        "Pms.modules.roomType.view.facilities",
        "Pms.modules.room.view.RoomGrid",
        "Pms.modules.virtualRoom.view.VirtualRoomGrid",
        'Pms.modules.otaCalendar.view.otaCalendarSch',

        'Pms.modules.settings.view.Viewport',
        'Pms.modules.settings.view.galleryUploadForm',
        'Pms.modules.settings.view.gallery',
        'Pms.modules.settings.view.dataViewWindow',
        'Pms.modules.settings.view.imgDataView'
    ],
    stores: [
        "Pms.modules.roomType.store.RoomType",
        'Pms.modules.roomType.store.BaseRoom',
        'Pms.modules.otaCalendar.store.Event',
        'Pms.modules.otaCalendar.store.Resource',

        'Pms.modules.settings.store.Gallery'
    ],
    models: [
        "Pms.modules.roomType.model.RoomType",
        'Pms.modules.otaCalendar.model.Resource',
        'Pms.modules.otaCalendar.model.Event'
    ],
    subControllers: [
        'Pms.modules.room.controller.Room',
        'Pms.modules.virtualRoom.controller.VirtualRoom',
        'Pms.modules.otaCalendar.controller.otaCalendar',
        'Pms.modules.periodInfo.controller.PeriodInfo'
    ],
    refs: [
        {ref: 'roomTypeGrid', selector: 'roomTypeGrid'},
        {ref: 'roomTypeStore', selector: 'roomTypeStore'},
        {ref: 'roomTypeViewport', selector: 'roomTypeViewport'},

        {ref: 'settingsViewport', selector: 'settingsViewport'}
    ],

    extravailable: true,

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create("Pms.modules.roomType.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            "roomTypeGrid button[action=new]": {
                click: this.newRoomType
            },
            'roomTypeGrid button[action=edit]': {
                click: this.editRoomType
            },
            'roomTypeGrid': {
                itemdblclick: this.editRoomType
            },
            "roomTypeGrid button[action=delete]": {
                click: this.deleteRoomType
            },
            "roomTypeGrid button[action=commit]": {
                click: this.commitRoomType
            },
            "roomTypeGrid button[action=default]": {
                click: this.setDefaultVirtualRoom
            },
            'roomTypeEditWindow button[action=update]': {
                click: this.updateRoomType
            },
            'roomTypeAddWindow button[action=add-roomType]': {
                click: this.addRoomType
            },
            'roomTypeEditWindow galleryUploadForm filefield': {
                change: this.uploadPhoto
            },
            'roomTypeEditWindow gallery': {
                deletephoto: this.deletePhoto
            },
            'gallery[type=roomType]': {
                deletephoto: this.deletePhoto
            },
            'dataViewWindow button[action=photoselected]': {
                click: this.assignPhoto
            },
            // refresh actions
            'roomTypeEditWindow': {
                close: this.refreshOnClose
            },
            'roomTypeAddWindow': {
                close: this.refreshOnClose
            }
        });
    },

    newRoomType: function () {
        var win = Ext.widget('roomTypeAddWindow');
        win.show();
    },

    addRoomType: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.roomType.model.RoomType', values),
            grid = this.getRoomTypeGrid(),
            store = grid.getStore(),
            fieldset = form.down('fieldset[name=details]'),
            detailsFields = fieldset.getRefItems(),
            details = {};

        for (var i = 1; i < detailsFields.length; i++) {
            details[detailsFields[i].name] = parseInt(detailsFields[i].value);
        }
        if (form.isValid()) {
            rec.set(values);
            rec.data.details = details;
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
        } else {
            Ext.Msg.alert(l('error'), l('roomTypeSaveError'));
        }
    },

    editRoomType: function (button, e) {
        var grid = button.up('roomTypeGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0];

            Pms.Ajax.request({
                url: 'rest/roomTypeFacility/byRoomType/' + rec.data.id,
                method: 'GET',
                async: false,
                success: function (response) {
//                    Pms.App.showNotification({
//                        message: l('saveSuccess.msg'),
//                        icon: Pms.notificationOk
//                    });
                    rec.data.facilities = Ext.pluck(response.content, 'id');
                },
                failure: function () {
                    Pms.App.showNotification({
                        message: l('roomTypeInfoRecieveError')
                    });
                }
            });

            Pms.Ajax.request({
                url: 'rest/roomType/' + rec.data.id,
                method: 'GET',
                async: false,
                success: function (response) {
                    var viewRec = Ext.create('Pms.modules.roomType.model.RoomType', response.content),
                        details = response.content.details;
                    var win = Ext.widget('roomTypeEditWindow', {data: rec.data}),
                        form = win.down('form');

                    form.getForm().setValues(details);
                    form.loadRecord(rec);
//                    Pms.App.showNotification({
//                        message: l('saveSuccess.msg'),
//                        icon: Pms.notificationOk
//                    });
                },
                failure: function () {
                    Pms.App.showNotification({
                        message: l('roomTypeInfoRecieveError')
                    });
                }
            });


        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    updateRoomType: function (btn) {
        var win = btn.up('roomTypeEditWindow'),
            form = win.down('roomTypeForm'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getRoomTypeGrid(),
            store = grid.getStore(),
            facilityForm = win.down('facilities'),
            facilityValues = facilityForm.getValues(),
            fieldset = form.down('fieldset[name=details]'),
            detailsFields = fieldset.getRefItems(),
            details = {};
        for (var i = 1; i < detailsFields.length; i++) {
            details[detailsFields[i].name] = parseInt(detailsFields[i].value);
            console.log(detailsFields[i].name);
        }

        if (facilityForm.isValid()) {
            var data = {roomType: {id: parseInt(values.id), type: 'roomType'}, facilities: []};
            if (!Ext.isEmpty(facilityValues)) {
                for (var id in facilityValues) {
                    data.facilities.push({
                        id: parseInt(id),
                        type: 'ROOM_TYPE'
                    });
                }
            }

            Pms.Ajax.request({
                url: 'rest/roomTypeFacility/bind',
                method: 'PUT',
                jsonData: data,
                success: function (response) {
                    Pms.App.showNotification({
                        message: l('saveSuccess.msg'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    Pms.App.showNotification({
                        message: l('roomTypeInfoSaveError')
                    });
                }
            });
        } else {
            Ext.Msg.alert(l('warning'), l('roomTypeInfoSaveError'));
        }

        if (form.isValid() && !record.data.approved) {
            values.details = details;
            record.set(values);
            store.sync({
                success: function () {
//                    win.close();
                    store.reload();
                },
                failure: function () {
//                    win.close();
                    store.reload();
                }
            });
        }
    },

    commitRoomType: function (button) {
        var me = this,
            grid = button.up('roomTypeGrid'),
            selected = grid.getSelectionModel().getSelection(),
            store = grid.getStore(),
            idArray = '';

        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('confirmRoomType'), function (btn) {
                if (btn === "yes") {
                    Ext.each(selected, function (data, index) {
                        idArray += data.internalId;
                        if (index != selected.length - 1)
                            idArray += ',';
                    }, this);
                    Pms.Ajax.request({
                        url: "rest/roomType/" + idArray + '/approved',
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        success: function (data) {
                            me.refreshOnClose();
                            Pms.App.showNotification({
                                message: l('commitSuccess'),
                                icon: Pms.notificationOk
                            });
                        }
                    });

                }

            }, this);
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    deleteRoomType: function (button) {
        var record,
            me = this,
            grid = button.up('roomTypeGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();
        if (selected.length > 0) {
            record = selected[0];
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return
        }
        if (record.get('approved')) {
            Ext.Msg.alert(l('warning'), Pms.iconError + l('warning.cantDeleteCommited'));
        }
        else {
            Ext.Msg.confirm(l('deleteRoomType'), l('common.confirm.delete.message'), function (btn) {
                if (btn === 'yes') {
                    store.remove(selected[0]);
                    store.sync({
                        success: function (batch, o) {
                            me.refreshOnClose();
                        },
                        failure: function () {
                            store.reload();
                        }
                    });
                }
            }, this);
        }
    },

    uploadPhoto: function (field) {
        var form = field.up('galleryUploadForm'),
            gallery = form.nextNode('gallery'),
            dataview = gallery.down('dataview'),
            store = dataview.getStore(),
            photos = [];

        if (store.data.items.length) {
            for (var i in store.data.items) {
                photos.push(store.data.items[i].data.code);
            }
        }

        form.getForm().submit({
            scope: this,
            success: function (s, res) {
                photos.push(res.result.content);
                this.bindPhoto(gallery.roomTypeId, photos, dataview.store);
            },
            failure: function () {
                Pms.App.showNotification({
                    message: l('photo.uploadFail')
                });
            }
        });
    },

    deletePhoto: function (dataview, rec) {
        var me = this,
            gallery = dataview.up('gallery'),
            store = dataview.getStore(),
            photo = rec.data.code,
            photos = [];

        store.remove(rec);

        if (store.data.items.length) {
            for (var i in store.data.items) {
                photos.push(store.data.items[i].data.code);
            }
        }

        this.bindPhoto(gallery.roomTypeId, photos, store);
        dataview.refresh();
    },

    assignPhoto: function (btn, e) {
        var dataViewWin = btn.up('dataViewWindow'),
            dataview = dataViewWin.down('dataview'),
            store = dataview.getStore(),
            roomTypeId = dataViewWin.roomTypeId,
            roomTypeStore = dataViewWin.roomTypeStore,
            roomTypePhotos = dataViewWin.roomTypePhotos,
            records = dataview.getSelectionModel().getSelection();

        if (records.length) {
            for (var i in records) {
                if (!Ext.Array.contains(roomTypePhotos, records[i].data.code)) {
                    roomTypePhotos.push(records[i].data.code);
                }
            }
            this.bindPhoto(roomTypeId, roomTypePhotos, roomTypeStore);
            dataViewWin.close();
        }
    },

    bindPhoto: function (roomTypeId, codes, galleryStore) {
        if (!Ext.isEmpty(roomTypeId)) {
            Pms.Ajax.request({
                url: 'rest/document/bind/',
                method: 'PUT',
                async: false,
                jsonData: {roomType: {id: roomTypeId, type: 'roomType'}, codes: codes},
                success: function (response) {
                    if (!Ext.isEmpty(galleryStore)) galleryStore.load();
                    Pms.App.showNotification({
                        message: l('photo.listUpdated'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    Pms.App.showNotification({
                        message: l('photo.listUpdateError')
                    });
                }
            });
        }
    },
    setDefaultVirtualRoom: function (button) {
        var grid = button.up('roomTypeGrid'),
            selected = grid.getSelectionModel().getSelection(),
            store = grid.getStore(),
            id = selected[0].getId(),
            me = this;

        if (selected.length > 0) {
            Pms.Ajax.request({
                url: 'rest/baseRoom/' + id + '/default',
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                success: function (data) {
                    store.reload();
                    button.up('window').down('virtualRoomGrid').getStore().reload();
                    grid.getSelectionModel().clearSelections();
                    Pms.App.showNotification({
                        message: l('success'),
                        icon: Pms.notificationOk
                    });
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('virtualRoom.warning')
            });
        }
    },

    refreshOnClose: function () {
        var roomTypeViewport = this.getRoomTypeViewport();
        if (!Ext.isEmpty(roomTypeViewport)) {
            var roomTypeGrid = roomTypeViewport.down('roomTypeGrid');
            if (!Ext.isEmpty(roomTypeGrid)) {
                roomTypeGrid.getStore().reload();
                roomTypeGrid.getSelectionModel().clearSelections();
            }
            var virtualRoomGrid = roomTypeViewport.down('virtualRoomGrid');
            if (!Ext.isEmpty(virtualRoomGrid)) {
                virtualRoomGrid.getStore().reload();
                virtualRoomGrid.getSelectionModel().clearSelections();
            }
        }
        var settingsViewport = this.getSettingsViewport();
        if (!Ext.isEmpty(settingsViewport)) {
            Ext.widget('settingsStore').load({
                scope: this,
                callback: function (records, operation, success) {
                    var data = records[0].data,
                        reviewTab = settingsViewport.down('review').up('panel[tab]');

                    reviewTab.removeAll(true);

                    var review = Ext.widget('review', {data: data});
                    reviewTab.add(review);
                    reviewTab.doLayout();
                }
            });
        }
    }
});