Ext.define('Pms.modules.settings.controller.Settings', {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.settings.view.Viewport',
        'Pms.modules.settings.view.descriptionForm',
        'Pms.modules.settings.view.galleryUploadForm',
        'Pms.modules.settings.view.gallery',
        'Pms.modules.settings.view.commonForm',
        'Pms.modules.settings.view.allFacilities',
        'Pms.modules.settings.view.review',
        'Pms.modules.settings.view.roomTypeTabs',
        'Pms.modules.settings.view.dataViewWindow',
        'Pms.modules.settings.view.uploadWindow',
        'Pms.modules.settings.view.imgDataView',
        'Pms.modules.settings.view.channelManagerForm',
        'Pms.modules.settings.view.bookingButtonForm',


        'Pms.modules.bookingButton.view.BookingButtonGrid',


        'Pms.modules.accommodation.view.AccommodationGrid',
        'Pms.modules.accommodation.view.AccommodationForm',
        'Pms.modules.accommodation.view.AccommodationAddWindow',
        'Pms.modules.accommodation.view.AccommodationEditWindow',

        'Pms.modules.room.view.RoomGrid',

        'Pms.modules.bankDetails.view.BankDetailsGrid'
    ],
    stores: [
        'Pms.modules.settings.store.Settings'
    ],
    models: [
        'Pms.modules.settings.model.Settings'
    ],
    refs: [
        {ref: 'settingsViewport', selector: 'settingsViewport'},
        {ref: 'gallery', selector: 'gallery'},
        {ref: 'galleryStore', selector: 'galleryStore'},
        {ref: 'settingsStore', selector: 'settingsStore'}
    ],

    extravailable: true,

    subControllers: [
//        'Pms.modules.bookingButton.controller.BookingButtonController',

        'Pms.modules.accommodation.controller.Accommodation',
        'Pms.modules.bankDetails.controller.BankDetails',
        'Pms.modules.roomType.controller.RoomType'
    ],

    label: l('hotel.settings'),

    init: function (contr, subController) {
        var me = this;

        if (!subController) {
            me.buildItems();
        }

        me.control({
            'commonForm button[action=update]': {
                click: me.update
            },
            'channelManagerForm button[action=importBookings]': {
                click: me.importBookings
            },
            'channelManagerForm button[action=importFromWubook]': {
                click: me.importFromWubook
            },
            'channelManagerForm button[action=exportToWubook]': {
                click: me.exportToWubook
            },
            'channelManagerForm button[action=updateQuota]': {
                click: me.updateQuota
            },
            'allFacilities button[action=update]': {
                click: me.update
            },
            'commonForm filefield': {
                change: me.uploadLogo
            },
            'galleryUploadForm filefield': {
                change: me.uploadPhoto
            },
            'galleryUploadForm filesfield': {
                change: me.uploadPhoto
            },
            'gallery[type=hotel]': {
                deletephoto: me.deletePhoto,
                mainphoto: me.mainPhoto
            },
            //refresh actions
            'uploadWindow': {
                close: me.refreshOnClose
            }
        });
    },

    buildItems: function () {
        if (!Ext.isEmpty(this.win)) {
            Ext.widget('settingsStore').load({
                scope: this,
                callback: function (records, operation, success) {
                    var data = records[0].data;

                    Pms.Ajax.request({
                        url: 'rest/settings/hotelFacilities',
                        method: 'GET',
                        async: false,
                        success: function (response) {
                            data.facilities = response.content;
                        },
                        failure: function () {
                            Pms.App.showNotification({
                                message: l('facilitiesError')
                            });
                        }
                    });

                    var view = Ext.widget('settingsViewport', {data: data});
                    view.down('commonForm').getForm().setValues(data.hotelInfo);
                    this.win.add(view);
                }
            });
        }
    },

    update: function (btn) {
        var me = this,
            formPanel = btn.up('form'),
            form = formPanel.getForm(),
            bind = '';

        if (form.isValid()) {
            if (formPanel.getXType() == 'commonForm') {
                data = form.getFieldValues();
                if (formPanel.data.hotelInfo.logo || formPanel.data.hotelInfo.logo == null) {
                    data.logo = formPanel.data.hotelInfo.logo;
                }

                data.checkIn = data.checkIn.getHours() * 60 + data.checkIn.getMinutes();
                if (data.earlyCheckInStart != null && data.earlyCheckInEnd != null) {
                    data.earlyCheckInStart = data.earlyCheckInStart.getHours() * 60 + data.earlyCheckInStart.getMinutes();
                    data.earlyCheckInEnd = data.earlyCheckInEnd.getHours() * 60 + data.earlyCheckInEnd.getMinutes();
                }
                data.checkOut = data.checkOut.getHours() * 60 + data.checkOut.getMinutes();
                if (data.lateCheckOutStart != null && data.lateCheckOutEnd != null) {
                    data.lateCheckOutStart = data.lateCheckOutStart.getHours() * 60 + data.lateCheckOutStart.getMinutes();
                    data.lateCheckOutEnd = data.lateCheckOutEnd.getHours() * 60 + data.lateCheckOutEnd.getMinutes();
                }
                data.timeZone = data.timeZone ? data.timeZone : 'UTC';
            }
            else if (formPanel.getXType() == 'allFacilities') {
                var values = form.getFieldValues(),
                    data = [];

                delete values.id;
                for (var id in values) {
                    if (values[id] && !isNaN(parseInt(id)))
                        data.push({facility: {id: id, type: 'HOTEL'}, chargeFree: 'N_A'});
                }

                bind = '/bind';
            }

            delete data.type;
            delete data.dfrom;
            delete data.dto;
            delete data.file;

            if (data.paymentInfo) {
                var paymentInfo = {
                    value: data.paymentInfo
                };
                Pms.Ajax.request({
                    url: 'rest/settings/paymentInfo',
                    method: 'PUT',
                    jsonData: paymentInfo,
                    success: function (response) {
//                        me.refreshOnClose();
//                        Pms.App.showNotification({
//                            message: l('settings.changed'),
//                            icon: Pms.notificationOk
//                        });
                    },
                    failure: function () {
//                        Pms.App.showNotification({
//                            message: l('settings.notChanged')
//                        });
                    }
                });
                delete data.paymentInfo;
            }

            if (data.importantInfo) {
                var importantInfo = {
                    value: data.importantInfo
                };
                Pms.Ajax.request({
                    url: 'rest/settings/importantInfo',
                    method: 'PUT',
//                params: {importFromWubook: 'true'},
                    jsonData: importantInfo,
                    success: function (response) {
//                        me.refreshOnClose();
//                        Pms.App.showNotification({
//                            message: l('settings.changed'),
//                            icon: Pms.notificationOk
//                        });
                    },
                    failure: function () {
//                        Pms.App.showNotification({
//                            message: l('settings.notChanged')
//                        });
                    }
                });

                delete data.importantInfo;
            }

            Pms.Ajax.request({
                url: 'rest/settings' + bind,
                method: 'PUT',
//                params: {importFromWubook: 'true'},
                jsonData: data,
                success: function (response) {
                    me.refreshOnClose();
                    Pms.App.showNotification({
                        message: l('settings.changed'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    Pms.App.showNotification({
                        message: l('settings.notChanged')
                    });
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('incorrectForm'));
        }
    },

    uploadLogo: function (field) {
        var me = this,
            uploadForm = field.up('form'),
            commonForm = uploadForm.up('commonForm'),
            img = uploadForm.up('fieldset').down('image');

        uploadForm.getForm().submit({
            scope: me,
            params: {type: 'LOGO'},
            failure: function (s, res) {

            },
            success: function (s, res) {
                commonForm.data.hotelInfo.logo = res.result.content;
                img.setSrc(_('imagesUrlPrefix') + res.result.content);
            }
        });
    },

    mainPhoto: function (dataview, rec) {
        var me = this,
            commonForm = Ext.ComponentQuery.query('commonForm')[0],
            mainPhotoFiled = commonForm.getForm().findField('mainPhoto');

        mainPhotoFiled.setValue(rec.data.code);

        if (rec.data.code) {
            var importantInfo = {
                value: rec.data.code
            };
            Pms.Ajax.request({
                url: 'rest/settings/mainPhoto',
                method: 'PUT',
//                params: {importFromWubook: 'true'},
                jsonData: importantInfo,
                success: function (response) {
//                        me.refreshOnClose();
//                        Pms.App.showNotification({
//                            message: l('settings.changed'),
//                            icon: Pms.notificationOk
//                        });
                },
                failure: function () {
//                        Pms.App.showNotification({
//                            message: l('settings.notChanged')
//                        });
                }
            });

//            delete data.importantInfo;
        }
//        commonForm.data.hotelInfo.mainPhoto = rec.data.code;

//        console.log(commonForm.data);

//        var me = this;
//        Pms.Ajax.request({
//            url: 'rest/document/photo/' + rec.data.code,
//            method: 'DELETE',
//            success: function (response) {
//                me.refreshOnClose();
//                Pms.App.showNotification({
//                    message: l('photo.deleteSuccess'),
//                    icon: Pms.notificationOk
//                });
//            },
//            failure: function () {
//                Pms.App.showNotification({
//                    message: l('photo.deleteError')
//                });
//            }
//        });
    },

    deletePhoto: function (dataview, rec) {
        var me = this;
        Pms.Ajax.request({
            url: 'rest/document/photo/' + rec.data.code,
            method: 'DELETE',
            success: function (response) {
                me.refreshOnClose();
                Pms.App.showNotification({
                    message: l('photo.deleteSuccess'),
                    icon: Pms.notificationOk
                });
            },
            failure: function () {
                Pms.App.showNotification({
                    message: l('photo.deleteError')
                });
            }
        });
    },

    importFromWubook: function () {
        Ext.Msg.confirm(l('importFromWubook.title'), l('importFromWubook.confirmation'), function (btn) {
            if (btn === 'yes') {
                Pms.Ajax.request({
                    url: 'rest/settings/importFromWubook',
                    method: 'POST',
                    success: function (response) {
                        Pms.App.showNotification({
                            message: l('import.successMessage'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function () {
                        Pms.App.showNotification({
                            message: l('import.errorMessage')
                        });
                    }
                });
            }
        });
    },

    exportToWubook: function () {
        Ext.Msg.confirm(l('exportToWubook.title'), l('exportToWubook.confirmation'), function (btn) {
            if (btn === 'yes') {
                Pms.Ajax.request({
                    url: 'rest/settings/exportToWubook',
                    method: 'POST',
                    success: function (response) {
                        Pms.App.showNotification({
                            message: l('export.successMessage'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function () {
                        Pms.App.showNotification({
                            message: l('export.errorMessage')
                        });
                    }
                });
            }
        });
    },

    importBookings: function (button) {
        var form = button.up('form');

        Ext.Msg.confirm(l('importBookings.title'), l('importBookings.confirmation'), function (btn) {
            if (btn === 'yes') {
                Pms.Ajax.request({
                    url: 'wubooking/importBookings',
                    method: 'POST',
                    success: function (response) {
                        Pms.App.showNotification({
                            message: l('import.successMessage'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function () {
                        Pms.App.showNotification({
                            message: l('import.errorMessage')
                        });
                    }
                });
            }
        });
    },
    updateQuota: function (button) {
        var form = button.up('form');
        if (!form.isValid()) {
            Pms.App.showNotification({
                message: l('incorrectForm')
            });
            return
        }
        var dTo = button.previousNode().getValue(),
            dFrom = button.previousNode().previousNode().getValue();
        dTo = Ext.Date.format(dTo, 'Y-m-d');
        dFrom = Ext.Date.format(dFrom, 'Y-m-d');
        Ext.Msg.confirm(l('importBookings.title'), l('importBookings.confirmation'), function (btn) {
            if (btn === 'yes') {
                Pms.Ajax.request({
                    url: 'rest/settings/synchronizeQuota',
                    method: 'GET',
                    params: {
                        dto: dTo,
                        dfrom: dFrom
                    },
                    success: function (response) {
                        Pms.App.showNotification({
                            message: l('synchronizeQuota.confirm'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function () {
                        Pms.App.showNotification({
                            message: l('import.errorMessage')
                        });
                    }
                });
            }
        });
    },

    refreshOnClose: function () {
        var me = this;
        var settingsViewport = this.getSettingsViewport();
        if (!Ext.isEmpty(settingsViewport)) {
            Ext.widget('settingsStore').load({
                scope: this,
                callback: function (records, operation, success) {
                    var data = records[0].data,
                        reviewTab = settingsViewport.down('review').up('panel[tab]');

                    Pms.Ajax.request({
                        url: 'rest/settings/hotelFacilities',
                        method: 'GET',
                        async: false,
                        success: function (response) {
                            data.facilities = response.content;
                            reviewTab.removeAll(true);
                            var review = Ext.widget('review', {
                                data: data
                            });
                            reviewTab.add(review);
                            reviewTab.doLayout();
                        },
                        failure: function () {
                            Pms.App.showNotification({
                                message: l('hotel.settingsSaved')
                            });
                        }
                    });
                }
            });

            var gallery = settingsViewport.down('gallery[galleryId=hotelPhoto]');

            if (!Ext.isEmpty(gallery)) {
                var dataviewStore = gallery.down('dataview').getStore();
                if (!Ext.isEmpty(dataviewStore)) {
                    dataviewStore.load();
                }
            }
        }
    }
});