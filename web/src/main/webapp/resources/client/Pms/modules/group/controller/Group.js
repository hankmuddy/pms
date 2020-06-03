Ext.define("Pms.modules.group.controller.Group", {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.group.view.Viewport',
        'Pms.modules.group.view.GroupEditWindow',
        'Pms.modules.group.view.GroupAddWindow',
        'Pms.modules.group.view.GroupGrid',
        'Pms.modules.group.view.GroupForm',
        'Pms.modules.contact.view.ContactEditWindow',
        'Pms.modules.contact.view.ContactAddWindow',
        'Pms.modules.contact.view.ContactGrid',
        'Pms.modules.contact.view.ContactForm',
        'Pms.modules.group.view.GroupBookingSearchGrid',
        'Pms.modules.group.view.GroupBookingSearchFilterForm'
    ],
    stores: [
        'Pms.modules.group.store.Group',

        'Pms.modules.contact.store.Contact'
    ],
    models: [
        'Pms.modules.group.model.Group',
        'Pms.modules.contact.model.Contact'
    ],
    refs: [
        {ref: 'groupGrid', selector: 'groupGrid'},
        {ref: 'groupStore', selector: 'groupStore'},

        {ref: 'contactGrid', selector: 'contactGrid'},
        {ref: 'contactStore', selector: 'contactStore'}
    ],

    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.create("Pms.modules.group.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            // Group -----------------------
            'groupViewport button[action=new-group]': {
                click: this.newGroup
            },
            'groupAddWindow button[action=save-group]': {
                click: this.addGroup
            },
            'groupGrid': {
                itemdblclick: this.editGroup
            },
            'groupEditWindow button[action=save-group]': {
                click: this.updateGroup
            },
            'groupViewport button[action=delete-group]': {
                click: this.deleteGroup
            },

            // Contact -----------------------
            'groupEditWindow button[action=new-contact]': {
                click: this.newContact
            },
            'contactAddWindow button[action=save-contact]': {
                click: this.addContact
            },
            'contactGrid': {
                itemdblclick: this.editContact
            },
            'contactEditWindow button[action=save-contact]': {
                click: this.updateContact
            },
            'groupEditWindow button[action=delete-contact]': {
                click: this.deleteContact
            }
        });
    },

    // Group -----------------------
    newGroup: function () {
        var win = Ext.widget('groupAddWindow');
        win.show();
    },

    addGroup: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.group.model.Group', values),
            grid = this.getGroupGrid(),
            store = grid.getStore();

        store.add(rec);

        win.close();
    },

    editGroup: function (grid, record) {
        var win = Ext.widget('groupEditWindow'),
            groupStore = grid.getStore();

        win.down('form').loadRecord(record);

        var contactGrid = win.down('grid'),
            contactStore = contactGrid.getStore().load({
                params: {
                    filter: {
                        property_group: {
                            equal: record.data['id']
                        }
                    }
                }
            });
    },

    updateGroup: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues();

        record.set(values);
        win.close();
    },

    deleteGroup: function (grid, record) {
        var grid = this.getGroupGrid(),
            store = grid.getStore(),
            record = grid.getSelectionModel().getSelection();

        Ext.MessageBox.confirm(l('group.Delete'), l('confirmation'), function (btn) {
            if (btn === 'yes') {
                store.remove(record);
            }
        });
    },

    // Contact -----------------------
    newContact: function (button) {
        // get group ID and put it in property_group field in contactForm
        var groupWin = button.up('window'),
            groupForm = groupWin.down('form'),
            values = groupForm.getValues(),
            groupId = { property_group: values.id },
            win = Ext.widget('contactAddWindow'),
            form = win.down('form');

        form.getForm().setValues(groupId);

        win.show();
    },

    addContact: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.contact.model.Contact', values),
            grid = this.getContactGrid(),
            store = grid.getStore();

        store.add(rec);
        win.close();
    },

    editContact: function (grid, record) {
        var win = Ext.widget('contactEditWindow'),
            form = win.down('form');

        form.loadRecord(record);

        // get group ID and put it in property_group field in contactForm
        var groupWin = grid.up('window'),
            groupForm = groupWin.down('form'),
            values = groupForm.getValues(),
            groupId = { property_group: values.id };

        form.getForm().setValues(groupId);
    },

    updateContact: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues();

        record.set(values);
        win.close();
    },

    deleteContact: function (grid, record) {
        var grid = this.getContactGrid(),
            store = grid.getStore(),
            record = grid.getSelectionModel().getSelection();

        Ext.MessageBox.confirm(l('group.Delete'), l('confirmation'), function (btn) {
            if (btn === 'yes') {
                store.remove(record);
            }
        });
    },

//    buildItems: function () {
//        if (typeof(this.win) !== "undefined") {
//            var view = Ext.create("Pms.modules.group.view.Viewport");
//            this.win.add(view);
//        }
//    }
});