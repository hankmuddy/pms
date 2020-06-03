Ext.define('Pms.modules.company.controller.Company', {
    extend: "Pms.abstract.Controller",
    views: [
        'Pms.modules.company.view.Viewport',
        'Pms.modules.company.view.CompanyEditWindow',
        'Pms.modules.company.view.CompanyAddWindow',
        'Pms.modules.company.view.CompanyGrid',
        'Pms.modules.company.view.CompanyForm',
        'Pms.modules.company.view.CompanyFilterForm',
        'Pms.modules.company.view.CompanyDiscountWindow',
        'Pms.modules.person.view.PersonBookForm',
        'Pms.modules.child.view.ChildBookForm',
        'Pms.modules.group.view.GroupGrid',
        'Pms.modules.booking.view.consoleGrid',
        'Pms.modules.serviceUse.view.serviceUseGroupGrid',
        'Pms.modules.catalog.view.CatalogOrderGrid',
        'Pms.modules.bill.view.BillGrid',
        'Pms.modules.payment.view.PaymentGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseAddWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseAddTabs'
    ],
    stores: [
        'Pms.modules.company.store.Company',
        'Pms.modules.person.store.Country'
    ],
    models: [
        'Pms.modules.company.model.Company'
    ],
    refs: [
        {
            ref: 'companyViewport',
            selector: 'companyViewport'
        },
        {
            ref: 'companyGrid',
            selector: 'companyGrid'
        },
        {
            ref: 'consoleGrid',
            selector: 'consoleGrid'
        }
    ],

    extravailable: true,

    subControllers: [
        'Pms.modules.contact.controller.Contact',
        'Pms.modules.bill.controller.Bill',
        'Pms.modules.payment.controller.Payment'
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create("Pms.modules.company.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            // Company -----------------------
            'companyViewport button[action=new]': {
                click: this.newCompany
            },
            'companyViewport button[action=edit]': {
                click: this.editCompany
            },
            'companyGrid': {
                itemdblclick: this.editCompany
            },
            'companyViewport button[action=delete]': {
                click: this.deleteCompany
            },
            'companyViewport button[action=discount]': {
                click: this.discount
            },
            'companyDiscountWindow button[action=save]': {
                click: this.saveDiscount
            },
            'companyDiscountWindow': {
                close: this.refreshOnClose
            },
            'companyAddWindow button[action=save]': {
                click: this.addCompany
            },
            'companyEditWindow button[action=save]': {
                click: this.updateCompany
            },
            'companyForm filefield': {
                change: this.uploadFile
            },
            'companyForm': {
                deletefile: this.deleteFile
            },
            // refresh actions
            'companyAddWindow': {
                close: this.refreshOnClose
            },
            'companyEditWindow': {
                close: this.refreshOnClose
            }
        });
    },

    newCompany: function () {
        var win = Ext.widget('companyAddWindow');
        win.show();
    },

    addCompany: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.company.model.Company', values),
            grid = this.getCompanyGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            if (win.down('form').data.documents) {
                rec.data.documents = win.down('form').data.documents;
            }
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
            Ext.Msg.alert(l('warning'), l('error.incorrect_form_data'));
        }
    },

    saveDiscount: function (button) {
        var win = button.up('companyDiscountWindow'),
            form = win.down('form').getForm(),
            values = form.getValues();
        if (form.isValid()) {
            Pms.Ajax.request({
                url: 'rest/company/' + values.id + '/discount',
                jsonData: {discount: parseInt(values.discount)},
                method: 'PUT',
                success: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('discountSaved'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            })
        } else {
            Ext.Msg.alert(l('warning'), l('error.incorrect_form_data'));
        }
    },

    discount: function (button) {
        var view = button.up('companyViewport'),
            grid = view.down('companyGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                win = Ext.widget('companyDiscountWindow');

            win.down('form').loadRecord(rec);
            win.show();
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    editCompany: function (button, e) {
        var win = button.up('companyViewport'),
            grid = win.down('companyGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                win = Ext.widget('companyEditWindow', {data: rec.data}),
                form = win.down('companyForm');

            form.loadRecord(rec);
            win.show();
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    updateCompany: function (button) {
        var win = button.up('companyEditWindow'),
            form = win.down('form').getForm(),
            rec = form.getRecord(),
            values = form.getValues(),
            grid = this.getCompanyGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            if (win.down('form').data.documents) {
                rec.data.documents = win.down('form').data.documents;
            }
            rec.set(values);
            store.sync({
                success: function () {
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('warning'), l('error.incorrect_form_data'));
        }
    },

    deleteCompany: function (button, e) {
        var win = button.up('companyViewport'),
            grid = win.down('companyGrid'),
            store = grid.getStore(),
            rec = grid.getSelectionModel().getSelection()[0],
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            Ext.Msg.confirm(l('deletingRoom'), l('confirmation'), function (btn) {
                if (btn === 'yes') {
                    store.remove(rec);
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
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    uploadFile: function (field) {
        var uploadForm = field.up('form').getForm(),
            companyForm = field.up('window').down('companyForm');

        uploadForm.submit({
            scope: this,
            params: {
                type: 'SCAN'
            },
            success: function (s, res) {
                companyForm.data.documents.push(res.result.content);
                companyForm.documentsData.push([res.result.content]);
                companyForm.down('dataview').store.load();
            }
        })
    },

    deleteFile: function (dataview, rec) {
        var me = this;

        Pms.Ajax.request({
            url: 'rest/document/doc/' + rec.data.file,
            method: 'DELETE',
            success: function (response) {
                companyForm.data.documents.splice(rec.index, 1);
                companyForm.documentsData.splice(rec.index, 1);
                companyForm.down('dataview').store.load();
                Pms.App.showNotification({
                    message: l('company.photo.deletedSuccess'),
                    icon: Pms.notificationOk
                });
            },
            failure: function () {
                Pms.App.showNotification({
                    message: l('company.photo.deletedFailure')
                });
            }
        });
    },


    refreshOnClose: function () {
        var companyViewport = this.getCompanyViewport();
        if (!Ext.isEmpty(companyViewport)) {
            var companyGrid = companyViewport.down('companyGrid');
            if (!Ext.isEmpty(companyGrid)) {
                companyGrid.getStore().reload();
                companyGrid.getSelectionModel().clearSelections();
            }
        }
    }
//    buildItems: function () {
//        if (!Ext.isEmpty(this.win)) {
//            var view = Ext.create("Pms.modules.company.view.Viewport");
//            this.win.add(view);
//        }
//    }
});