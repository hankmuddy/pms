Ext.define('Pms.modules.person.controller.Person', {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.person.view.Viewport',
        'Pms.modules.person.view.PersonEditWindow',
        'Pms.modules.person.view.PersonAddWindow',
        'Pms.modules.person.view.PersonGrid',
        'Pms.modules.person.view.PersonForm',
        'Pms.modules.person.view.PersonBookForm',
        'Pms.modules.person.view.PersonFilterForm',
        'Pms.modules.person.view.groupPersonGrid',
        'Pms.modules.child.view.ChildBookForm',
        'Pms.modules.group.view.GroupGrid',
        'Pms.modules.booking.view.consoleGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseAddWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseAddTabs',
        'Pms.modules.bill.view.BillGrid',
        'Pms.modules.child.view.ChildFilterForm',
        'Pms.modules.child.view.ChildGrid',
        'Pms.modules.payment.view.PaymentGrid',
        'Pms.modules.person.view.PersonDiscountWindow',
        'Pms.modules.person.view.ImportContactsWindow',
        'Pms.modules.person.view.ImportContactsForm',
        'Pms.modules.person.view.SendEmailWindow'
    ],
    stores: [
        'Pms.modules.person.store.Person',
        'Pms.modules.person.store.Country',
        'Pms.modules.person.store.Language'
    ],
    models: [
        'Pms.modules.person.model.Person'
    ],
    refs: [
        {ref: 'personViewport', selector: 'personViewport'},
        {ref: 'personGrid', selector: 'personGrid'},
        {ref: 'groupPersonGrid', selector: 'groupPersonGrid'},

        {ref: 'groupGrid', selector: 'groupGrid'},

        {ref: 'groupRoomUseEditWindow', selector: 'groupRoomUseEditWindow'}
    ],

    extravailable: true,

    subControllers: [
        'Pms.modules.bill.controller.Bill',
        'Pms.modules.payment.controller.Payment',
        'Pms.modules.child.controller.Child',
        'Pms.modules.booking.controller.Booking'
    ],

    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.create('Pms.modules.person.view.Viewport');
            this.buildItems(view);
        }

        this.control({
            // Person -----------------------
            'personViewport button[action=new]': {
                click: this.newPerson
            },
            'personGrid button[action=edit]': {
                click: this.routeAction
            },
            'personGrid': {
                itemdblclick: function (grid, record, item, index, e, eOpts) {
                    this.edit(grid, index, null);
                }
            },
            'groupPersonGrid': {
                itemdblclick: this.editGroupMember
            },
            'personViewport button[action=delete]': {
                click: this.deletePerson
            },
            'personViewport button[action=discount]': {
                click: this.discount
            },
            'personViewport button[action=email]': {
                click: this.email
            },
            'personViewport button[action=import]': {
                click: this.import
            },
            'importContactsWindow button[action=import]': {
                click: this.importContacts
            },
            'sendEmailWindow button[action=send]': {
                click: this.sendEmail
            },
            'personAddWindow button[action=save]': {
                click: this.addPerson
            },
            'personEditWindow button[action=save]': {
                click: this.updatePerson
            },
            'personDiscountWindow button[action=save]': {
                click: this.saveDiscount
            },
            'personAddWindow filefield': {
                change: this.uploadFile
            },
            'personEditWindow filefield': {
                change: this.uploadFile
            },
            // refresh actions
            'personDiscountWindow': {
                close: this.refreshOnClose
            },
            'personAddWindow': {
                close: this.refreshOnClose
            },
            'personEditWindow': {
                close: this.refreshOnClose
            },
            'importContactsWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseEditWindow button[action=edit-person]': {
                click: this.editPersonFromBooking
            }
        });
    },

    // Person -----------------------
    newPerson: function () {
        var win = Ext.widget('personAddWindow');
        win.show();
    },

    addPerson: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getForm().getFieldValues(),
            rec = Ext.create('Pms.modules.person.model.Person', values);
        var grid = this.getPersonGrid(),
            store = grid.getStore(),
            fileField = form.getForm().findField('file'),
            documentsForm = form.down('form').getForm();

        if (form.isValid()) {
            if (this.personDocumentFile) {
                rec.data.identity = this.personDocumentFile;
                this.personDocumentFile = null;
            }
            store.add(rec);
            store.sync({
                success: function () {
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        }
        else {
            Ext.Msg.alert(l('error'), l('error.incorrect_form_data'));
        }
    },

    edit: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        var win = Ext.widget('personEditWindow', {data: rec}),
            form = win.down('form');

        form.loadRecord(row);
        win.show();
    },

    editGroupMember: function (grid, memberRec, item, index, e, eOpts) {
        var me = this,
            store = grid.getStore(),
            win = Ext.widget('personEditWindow', {data: memberRec.data.person}),
            form = win.down('form'),
            rec = Ext.create('Pms.modules.person.model.Person', memberRec.data.person);

        form.loadRecord(rec);
        win.show();
    },

    updatePerson: function (button) {
        var win = button.up('personEditWindow'),
            form = win.down('personForm').getForm(),
            values = form.getValues(),
            grid = this.getPersonGrid(),
            record = form.getRecord(),
            store,
            isFromBooking = false;
        if (values.email == '') values.email = null;
        if (!grid) {
            store = Ext.create('Pms.modules.person.store.Person').load();
            isFromBooking = true;
        }
        else store = grid.getStore();

        values.id = parseInt(values.id);
        if (form.isValid()) {
            if (this.personDocumentFile) {
                record.data.identity = this.personDocumentFile;
                this.personDocumentFile = null;
            }
            record.set(values);
            if (isFromBooking) store.add(record);
            store.sync({
                success: function () {
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('error.recordNotSaved'));
        }
    },

    refreshOnClose: function () {
        var personViewport = this.getPersonViewport();
        if (!Ext.isEmpty(personViewport)) {
            var personGrid = personViewport.down('personGrid');
            if (!Ext.isEmpty(personGrid)) {
                personGrid.getStore().reload();
                personGrid.getSelectionModel().clearSelections();
            }
        }
        var groupRoomUseEditWindow = this.getGroupRoomUseEditWindow();
        if (!Ext.isEmpty(groupRoomUseEditWindow)) {
            var groupPersonGrid = groupRoomUseEditWindow.down('groupPersonGrid');
            if (!Ext.isEmpty(groupPersonGrid)) {
                groupPersonGrid.getStore().reload();
                groupPersonGrid.getSelectionModel().clearSelections();
            }
        }
    },

    deletePerson: function (button) {
        var win = button.up('personViewport'),
            grid = win.down('personGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            for (var i in selected) {
                Ext.MessageBox.confirm(l('deleting'), l('person.confirmDeleting') + ' <b>' + selected[i].data.lastName + ' ' + selected[i].data.firstName + '</b>?', function (btn) {
                    if (btn === 'yes') {
                        store.remove(selected[0]);
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
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    uploadFile: function (field) {
        var documentsForm = field.up('form').getForm(),
            imageField = field.up('fieldset').down('image');

        documentsForm.submit({
            scope: this,
            params: {type: 'SCAN'},
            success: function (s, res) {
                this.personDocumentFile = res.result.content;
                imageField.setSrc(_('imagesUrlPrefix') + this.personDocumentFile);
            }
        })
    },

    discount: function (button) {
        var view = button.up('personViewport'),
            grid = view.down('personGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                win = Ext.widget('personDiscountWindow');

            win.down('form').loadRecord(rec);
            win.show();
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    import: function () {
        var win = Ext.create('Pms.modules.person.view.ImportContactsWindow');
        win.show();
    },
    importContacts: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getForm().getFieldValues(),
            fileForm = form.down('form'),
            struct = JSON.stringify(values);

        fileForm.submit({
            url: 'import',
            params: {struct: struct},
            scope: this,
            success: function (s, res) {
                win.close();
                Pms.App.showNotification({
                    message: l('import.successMessage'),
                    icon: Pms.notificationOk
                });
            },
            failure: function (s, res) {
                var msg = JSON.parse(arguments[1].response.responseText)[0].code;
                Pms.App.showNotification({
                    message: l(msg),
                    tittle: l('import.errorMessage')
                });
            }
        })
    },
    email: function (button) {
        var win = button.up('personViewport'),
            grid = win.down('personGrid'),
            selected = grid.getSelectionModel().getSelection();
        if (!selected.length) {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
        var email = selected[0].data.email,
            name = selected[0].data.firstName + ' ' + selected[0].data.lastName;
        var win = Ext.widget('sendEmailWindow', {
            email: email ? email : '',
            name: name
        });
        win.show();
    },
    sendEmail: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getForm().getFieldValues();
        Pms.Ajax.request({
            url: 'rest/mail',
            method: 'POST',
            jsonData: {
                to: values.email,
                subject: values.theme,
                content: values.text
            },
            success: function(){
                win.close();
                Pms.App.showNotification({
                    message: l('bookingView.sent'),
                    icon: Pms.notificationOk
                })
            }
        })
    },

    saveDiscount: function (button) {
        var win = button.up('personDiscountWindow'),
            form = win.down('form').getForm(),
            values = form.getValues();
        if (form.isValid()) {
            Pms.Ajax.request({
                url: 'rest/adult/' + values.id + '/discount',
                jsonData: {discount: parseInt(values.discount)},
                method: 'PUT',
                success: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('discountSaved'),
                        icon: Pms.notificationOk
                    })
                }
            })
        } else {
            Pms.App.showNotification({
                message: l('discountNotSaved'),
                icon: Pms.notificationOk
            })
        }
    },

    editPersonFromBooking: function (btn) {
        var grid = btn.up('window').down('#groupPersonGridBooking');
        var rows = grid.getSelectionModel().getSelection();
        if (rows.length) {
            this.editGroupMember(grid, rows[0], null, rows)
        }
        else {
            Ext.Msg.alert(l('error'), l('warning.selectRecord'));
        }
    },

//    buildItems: function () {
//        if (typeof(this.win) !== "undefined") {
//            var view = Ext.create('Pms.modules.person.view.Viewport');
//            this.win.add(view);
//        }
//    }
});
