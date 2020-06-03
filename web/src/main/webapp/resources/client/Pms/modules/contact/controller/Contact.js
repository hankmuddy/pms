Ext.define("Pms.modules.contact.controller.Contact", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.contact.view.Viewport",
        "Pms.modules.contact.view.ContactGrid",
        "Pms.modules.contact.view.ContactForm",
        "Pms.modules.contact.view.ContactAddWindow",
        "Pms.modules.contact.view.ContactEditWindow",
        'Pms.modules.company.view.CompanyEditWindow'
    ],

    stores: [
        "Pms.modules.contact.store.Contact"
    ],

    models: [
        "Pms.modules.contact.model.Contact"
    ],

    refs: [
        {
            ref: 'companyEditWindow',
            selector: 'companyEditWindow'
        },
        {
            ref: 'contactGrid',
            selector: 'contactGrid'
        }
    ],

    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.create("Pms.modules.contact.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            'companyEditWindow button[action=new]': {
                click: this.newContact
            },
            'companyEditWindow button[action=edit]': {
                click: this.editContact
            },
            'contactGrid': {
                itemdblclick: this.edit2
            },
            'companyEditWindow menuitem[action=commit]': {
                click: function () {
                    Ext.Msg.alert(l('warning'), l('cantCommitContact'));
                }
            },
            'companyEditWindow button[action=delete]': {
                click: this.deleteContact
            },
            'contactAddWindow button[action=save]': {
                click: this.addContact
            },
            'contactEditWindow button[action=save]': {
                click: this.updateContact
            },
            'contactAddWindow': {
                close: this.refreshOnClose
            },
            'contactEditWindow': {
                close: this.refreshOnClose
            }
        });
    },

    newContact: function (button) {
        var companyWin = button.up('window'),
            companyForm = companyWin.down('form'),
            values = companyForm.getValues(),
            companyId = {company: values.id },
            win = Ext.widget('contactAddWindow'),
            form = win.down('form');

        form.getForm().setValues(companyId);
        win.show();
    },

    addContact: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.contact.model.Contact', values),
            grid = this.getContactGrid(),
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
        } else {
            Ext.Msg.alert(l('error'), l('error.incorrect_form_data'));
        }
    },

    editContact: function (button, e) {
        var view = button.up('companyEditWindow'),
            grid = view.down('contactGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var record = selected[0],
                win = Ext.widget('contactEditWindow');
            record.data.company = record.data.company.id;
            win.down('form').loadRecord(record);
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    edit2: function (grid, record) {
        var win = Ext.widget('contactEditWindow');
        record.data.company = record.data.company.id;
        win.down('form').loadRecord(record);
    },

    updateContact: function (button, e) {
        var win = button.up('contactEditWindow'),
            form = win.down('contactForm').getForm(),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getContactGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.add(record);
            store.sync({
                success: function () {
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

    refreshOnClose: function (win) {
        var companyEditWindow = this.getCompanyEditWindow();
        if (!Ext.isEmpty(companyEditWindow)) {
            var contactGrid = companyEditWindow.down('contactGrid');
            if (!Ext.isEmpty(contactGrid)) {
                contactGrid.getStore().reload();
                contactGrid.getSelectionModel().clearSelections();
            }
        }
    },

    deleteContact: function (button, e) {
        var me = this,
            win = button.up('companyEditWindow'),
            grid = win.down('contactGrid'),
            selected = grid.getSelectionModel().getSelection(),
            store = grid.getStore();

        var record;
        if (selected.length > 0) {
            record = selected[0];
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return
        }

        Ext.Msg.confirm(l('deleteContact'), l('confirmation'), function (btn) {
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
    },

//    buildItems: function () {
//        if (!Ext.isEmpty(this.win)) {
//            var view = Ext.create("Pms.modules.contact.view.Viewport");
//            this.win.add(view);
//        }
//    }
});