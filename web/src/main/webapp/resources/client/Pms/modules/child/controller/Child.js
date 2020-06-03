Ext.define('Pms.modules.child.controller.Child', {
    extend: "Pms.abstract.Controller",
    views: [
        'Pms.modules.person.view.Viewport',
        'Pms.modules.child.view.Viewport',
        'Pms.modules.child.view.ChildEditWindow',
        'Pms.modules.child.view.ChildAddWindow',
        'Pms.modules.child.view.ChildGrid',
        'Pms.modules.child.view.ChildForm',
        'Pms.modules.child.view.groupChildGrid',

        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow',
    ],
    stores: ['Pms.modules.child.store.Child'],
    models: ['Pms.modules.child.model.Child'],
    refs: [
        {ref: 'childViewport', selector: 'childViewport'},
        {ref: 'childGrid', selector: 'childGrid'},
        {ref: 'personViewport', selector: 'personViewport'},
        {ref: 'groupRoomUseEditWindow', selector: 'groupRoomUseEditWindow'},
        {ref: 'groupChildGrid', selector: 'groupChildGrid'}
    ],

    extravailable: true,

    init: function (contr, subController) {
        // this.callParent(arguments);

        if (!subController) {
            var view = Ext.create('Pms.modules.child.view.Viewport');
            this.buildItems(view);
        }


        this.control({
            // Child -----------------------
            'personViewport button[action=new-child]': {
                click: this.newChild
            },
            'personViewport button[action=edit-child]': {
                click: this.editChild
            },
            'childGrid': {
                itemdblclick: this.editChild
            },
            'childAddWindow button[action=save]': {
                click: this.addChild
            },
            'childEditWindow button[action=save]': {
                click: this.updateChild
            },
            'personViewport menuitem[action=commit-child]': {
                click: function () {
                    Ext.Msg.alert(l('commiting'), l('cantCommitChild'));
                }
            },
            'personViewport button[action=delete-child]': {
                click: this.deleteChild
            },
            'childAddWindow': {
                close: this.refreshOnClose
            },
            'childEditWindow': {
                close: this.refreshOnClose
            },
            'childAddWindow filefield': {
                change: this.uploadFile
            },
            'childEditWindow filefield': {
                change: this.uploadFile
            },
            'groupChildGrid button[action=edit-child]': {
                click: this.editChildFromBooking
            }
        });
    },

    // Person -----------------------
    newChild: function () {
        Ext.widget('childAddWindow');
    },

    addChild: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.child.model.Child', values);

        var grid = this.getChildGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            if (this.childDocumentFile) {
                rec.data.identity = this.childDocumentFile;
                this.childDocumentFile = null;
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
            Ext.Msg.alert(l('error'), l('childSaveError'));
        }
    },

    editChild: function (button, e) {
        var win = button.up('window'),
            grid = win.down('childGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                win = Ext.widget('childEditWindow', {data: rec.data});

            win.down('form').loadRecord(rec);
            win.show();
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },
    editGroupMember: function (grid, memberRec, item, index, e, eOpts) {
        var me = this,
            store = grid.getStore(),
            win = Ext.widget('childEditWindow', {data: memberRec.data.person}),
            form = win.down('form'),
            rec = Ext.create('Pms.modules.child.model.Child', memberRec.data.person);

        form.loadRecord(rec);
        win.show();
    },
    updateChild: function (button) {
        var win = button.up('childEditWindow'),
            form = win.down('childForm').getForm(),
            values = form.getValues(),
            grid = this.getChildGrid(),
            record = form.getRecord(),
            store = grid.getStore(),
            isFromBooking = false;
        if (!grid.isVisible()) {
            store = Ext.create('Pms.modules.child.store.Child').load();
            isFromBooking = true;
        }

        values.id = parseInt(values.id);
        if (form.isValid()) {
            if (this.childDocumentFile) {
                values.identity = this.childDocumentFile;
                this.childDocumentFile = null;
            }
            record.set(values);
            if (isFromBooking) store.add(record);
            store.sync({
                success: function () {
                    store.reload();
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        }
        else {
            Ext.Msg.alert(l('error'), l('checkFormFields'));
        }
    },

    refreshOnClose: function () {
        var personViewport = this.getPersonViewport();
        if (!Ext.isEmpty(personViewport)) {
            var childGrid = personViewport.down('childGrid');
            if (!Ext.isEmpty(childGrid)) {
                childGrid.getStore().reload();
                childGrid.getSelectionModel().clearSelections();
            }
        }
        var groupRoomUseEditWindow = this.getGroupRoomUseEditWindow();
        if (!Ext.isEmpty(groupRoomUseEditWindow)) {
            var childGroupGrid = groupRoomUseEditWindow.down('childGrid');
            if (!Ext.isEmpty(childGroupGrid)) {
                childGroupGrid.getStore().reload();
                childGroupGrid.getSelectionModel().clearSelections();
            }
        }
        var groupChildGrid = this.getGroupChildGrid();
        if (!Ext.isEmpty(groupChildGrid)) {
            groupChildGrid.getStore().reload();
            groupChildGrid.getSelectionModel().clearSelections();
        }
    },

    deleteChild: function (button) {
        var me = this,
            win = button.up('window'),
            grid = win.down('childGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            for (var i in selected) {
                Ext.MessageBox.confirm(l('deleting'), l('confirmDeleteChild') + ' <b>' + selected[i].data.lastName + ' ' + selected[i].data.firstName + '</b>?', function (btn) {
                    if (btn === 'yes') {
                        store.remove(selected[0]);
                        store.sync();
                    }
                }, this);
            }
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    uploadFile: function (field) {
        var documentsForm = field.up('form').getForm(),
            imageField = field.up('window').down('image');

        documentsForm.submit({
            scope: this,
            params: {type: 'SCAN'},
            success: function (s, res) {
                this.childDocumentFile = res.result.content;
                imageField.setSrc(_('imagesUrlPrefix') + this.childDocumentFile);
            }
        })
    },

    // Child -----------------------
    editChildFromBooking: function (btn) {
        var grid = btn.up('window').down('#childGridBooking');
        var rows = grid.getSelectionModel().getSelection();
        if (rows.length) {
            this.editGroupMember(grid, rows[0], null, rows)
        }
    },

//    buildItems: function () {
//        if (!Ext.isEmpty(this.win)) {
//            var view = Ext.create('Pms.modules.child.view.Viewport');
//            this.win.add(view);
//        }
//    }
});