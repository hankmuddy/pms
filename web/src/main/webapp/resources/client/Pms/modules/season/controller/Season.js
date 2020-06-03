Ext.define('Pms.modules.season.controller.Season', {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.season.view.SeasonAddWindow',
        'Pms.modules.season.view.SeasonEditWindow',
        'Pms.modules.season.view.SeasonForm',
        'Pms.modules.plan.view.PlanSch'
    ],
    stores: [
        'Pms.modules.season.store.Season'
    ],
    models: [
        'Pms.modules.season.model.Season'
    ],
    refs: [
        {ref: 'seasonGrid', selector: 'seasonGrid'},
        {ref: 'seasonEditWindow', selector: 'seasonEditWindow'},
        {ref: 'planSch', selector: 'planSch'}
    ],

    extravailable: true,

    init: function () {

        var me = this;

        me.control({
            'planSch': {
                newseason: me.new,
                editseason: me.edit,
                commitseason: me.commit,
                deleteseason: me.delete
            },
            'seasonAddWindow button[action=add]': {
                click: me.add
            },
            'seasonEditWindow button[action=update]': {
                click: me.update
            },
            'seasonEditWindow button[action=commit]': {
                click: me.commit
            },
            'seasonEditWindow button[action=delete]': {
                click: me.delete
            },
            'seasonAddWindow': {
                close: me.refreshOnClose
            },
            'seasonEditWindow': {
                close: me.refreshOnClose
            }
        });
    },

    new: function (sch, rec, resource, e, eOpts) {
        var me = this;

        if (!resource.data.approved) {
            Pms.App.showNotification({
                message: l('createSeasonError')
            });
            me.refreshOnClose();
            return false;
        }

        var win = Ext.widget('seasonAddWindow'),
            form = win.down('form'),
            data = rec.data;

        data.plan = resource.data.id;

        form.getForm().setValues(data);
        win.show();
    },

    add: function (btn) {
        var me = this,
            win = btn.up('window'),
            form = win.down('form'),
            data = form.getValues(),
            grid = me.getPlanSch().down('grid'),
            store = grid.getEventStore();

        if (!form.isValid()) {
            Pms.App.showNotification({
                message: l('checkFormFields')
            });
            return false;
        }

        data.plan = {id: data.plan};
        /* to UTC */
        data.start = Pms.toUTC(data.start);
        data.until = Pms.toUTC(data.until);

        Pms.Ajax.request({
            url: 'rest/season',
            method: 'POST',
            jsonData: data,
            success: function (response) {
                win.close();
                Pms.App.showNotification({
                    message: l('saveSuccess.msg'),
                    icon: Pms.notificationOk
                });
            },
            failure: function () {
                win.close();
                Pms.App.showNotification({
                    message: l('seasonSaveError')
                });
            }
        });
    },

    edit: function (sch, rec, e, r) {
        var me = this,
            data = rec.data,
            win = Ext.widget('seasonEditWindow', {data: data}),
            form = win.down('form');

        data.plan = data.plan.id;

        form.getForm().setValues(data);
        win.show();
    },

    update: function (btn) {
        var me = this,
            win = btn.up('window'),
            form = win.down('form'),
            data = form.getValues(),
            grid = me.getPlanSch().down('grid'),
            store = grid.getEventStore();

        if (!form.isValid()) {
            Pms.App.showNotification({
                message: l('checkFormFields')
            });
            return false;
        }

        data.plan = {id: data.plan};
        data.start = Pms.toUTC(data.start);
        data.until = Pms.toUTC(data.until);

        Pms.Ajax.request({
            url: 'rest/season/' + data.id,
            method: 'PUT',
            jsonData: data,
            success: function (response) {
                win.close();
                Pms.App.showNotification({
                    message: l('saveSuccess.msg'),
                    icon: Pms.notificationOk
                });
            },
            failure: function () {
                win.close();
                Pms.App.showNotification({
                    message: l('seasonSaveError')
                });
            }
        });
    },

    commit: function (sch, rows, e, r) {
        var me = this;

        if (!rows.length) {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
            return false;
        }

        var confirmMsg = rows.length > 1 ? l('confirmSeasons') : l('confirmSeason');
        Ext.Msg.confirm(l('confirm'), confirmMsg, function (btn) {
            if (btn === 'yes') {
                ids = rows[0].data.id;
                for (var i = 1; i < rows.length; i++) ids += ',' + rows[i].data.id;
                Pms.Ajax.request({
                    url: 'rest/season/' + ids + '/approved',
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    success: function (response) {
                        me.refreshOnClose();
                        var msg = rows.length > 1 ? l('seasonsConfirmed') : l('seasonConfirmed');
                        Pms.App.showNotification({
                            message: msg,
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function () {
                        me.refreshOnClose();
                        var msg = rows.length > 1 ? l('seasonsNotConfirmed') : l('seasonNotConfirmed');
                        Pms.App.showNotification({message: msg});
                    }
                });
            }
        }, this);
    },

    delete: function (sch, rec, e, r) {
        var me = this,
            data = rec.data;

        if (data.approved) {
            Ext.Msg.alert(l('warning'), l('cantDeleteCommitedSeason'));
            return false;
        }

        Ext.Msg.confirm(l('confirm'), l('deleteSeason'), function (btn) {
            if (btn === 'yes') {
                Pms.Ajax.request({
                    url: 'rest/season/' + data.id,
                    method: 'DELETE',
                    headers: {'Content-Type': 'application/json'},
                    success: function (response) {
                        me.refreshOnClose();
                        Pms.App.showNotification({
                            message: l('seasonDeleted'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function (response) {
                        me.refreshOnClose();
                        Pms.App.showNotification({
                            message: l('seasonNotDeleted')
                        });
                    }
                });
            }
        }, this);
    },

    refreshOnClose: function () {
        var planSch = this.getPlanSch(),
            eventStore;
        if (!Ext.isEmpty(planSch)) {
            eventStore = planSch.down('grid').getEventStore();
            if (!Ext.isEmpty(eventStore)) {
                eventStore.reload();
            }
        }
    }
});