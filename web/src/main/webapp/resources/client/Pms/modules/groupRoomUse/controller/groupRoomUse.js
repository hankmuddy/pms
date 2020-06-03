Ext.define('Pms.modules.groupRoomUse.controller.groupRoomUse', {
    extend: 'Pms.abstract.Controller',

    requires: ['Ext.ux.grid.Printer'],

    views: [
        'Pms.modules.groupRoomUse.view.Viewport',
        'Pms.modules.groupRoomUse.view.schPanel',
        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseAddWindow',
        'Pms.modules.groupRoomUse.view.groupBookingWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseConfirmWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseRepairWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseIncomeWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseOutgoWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseMoveWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseRefuseWindow',
        'Pms.modules.groupRoomUse.view.RefuseInfoWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseAddTabs',
        'Pms.modules.person.view.groupPersonGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseConfirmForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseRepairForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseEditForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseGroupEditForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseIncomeForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseOutgoForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseMoveForm',
        'Pms.modules.groupRoomUse.view.groupRoomUseRefuseForm',
        'Pms.modules.groupRoomUse.view.roomUseFilterForm',
        'Pms.modules.groupRoomUse.view.groupBookingAddWindow',
        'Pms.modules.groupRoomUse.view.groupBookingForm',
        'Pms.modules.groupRoomUse.view.groupBookingGrid',
        'Pms.modules.groupRoomUse.view.addGuestWindow',
        'Pms.modules.groupRoomUse.view.bookDataGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseGroupAddWindow',
        'Pms.modules.groupRoomUse.view.RoomUseRoomGrid',
        'Pms.modules.groupRoomUse.view.addRoomWindow',
        'Pms.modules.person.view.PersonEditWindow',
        'Pms.modules.person.view.PersonDetailWindow',
        'Pms.modules.person.view.PersonForm',
        'Pms.modules.person.view.PersonBookForm',
        'Pms.modules.person.view.PersonRoomIncomeWindow',
        'Pms.modules.child.view.ChildGrid',
        'Pms.modules.child.view.ChildBookForm',
        'Pms.modules.child.view.groupChildGrid',
        'Pms.modules.company.view.CompanyBookForm',
        'Pms.modules.company.view.CompanyDetailWindow',
        'Pms.modules.company.view.CompanyForm',
        'Pms.modules.company.view.CompanyBookForm',
        'Pms.modules.catalog.view.CatalogOrderGrid',
        'Pms.modules.room.view.freeRoomGrid',
        'Pms.modules.room.view.RoomGrid',
        'Pms.modules.room.view.GroupRoomUseRoomGrid',
        'Pms.modules.groupRoomUse.view.ccViewWindow'
    ],
    stores: [
        'Pms.modules.groupRoomUse.store.Resource',
        'Pms.modules.groupRoomUse.store.Event',
        'Pms.modules.group.store.Group',
        'Pms.modules.roomUse.store.roomUse',
        'Pms.modules.person.store.Person',
        'Pms.modules.child.store.Child',
        'Pms.modules.living.store.Living'
    ],
    models: [
        'Pms.modules.groupRoomUse.model.Resource',
        'Pms.modules.groupRoomUse.model.Event',
        'Pms.modules.group.model.Group',
        'Pms.modules.roomUse.model.roomUse',
        'Pms.modules.catalog.model.Catalog',
        'Pms.modules.room.model.Room'
    ],
    refs: [
        {ref: 'schPanel', selector: 'schPanel'},
        {ref: 'resourceStore', selector: 'resourceStore'},
        {ref: 'eventStore', selector: 'eventStore'},
        {ref: 'defPayerStore', selector: 'defPayerStore'},
        {ref: 'groupRoomUseViewport', selector: 'groupRoomUseViewport'},
        {ref: 'groupRoomUseEditWindow', selector: 'groupRoomUseEditWindow'},
        {ref: 'groupPersonGrid', selector: 'groupPersonGrid'},
        {ref: 'groupChildGrid', selector: 'groupChildGrid'},
        {ref: 'roomUseRoomGrid', selector: 'roomUseRoomGrid'},
        {ref: 'groupRoomUseOutgoForm', selector: 'groupRoomUseOutgoForm'},
        {ref: 'groupRoomUseEditForm', selector: 'groupRoomUseEditForm'},
        {ref: 'groupRoomUseMoveForm', selector: 'groupRoomUseMoveForm'},
        {ref: 'groupRoomUseRefuseForm', selector: 'groupRoomUseRefuseForm'},
        {ref: 'groupRoomUseRepairForm', selector: 'groupRoomUseRepairForm'},
        {ref: 'groupRoomUseAddTabs', selector: 'groupRoomUseAddTabs'},

        {ref: 'personStore', selector: 'personStore'},
        {ref: 'personForm', selector: 'personForm'},

        {ref: 'groupRoomUseRoomGrid', selector: 'groupRoomUseRoomGrid'},
        {ref: 'groupStore', selector: 'groupStore'},
        {ref: 'groupGrid', selector: 'groupGrid'}
    ],

    subControllers: [
        'Pms.modules.bill.controller.Bill',
        'Pms.modules.payment.controller.Payment',
        'Pms.modules.serviceUse.controller.serviceUse',
        'Pms.modules.person.controller.Person',
        'Pms.modules.child.controller.Child'
    ],

    extravailable: true,

    init: function (contr, subController) {

        if (!subController) {
            this.buildItems();
        }

        this.control({
            'addGuestWindow #addGuest': {
                activate: this.addGuestTab
            },
            'addGuestWindow #addChild': {
                activate: this.addChildTab
            },
            'groupRoomUseAddWindow button[action=save]': {
                click: this.addBooking
            },
            'groupBookingAddWindow button[action=save]': {
                click: this.addBooking
            },
            'groupRoomUseGroupAddWindow button[action=save]': {
                click: this.addBooking
            },
            'groupRoomUseGroupAddWindow': {
                close: this.refreshOnClose
            },
            'personBookForm button[action=detail]': {
                click: this.personDetailWindow
            },
            'companyBookForm button[action=detail]': {
                click: this.companyDetailWindow
            },
            'personDetailWindow button[action=update]': {
                click: this.personUpdate
            },
            'personDetailWindow button[action=updateInfo]': {
                click: this.personUpdateInfo
            },
            'groupRoomUseEditWindow childGrid': {
                itemdblclick: this.childDetailWindow
            },
            'schPanel grid': {
                eventdblclick: this.editBooking,
                eventresizeend: this.eventResize
            },
            'schPanel button[action=new-group-booking]': {
                click: this.newGroupBooking
            },
            'schPanel button[action=new-income]': {
                click: this.newIncome
            },
            'schPanel button[action=new-booking]': {
                click: this.newBooking
            },
            'bookingViewport button[action=new-booking]': {
                click: this.newBooking
            },
            'bookingViewport button[action=new-income]': {
                click: this.newIncome
            },
            'bookingViewport button[action=new-group-booking]': {
                click: this.newGroupBooking
            },
            'schPanel button[action=new-repair]': {
                click: this.newRepair
            },
            'schPanel button[action=group-booking]': {
                click: this.newBooking
            },
            'schPanel button[action=search-booking]': {
                click: this.searchBooking
            },
            'schPanel button[action=clients]': {
                click: this.clients
            },
            'schPanel button[action=companies]': {
                click: this.companies
            },
            'schPanel button[action=console]': {
                click: this.console
            },
            'groupRoomUseEditWindow button[action=save]': {
                click: this.updateBooking
            },
            'groupRoomUseMoveWindow button[action=save]': {
                click: this.moveBooking
            },
            'groupRoomUseRefuseWindow button[action=save]': {
                click: this.refuseBooking
            },
            'groupRoomUseConfirmWindow button[action=save]': {
                click: this.confirm
            },
            'groupRoomUseIncomeWindow button[action=save]': {
                // click: function() { alert('income'); }
                click: this.income
            },
            'groupRoomUseOutgoWindow button[action=save]': {
                click: this.outgo
            },
            'groupRoomUseRepairWindow button[action=save]': {
                click: this.repair
            },
            'groupRoomUseRepairWindow button[action=update]': {
                click: this.repairUpdate
            },
            'serviceUseGroupGrid button[action=discount]': {
                click: this.discount
            },
            'groupGrid': {
                itemdblclick: this.editBooking
            },
            'groupRoomUseAddWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseEditWindow': {
                close: this.refreshOnClose
            },
            'groupBookingAddWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseIncomeWindow': {
                close: this.refreshOnClose,
                incomeWithGuest: this.income
            },
            'groupRoomUseOutgoWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseConfirmWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseRepairWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseRefuseWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseMoveWindow': {
                close: this.refreshOnClose
            },
            'addGuestWindow button[action=save]': {
                click: this.addGuest
            },
            'personDetailWindow filefield': {
                change: this.uploadFile
            },
            'personRoomIncomeWindow button[action=income]': {
                click: this.incomePerson
            },
            'addRoomWindow button[action=save]': {
                click: this.updateGroupBooking
            }
        });
    },

    newGroupBooking: function () {
        var win = Ext.widget('groupRoomUseGroupAddWindow');
        win.show();
    },
    newIncome: function (button) {
        var win = Ext.widget('groupRoomUseAddWindow'),
            gruForm = win.down('groupRoomUseForm'),
            gruData = {status: 'BOOKING_FREE', startDate: new Date()};
        gruForm.getForm().setValues(gruData);
        win.show();
    },

    newBooking: function (button) {
        var win = Ext.widget('groupRoomUseAddWindow'),
            gruForm = win.down('groupRoomUseForm'),
            gruData = {status: 'BOOKING_FREE', startDate: new Date()};

        gruForm.getForm().setValues(gruData);
        gruForm.down('combobox[name=baseRoom]').getStore().reload();
        win.show();
    },

    newRepair: function (button) {
        var win = Ext.widget('groupRoomUseRepairWindow');
        win.show();
    },

    groupBooking: function (button) {
        var win = Ext.widget('groupBookingWindow'),
            gruForm = win.down('groupRoomUseForm'),
            gruData = {status: 'BOOKING_FREE'};

        gruForm.getForm().setValues(gruData);
        win.show();
    },

    searchBooking: function (button) {
        Pms.App.runApplication({
            initialConfig: {
                text: l('searchBookings'),
                klass: 'Pms.modules.booking.controller.Booking'
            }
        });
    },

    clients: function (button) {
        Pms.App.runApplication({
            initialConfig: {
                text: l('clients'),
                klass: 'Pms.modules.person.controller.Person'
            }
        });
    },

    companies: function (button) {
        Pms.App.runApplication({
            initialConfig: {
                text: l('companies'),
                klass: 'Pms.modules.company.controller.Company'
            }
        });
    },

    console: function (button) {
        Pms.App.runApplication({
            initialConfig: {
                text: l('console'),
                klass: 'Pms.modules.console.controller.Console'
            }
        });
    },

    addGuestTab: function (button) {
        var win = button.up('window'),
            tabPanel = win.down('tabpanel');

        function addNewTab() {
            tabPanel.tabCount = tabPanel.items.items.length - 2;

            tabPanel.insert(tabPanel.tabCount, {
                title: l('guest') + ' ',// + tabPanel.tabCount,
                closable: true,
                items: [
                    {
                        xtype: 'personBookForm'
                    }
                ],
                listeners: {
                    close: function () {
                        tabPanel.tabCount--;
                        tabPanel.setActiveTab(0);
                    }
                }
            }).show();
            tabPanel.tabCount++;
        }

        addNewTab();
    },

    addChildTab: function (button) {
        var win = button.up('window'),
            tabPanel = win.down('tabpanel');

        function addNewTab() {
            tabPanel.tabCount = tabPanel.items.items.length - 2;
            tabPanel.insert(tabPanel.tabCount, {
                title: l('child') + ' ',// + tabPanel.tabCount,
                closable: true,
                items: [
                    {
                        xtype: 'childBookForm'
                    }
                ],
                listeners: {
                    close: function () {
                        tabPanel.tabCount--;
                        tabPanel.setActiveTab(0);
                    }
                }
            }).show();
            tabPanel.tabCount++;
        }

        addNewTab();
    },

    personDetailWindow: function (button) {
        var win = button.up('window'),
            form = button.up('form'),
            tab = button.up('tab'),
            values = form.getForm().getValues(),
            rec = new Ext.create('Pms.modules.person.model.Person', values),
            detailWin = Ext.widget('personDetailWindow', {
                data: rec.data
            }),
            detailForm = detailWin.down('form');

        rec.data.country = rec.data.country[0];
        if (values.id) {
            var store = Ext.create('Pms.modules.person.store.Person').load({
                url: 'rest/adult/' + values.id,
                callback: function (rec) {
                    detailForm.getForm().setValues(rec[0].data);
                }
            });
        }
        else {
            detailForm.getForm().setValues(rec.data);
            detailForm.down('button[action=update]').action = 'updateInfo';
        }
        detailWin.show();
    },

    personDetailWindow2: function (grid, record) {
        var win = grid.up('window'),
            detailWin = Ext.widget('personDetailWindow'),
            detailForm = detailWin.down('form');

        detailForm.down('button[action=save]').destroy();
        detailForm.getForm().setValues(record.data);
        detailWin.show();
    },

    savePersonDetail: function (button) {
    },

    childDetailWindow: function (grid, record) {
//        var win = grid.up('window'),
//            detailWin = Ext.widget('childViewGrid', {data: record.data});
//
//        detailWin.show();
        Pms.App.showNotification({
            message: record.data.lastName + ' ' + record.data.firstName + '<br />' + Ext.Date.format(new Date(record.data.dob * 1000), 'd/m/Y')
        });
    },

    companyDetailWindow: function (button) {
        var win = button.up('window'),
            form = button.up('form'),
            tab = button.up('tab'),
            values = form.getForm().getValues(),
            detailWin = Ext.widget('companyDetailWindow'),
            detailForm = detailWin.down('form');

        if (values.id) {
            var store = Ext.create('Pms.modules.company.store.Company').load({
                params: {filter: {id: {equal: values.id}}},
                callback: function (records, operation, success) {
                    detailForm.getForm().setValues(records[0].data);
                }
            });
        }
        detailWin.show();
    },

    addBookingGroup: function (tabPanel) {
        var tabs = tabPanel.items.items,
            win = tabPanel.up('window'),
            isGroupMember = win.down('checkboxfield[name=isGroupMember]').value,
            tabCount = (tabs.length == 1) ? 1 : tabs.length - 2, // tabsCount = 1 if there are no `+` tabs or count tabs except `+` tabs
            groupMembers = [],
            master = win.down('toolbar').down('combobox[name=master]').value,
            pov = win.down('toolbar').down('combobox[name=pov]').value,
            customerForm = tabs[0].down('form'),
            gruValues = {},
            personValues = {};
        this.groupMembersValid = true;
        for (var i = 1; i < tabCount; i++) { // loop through tabs for persons and/or company
            var tab = tabs[i],
                form = tab.down('form'),
                values = form.getValues();
            if (form.isValid()) {
                if (values.isChild == "true") {
                    var recChild = Ext.create('Pms.modules.child.model.Child', values);
                    personValues = recChild.getData();
                    if (personValues.id) {
                        personValues = {id: personValues.id, type: 'child'}
                    }
                    else {
                        if (personValues.dob)
                            personValues.dob = personValues.dob.getTime() / 1000;
                    }
                    groupMembers.push({person: personValues});
                }
                else {
                    var recAdult = Ext.create('Pms.modules.person.model.Person', values);
                    personValues = recAdult.getData();
                    if (personValues.id) {
                        personValues = {id: personValues.id, type: 'adult'}
                    }
                    else {
                        for (var k in personValues) {
                            if (personValues[k] == '') personValues[k] = null;
                            else if (personValues[k] && personValues[k].getTime)
                                personValues[k] = personValues[k].getTime() / 1000;
                        }
//                        if (personValues.visaType == "") personValues.visaType = null;
//                        if (personValues.entryType == "") personValues.entryType = null;
//                        if (personValues.dob)
//                            personValues.dob = personValues.dob.getTime() / 1000;
                        if (personValues.country) personValues.country = personValues.country[0];
                    }
                    groupMembers.push({person: personValues});
                }
            }
            else {
                this.groupMembersValid = false;
            }
        }
        gruValues.customerGroup = {};
        if (master == 'masterCompany') {
            var companyForm = customerForm,
                companyValues = companyForm.getValues();
            if (companyValues.customer) {
                customerForm = tabs[1].down('form');
            }
            else customerForm = null;
            if (companyValues.id) {
                companyValues = {id: companyValues.id};
            }
            gruValues.customerGroup.company = companyValues;
        }
        gruValues.customerGroup.includeCustomer = isGroupMember;

        if (customerForm) {
            var recPerson = Ext.create('Pms.modules.person.model.Person', customerForm.getValues());
            gruValues.customerGroup.customer = recPerson.getData();
            if (gruValues.customerGroup.customer.id) {
                gruValues.customerGroup.customer = {
                    id: gruValues.customerGroup.customer.id,
                    type: 'adult'
                }
            }
            else {
                for (var k in gruValues.customerGroup.customer) {
                    if (gruValues.customerGroup.customer[k] == '') gruValues.customerGroup.customer[k] = null;
                    else if (gruValues.customerGroup.customer[k] && gruValues.customerGroup.customer[k].getTime)
                        gruValues.customerGroup.customer[k] = gruValues.customerGroup.customer[k].getTime() / 1000;
                }
            }
        }
        if (groupMembers.length) {
            gruValues.customerGroup.groupMembers = groupMembers;
        }
        gruValues.customerGroup.pov = pov;
        return gruValues
    },

    addBooking: function (button) {
        var win = button.up('window'),
            isGroupMember = win.down('checkboxfield[name=isGroupMember]').value,
            groupBooking = win.down('toolbar').down('hiddenfield[name=group_booking]').value,
            tabPanel = win.down('tabpanel'),
            tabs = tabPanel.items.items,
            customerForm = tabs[0].down('form'),
            customerValid = customerForm ? customerForm.isValid() : true,
            gruValues = this.addBookingGroup(tabPanel),
            personValues = {},
            customerPays = win.down('checkboxfield[name=customerPays]').value;
        if (!groupBooking) {
            var gruForm = win.down('groupRoomUseForm'), // get book form
                schPanel = this.getSchPanel();

            gruValues = Ext.merge(gruValues, gruForm.getValues(false, false, false)); // merge gruValues with form data
            gruValues.startDate = Ext.Date.parse(gruValues.startDate, "Y-m-d");
            gruValues.endDate = Ext.Date.parse(gruValues.endDate, "Y-m-d");
            /* to UTC */
            gruValues.startDate = Pms.toUTC(gruValues.startDate);
            gruValues.endDate = Pms.toUTC(gruValues.endDate);
            var recBooking = Ext.create('Pms.modules.groupRoomUse.model.booking', gruValues),
                storeBooking = Ext.create('Pms.modules.groupRoomUse.store.bookingRoomUse');
            recBooking.data.baseRoom = {
                id: recBooking.data.baseRoom,
                type: gruForm.down('combobox[name=baseRoom]').valueModels[0].data.type
            };
//            companyValid = companyForm ? companyForm.isValid() : true;
            if (gruForm.isValid() && customerValid && this.groupMembersValid) {
                storeBooking.add(recBooking);
                storeBooking.sync({
                    success: function () {
                        win.close();
                    },
                    failure: function () {

                    }
                });
            }
            else {
                Ext.Msg.alert(l('incorrectForm'), Pms.iconError + l('checkCorrectForm'));
            }
        }
        else {
            var roomUseGrid = win.down('groupRoomUseRoomGrid'),
                roomUseStore = roomUseGrid.getStore(),
                recBooking = null,
                roomUses = [],
                source = win.down('toolbar').down('combobox[name=source]').value,
                rcode = win.down('toolbar').down('textfield[name=rcode]').value;

            gruValues.group = gruValues.customerGroup;
            delete gruValues.customerGroup;
            roomUseStore.each(function (rec) {
                var type = rec.data.type;
                recBooking = Ext.create('Pms.modules.groupRoomUse.model.booking', rec.data);
                var bookingData = recBooking.data;
                bookingData.room = {id: rec.data.id};
                bookingData.plan = {id: bookingData.plan};
                bookingData.customerPays = customerPays;
                bookingData.baseRoom = {id: bookingData.baseRoom, type: type};
                delete bookingData.upgrade;
                delete bookingData.customerGroup;
                delete bookingData.id;
                delete bookingData.status;
                /*to UTC*/
                bookingData.startDate = Pms.toUTC(bookingData.startDate);
                bookingData.endDate = Pms.toUTC(bookingData.endDate);
                bookingData.source = source;
                bookingData.rcode = rcode == '' ? null : rcode;

                roomUses.push({roomUse: bookingData});
            });

            gruValues.roomUses = roomUses;

            if (customerValid && this.groupMembersValid) {
                Pms.Ajax.request({
                    url: 'rest/roomUse/group',
                    jsonData: gruValues,
                    success: function () {
                        win.close();
                    }
                })
            }
            else {
                Ext.Msg.alert(l('incorrectForm'), Pms.iconError + l('checkCorrectForm'));
            }
        }


    },

    addGuest: function (button) {
        var win = button.up('window'),
            tabPanel = win.down('tabpanel'),
            tabs = tabPanel.items.items,
            tabCount = tabs.length,
            group = win.data.customerGroup,
            groupMembers = group.groupMembers ? group.groupMembers : [],
            roomUseId = win.data.id,
            master = win.data.customerGroup.customer,
            persons = [];

        for (var i = 0; i < tabCount - 2; i++) { // loop through tabs for persons and/or company
            var tab = tabs[i],
                form = tab.down('form'),
                values = form.getValues(),
                isOldPerson = false;
            for (var j = 0; j < groupMembers.length; j++) {
                if (form.personId && parseInt(form.personId) == groupMembers[j].person.id) {
                    isOldPerson = true;
                }
            }

            if (!isOldPerson) {
                if (form.isValid()) {
                    if (values.isChild) {
                        var recChild = Ext.create('Pms.modules.child.model.Child', values);
                        values = recChild.getData();
                        if (values.id) {
                            values = {id: values.id, type: 'child'}
                        }
                        else {
                            if (values.dob)
                                values.dob = values.dob.getTime() / 1000;
                        }
                    }
                    else {
                        var recAdult = Ext.create('Pms.modules.person.model.Person', values);
                        values = recAdult.getData();
                        if (values.id) {
                            values = {id: values.id, type: 'adult'}
                        }
                        else {
                            for (var k in values) {
                                if (values[k] == '') values[k] = null;
                                else if (values[k] && values[k].getTime)
                                    values.customerGroup.customer[k] = values[k].getTime() / 1000;
                            }
                            if (values.country) values.country = values.country[0];
                        }
                    }
                    var reqData = {
                        person: values,
                        customerGroup: {id: group.id},
                        roomUse: {id: roomUseId}
                    };
                    if (win.toGroup) {
                        Pms.Ajax.request({
                            async: false,
                            url: 'rest/groupMember',
                            jsonData: {
                                customerGroup: {id: group.id},
                                person: values
                            },
                            success: function (response) {
                                if (i == tabCount - 3)
                                    win.close();
                            }
                        })
                    }
                    else {
                        Pms.Ajax.request({
                            async: false,
                            url: 'rest/groupMemberToRoomUse',
                            jsonData: {
                                groupMember: reqData,
                                roomUse: {
                                    id: roomUseId
                                }
                            },
                            success: function (response) {
                                if (i == tabCount - 3)
                                    win.close();
                            }
                        });
                    }
                }
            }
        }

    },

    /*
     * Обновляет данные:
     * Group::payer_by_default (смена плательщика по умолчанию).
     * Room_Use::endDate - в случае, если use_type = repair (изменять период ремонта номера).
     * Room_Use::endDate - в случае, если use_type = living || booking_free || booking_warranty и только если новая дата позже предыдущей (продление даты пребывания в номере).
     * Room_Use::rate в случае, если use_type не “booking_free” и не “booking_warranty” (смена тарифа проживания, любой тариф любого типа номера).
     * Room_Use::rate в случае, если use_type = booking_free || booking_warranty (смена тарифа проживания, один из тарифов данного типа номера и никакой другой).
     */
    editBooking: function (scheduler, record, e, eOpts) {
        // @TODO: discuss startDate change in update method???
        var useType = record.data.useType;

        if (!record.getId()) { // in  case that empty event was clicked (bryntum sheduler delay)
            return
        }
        if (useType == 'REPAIR') {
            var win = Ext.widget('groupRoomUseRepairWindow'),
                recordData = record.data,
                roomUseStore = Ext.widget('roomUseStore'),
                gruForm = win.down('groupRoomUseRepairForm'),
                winButton = win.down('button[action=save]');

            recordData.room = recordData['room.id'];
            gruForm.getForm().setValues(recordData);
            gruForm.down('combobox[name=room]').disable();
            winButton.action = 'update';
        }
        else if (record.data.status == 'REFUSE') {
            console.log(record.data);
            Ext.widget('refuseInfoWindow', {data: record.data});
        }
        else {
            console.log(record.data.status);
            var win = Ext.widget('groupRoomUseEditWindow', {
                    data: record.data
                }),
                group = record.data.customerGroup;

            var gruForm = win.down('groupRoomUseEditForm'),
                gruFormValues = {
                    id: group.id,
                    roomUse: record.data.id,
                    customer: group.customer,
                    endDate: record.data.endDate
                };
            gruForm.getForm().setValues(gruFormValues);
        }
    },

    updateBooking: function (button) {
        var me = this,
            win = button.up('window'),
            gruForm = win.down('form'), // get book form
            gruValues = gruForm.getValues(),
            scanForm = gruForm.down('form[name=scanForm]'),
            customer = gruForm.data.customerGroup.customer,
            identity = Ext.isEmpty(scanForm) ? null : scanForm.down('field[name=identity]').getValue();

        win.data.description = gruValues.description;
        win.data.comment = gruValues.comment;
        win.data.endDate = parseInt(gruValues.endDate);
        var rec = Ext.create('Pms.modules.groupRoomUse.model.booking', win.data);
        delete rec.data.id;
        rec.data.startDate = parseInt(Ext.Date.format(rec.data.startDate, 'U'));
        rec.data.endDate = Pms.toUTC(rec.data.endDate);

        Pms.Ajax.request({
            url: 'rest/roomUse/' + win.data.id,
            method: 'PUT',
            jsonData: rec.data,
            success: function (response) {
                me.refreshOnClose();

                var msg = Pms.iconOk + l('roomUse.edited');

                if (!Ext.isEmpty(customer) && identity) { // Save document scan if master customer and scan exist
                    customer.identity = identity;
                    Pms.Ajax.request({
                        url: 'rest/adult/' + customer.id,
                        method: 'PUT',
                        async: false,
                        jsonData: customer,
                        success: function (response) {
                            msg += '<br />' + l('person.scanWasSaved');
                        },
                        failure: function (response) {
                            msg += '<br />' + l('person.scanWasNotSaved');
                        }
                    });
                }
                Pms.App.showNotification({
                    message: msg,
                    icon: Pms.notificationOk
                });
            },
            failure: function (response) {
                var source = response.source,
                    code = response.code;
                Ext.Msg.alert(l('error') + ' ' + l(source), Pms.iconError + l(code), function () {
                    me.refreshOnClose()
                });
            }
        });
    },

    refreshOnClose: function () {
        var groupRoomUseViewport = this.getGroupRoomUseViewport();
        if (!Ext.isEmpty(groupRoomUseViewport)) {
            var schPanel = groupRoomUseViewport.down('schPanel');
            if (!Ext.isEmpty(schPanel)) {
                var eventStore = schPanel.down('grid').getEventStore();
                if (!Ext.isEmpty(eventStore)) {
                    eventStore.reload();
                }
            }
            var summaryGrid = groupRoomUseViewport.down('summaryGrid');
            if (!Ext.isEmpty(summaryGrid)) {
                summaryGrid.getStore().reload();
            }
        }
        var groupRoomUseEditWindow = this.getGroupRoomUseEditWindow();
        if (!Ext.isEmpty(groupRoomUseEditWindow)) {
            var groupPersonGrid = groupRoomUseEditWindow.down('groupPersonGrid');
            if (!Ext.isEmpty(groupPersonGrid)) {
                groupPersonGrid.getStore().reload();
                groupPersonGrid.getSelectionModel().clearSelections();
            }
            var groupChildGrid = groupRoomUseEditWindow.down('childGrid');
            if (!Ext.isEmpty(groupChildGrid)) {
                groupChildGrid.getStore().reload();
                groupChildGrid.getSelectionModel().clearSelections();
            }
        }
    },

    eventResize: function (grid, record, eOpts) {
        var me = this;
        record.data.endDate = Pms.toUTC(record.data.endDate);
        record.data.startDate = Pms.toUTC(record.data.startDate);
        var id = record.data.id;
        if (record.data.type == 'REPAIR') {
            Pms.Ajax.request({
                url: 'rest/repair/' + id,
                method: 'PUT',
                jsonData: record.data,
                success: function (response) {
                    me.refreshOnClose();
                },
                failure: function (response) {
                    var source = response[0].source,
                        code = response[0].code;
                    Ext.Msg.alert(l('error') + ' ' + l(source), l(code), function () {
                        me.refreshOnClose()
                    });
                }
            });
        }
        else {
            Pms.Ajax.request({
                url: 'rest/roomUse/' + id,
                method: 'PUT',
                jsonData: record.data,
                success: function (response) {
                    me.refreshOnClose();
                },
                failure: function (response) {
                    var source = response.source,
                        code = response.code;
                    Ext.Msg.alert(l('error') + ' ' + l(source), Pms.iconError + l(code), function () {
                        me.refreshOnClose()
                    });
                }
            });
        }
    },

    editPerson: function (grid, record) {
        var win = Ext.widget('personEditWindow'),
            personStore = grid.getStore();

        win.down('form').loadRecord(record);

        var i = 0,
            group_ids = [];
        Ext.each(record.data['property_groups'], function (v) {
            group_ids[i] = v.id;
            i++;
        });

        var groupGrid = win.down('grid'),
            groupStore = groupGrid.getStore().load({
                params: {
                    filter: {
                        id: { in: group_ids }
                    }
                }
            });
    },

    moveBooking: function (button) {
        var me = this;
        var win = button.up('window'),
            gruForm = win.down('groupRoomUseMoveForm'), // get refuse form
            gruValues = gruForm.getValues();
        var id = parseInt(gruValues.id);
        gruValues.room = {id: gruValues.room};
        gruValues.baseRoom = {id: gruValues.baseRoom, type: gruForm.baseRoomType};
        gruValues.sinceDate = parseInt(gruValues.sinceDate);
        /* to UTC */
        gruValues.sinceDate = Pms.toUTC(gruValues.sinceDate);
        console.log(gruValues.customerPays);
        if (gruValues.upgrade)
            delete gruValues.plan;
        else
            gruValues.plan = {id: gruValues.plan};
        delete gruValues.id;
        if (gruForm.isValid()) {
            Pms.Ajax.request({ // make refuse request
                async: false,
                url: 'rest/roomUse/' + id + '/moved',
                prevError: true,
                jsonData: gruValues,
                success: function (response) {
                    me.refreshOnClose();
                    win.close();
                },
                failure: function (res, options) {
                    if (res.code == 'refund.bankDetails.required') {
                        var bankDetailsWin = Ext.create('Pms.modules.bankDetails.view.BankDetailsSelectWindow', {
                            url: options.url,
                            data: options.jsonData,
                            success: options.onSuccessCallback,
                            method: 'POST'
                        });
                        bankDetailsWin.show();
                    }
                    else {
                        this.callParent();
                    }
                }
            });
        }
    },

    refuseBooking: function (button) {
        var me = this,
            win = button.up('window'),
            form = win.down('groupRoomUseRefuseForm'),
            data = form.getValues(),
            isGroup = win.down('checkboxfield').value;

        if (form.isValid()) {
            if (!isGroup) {
                Pms.Ajax.request({
                    url: 'rest/roomUse/' + data.id + '/refused',
                    method: 'POST',
                    prevError: true,
                    jsonData: {date: Pms.toUTC(data.date)},
                    success: function () {
                        win.close();
                    },
                    failure: Pms.bankDetailsRequired
                })
            } else {
                Pms.Ajax.request({
                    url: 'rest/roomUse/refusedByGroup/' + win.data.customerGroup.id,
                    method: 'POST',
                    prevError: true,
                    jsonData: {
                        date: Pms.toUTC(data.date)
                    },
                    success: function () {
                        Pms.App.showNotification({
                            message: l('saveSuccess.msg'),
                            icon: Pms.notificationOk
                        });
                        win.close();
                    },
                    failure: Pms.bankDetailsRequired
                })
            }
        }
    },

    confirm: function (button) {
        var win = button.up('window'),
            gruForm = win.down('groupRoomUseConfirmForm'), // get income form
            gruValues = gruForm.getValues();

        if (gruForm.isValid()) {
            Pms.Ajax.request({ // make income request
                async: false,
                url: 'rest/',
                jsonData: gruValues,
                success: function (response) {
                    win.close();
                },
                failure: function (response) {

                }
            });
        }
    },
    income: function (button) {
        var win = button.up('window'),
            id = win.data.id,
            me = this,
            fieldTime = win.down('timefield').getValue(),
            min = fieldTime.getMinutes(),
            hour = fieldTime.getHours(),
            time,
            isGroup = win.down('checkboxfield').value,
            url = 'rest/roomUse/',
            startDate = win.data.startDate;
        startDate.setMinutes(min);
        startDate.setHours(hour);
        time = startDate;
        console.log(win.data.startDate, time);
        time = Pms.toUTC(time);
        /*toUTC*/
        if (!isGroup) {
            url += id + '/checkedIn';
        }
        else {
            url += 'checkedInByGroup/' + win.data.customerGroup.id
        }
        Ext.Ajax.request({
            url: url,
            method: 'POST',
            jsonData: {time: time},
            success: function () {
                win.close();
                me.refreshOnClose();
            },
            failure: function (res) {
                var response = JSON.parse(res.responseText)[0],
                    code = response.code,
                    source = response.source;
                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l(code));
            }
        })
    },
    incomePerson: function (button) {
        var win = button.up('window'),
            roomUse = {
                id: win.roomUseId
            },
            groupMembers = win.down('grid').getSelectionModel().getSelection(),
            jsonData = [], me = this;
        Ext.each(groupMembers, function (member, index) {
            jsonData.push({
                roomUse: roomUse,
                groupMember: {id: member.data.id}
//                id: member.data.id
            });
        }, this);

        /*toUTC*/
        Pms.Ajax.request({ // make income request
            url: 'rest/groupMemberToRoomUse/list',
            method: 'POST',
            jsonData: jsonData,
            success: function (response) {
                win.income = true;
                win.close();
                var personGrid = me.getGroupPersonGrid(),
                    childGrid = me.getGroupPersonGrid();
                if (personGrid)
                    personGrid.getStore().reload();
                if (childGrid)
                    childGrid.getStore().reload();
                if (win.toRoomUseIncome) {
                    me.income(win.btn);
                }
            }
        });
    },

    outgo: function (button) {
        var win = button.up('window'),
            id = win.data.id,
            me = this,
            fieldTime = win.down('timefield').getValue(),
            min = fieldTime.getMinutes(),
            hour = fieldTime.getHours(),
            time = win.data.endDate < new Date() ? win.data.endDate : new Date(),
            isGroup = win.down('checkboxfield').value,
            url = 'rest/roomUse/';
        time.setMinutes(min);
        time.setHours(hour);
        time = Pms.toUTC(time);
        if (!isGroup) {
            url += id + '/checkedOut';
        }
        else {
            url += 'checkedOutByGroup/' + win.data.customerGroup.id
        }
        Ext.Ajax.request({
            url: url,
            method: 'POST',
            jsonData: {time: time},
            success: function () {
                win.close();
                me.refreshOnClose();
            },
            failure: function (res) {
                var response = JSON.parse(res.responseText)[0],
                    code = response.code,
                    source = response.source;
                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l(code));
            }
        })
    },

    repair: function (button) {
        var win = button.up('window'),
            gruForm = win.down('groupRoomUseRepairForm'), // get repair form
            gruValues = gruForm.getValues();
        gruValues.room = {id: gruValues.room};
        delete gruValues.id;
        gruValues.startDate = Pms.toUTC(gruValues.startDate);
        gruValues.endDate = Pms.toUTC(gruValues.endDate);
        if (gruForm.isValid()) {
            Pms.Ajax.request({ // make repair request
                url: 'rest/repair',
                jsonData: gruValues,
                success: function (response) {
                    win.close();
                },
                failure: function (response) {
                    var source = response.source,
                        code = response.code;
                    Ext.Msg.alert(l('error') + ' ' + l(source), l(code));
                }
            });
        }
    },

    repairUpdate: function (button) {
        var win = button.up('window'),
            gruForm = win.down('groupRoomUseRepairForm'), // get repair form
            gruValues = gruForm.getValues();
        gruValues.startDate = parseInt(gruValues.startDate);
        gruValues.endDate = parseInt(gruValues.endDate);
        var id = gruValues.id;
        gruValues.room = {id: gruForm.getForm().findField('room').getValue()};
        delete gruValues.id;
        var me = this;
        Pms.Ajax.request({
            url: 'rest/repair/' + id,
            method: 'PUT',
            jsonData: gruValues,
            success: function (response) {
                me.refreshOnClose();
            },
            failure: function (response) {
                var source = response[0].source,
                    code = response[0].code;
                Ext.Msg.alert(l('error') + ' ' + l(source), l(code), function () {
                    me.refreshOnClose()
                });
            }
        });
    },
    personUpdateInfo: function (button) {
        var win = button.up('window'),
            gruForm = win.down('personForm'), // get repair form
            gruValues = gruForm.getValues(),
            personBookForm = Ext.ComponentQuery.query('tabpanel')[0].activeTab.down('personBookForm');
        if (gruValues.dob) gruValues.dob = new Date(parseInt(gruValues.dob) * 1000);
        if (gruValues.passportValidTill) gruValues.passportValidTill = parseInt(gruValues.passportValidTill);
        if (gruValues.entryValidTill) gruValues.entryValidTill = parseInt(gruValues.entryValidTill);
        if (gruValues.entryValidFrom) gruValues.entryValidFrom = parseInt(gruValues.entryValidFrom);
        if (this.personDocumentFile) {
            gruValues.identity = this.personDocumentFile;
            this.personDocumentFile = null;
        }
        personBookForm.getForm().setValues(gruValues);
        win.close();
    },

    personUpdate: function (button) {
        var win = button.up('window'),
            gruForm = win.down('personForm'), // get repair form
            gruValues = gruForm.getValues(),
            me = this;
        var id = gruValues.id;
        delete gruValues.id;
        for (var k in gruValues) {
            if (gruValues[k] == '') gruValues[k] = null;
            else if (gruValues[k] && gruValues[k].getTime)
                gruValues[k] = gruValues[k].getTime() / 1000;
        }
        gruValues.type = 'adult';
        if (gruForm.isValid()) {
            if (this.personDocumentFile) {
                gruValues.identity = this.personDocumentFile;
                this.personDocumentFile = null;
            }
            Pms.Ajax.request({
                url: 'rest/adult/' + id,
                method: 'PUT',
                jsonData: gruValues,
                success: function () {
                    var personBookForm = me.getGroupRoomUseAddTabs().activeTab.down('personBookForm');
                    if (gruValues.dob) gruValues.dob = new Date(parseInt(gruValues.dob) * 1000);
                    personBookForm.getForm().setValues(gruValues);
                    win.close();
                }
            })
        }
    },
    uploadFile: function (field) {
        var documentsForm = field.up('form').getForm(),
            imageField = field.up('window').down('image');

        documentsForm.submit({
            scope: this,
            success: function (s, res) {
                this.personDocumentFile = res.result.content;
                imageField.setSrc('rest/document/' + this.personDocumentFile)
            }
        })
    },

    updateGroupBooking: function (button) {
        var win = button.up('window'),
            roomUseGrid = win.down('groupRoomUseRoomGrid'),
            roomUseStore = roomUseGrid.getStore(),
            recBooking = null,
            roomUses = [],
            group = null,
            me = this;

        roomUseStore.each(function (rec) {
            recBooking = Ext.create('Pms.modules.groupRoomUse.model.booking', rec.data);
            var bookingData = recBooking.data;
            if (!rec.data.exist) {
                bookingData.room = {id: rec.data.id};
                bookingData.plan = {id: bookingData.plan};
                bookingData.baseRoom = {id: bookingData.baseRoom, type: rec.data.type};
                delete bookingData.upgrade;
                delete bookingData.customerGroup;
                delete bookingData.id;
                delete bookingData.status;
                delete bookingData.source;
                /*to UTC*/
                bookingData.startDate = Pms.toUTC(bookingData.startDate);
                bookingData.endDate = Pms.toUTC(bookingData.endDate);

                roomUses.push({
                    roomUse: bookingData
                });
            }
        });

        Pms.Ajax.request({
            url: 'rest/roomUse/group',
            jsonData: {
                roomUses: roomUses,
                group: win.customerGroup
            },
            success: function () {
                win.close();
                me.getRoomUseRoomGrid().getStore().reload()
            }
        });
    },

    buildItems: function () {
        if (typeof(this.win) !== "undefined") {
            var view = Ext.widget('groupRoomUseViewport');
            this.win.maximized = true;
            this.win.add(view);
        }
    }
});