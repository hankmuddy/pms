Ext.define('Pms.modules.plan.controller.Plan', {
    extend: 'Pms.abstract.Controller',

    views: [
        'Pms.modules.plan.view.Viewport',
        'Pms.modules.plan.view.PlanAddWindow',
        'Pms.modules.plan.view.PlanEditWindow',
        'Pms.modules.plan.view.PlanGrid',
        'Pms.modules.plan.view.PlanSch',
        'Pms.modules.plan.view.PlanForm',
        'Pms.modules.plan.view.PlanFilterForm',
        'Pms.modules.plan.view.filterForm',
        'Pms.modules.plan.view.PlanView',
        'Pms.modules.season.view.SeasonGrid',
        'Pms.modules.virtualPlan.view.VirtualPlanGrid',
        "Pms.modules.restriction.view.RestrictionGrid",

    ],
    stores: [
//        'Pms.modules.plan.store.Plan',
//        'Pms.modules.season.store.Season'
    ],
    models: [
//        'Pms.modules.plan.model.Plan',
//        'Pms.modules.season.model.Season'
    ],
    refs: [
        {ref: 'planViewport', selector: 'planViewport'},
        {ref: 'planGrid', selector: 'planGrid'},
        {ref: 'planSch', selector: 'planSch'}
    ],
    subControllers: [
        'Pms.modules.season.controller.Season',
        'Pms.modules.living.controller.Living',
        'Pms.modules.virtualPlan.controller.VirtualPlan',
        'Pms.modules.restriction.controller.Restriction'
    ],

    extravailable: true,

    init: function (contr, subController) {
        var me = this;

        if (!subController) {
            var view = Ext.widget('planViewport');
            me.buildItems(view);
        }

        me.control({
            'planSch button': {
                click: me.routeAction
            },
            'planSch': {
                editplan: function (grid, record, item, index, e) {
                    me.edit(grid, index, null);
                },
                commitplan: me.commit,
                deleteplan: me.delete,
                defaultplan: me.setByDefault
            },
            'planAddWindow button[action=save]': {
                click: me.add
            },
            'planEditWindow button[action=update]': {
                click: me.update
            },
            'planEditWindow button[action=update-living]': {
                click: me.update
            },
            // refresh actions
            'planAddWindow': {
                close: me.refreshOnClose
            },
            'planEditWindow': {
                close: me.refreshOnClose
            }
        });
    },

    view: function (grid, row, tr) {
        console.log(grid, row);
        Ext.widget('planView', {data: row.data});
    },

    new: function (btn, e) {
        Ext.widget('planAddWindow').show();
    },

    add: function (btn, e) {
        var win = btn.up('window'),
            form = win.down('form'),
            data = form.getForm().getValues(),
            rec = Ext.create('Pms.modules.plan.model.Plan', data),
            store = Ext.create('Pms.modules.plan.store.Plan');

        if (form.isValid()) {
            store.add(rec);
            store.sync({
                success: function (response) {
                    win.close();
                },
                failure: function () {
                    store.reload();
                    win.close();
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('checkFormFields')
            });
        }
    },

    edit: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        var win = Ext.widget('planEditWindow', {data: rec}),
            form = win.down('form').getForm();

        form.setValues(rec);
        win.show();
    },

    update: function (btn, e) {
        var win = btn.up('window'),
            form = win.down('form'),
            data = form.getForm().getValues();

        if (form.isValid()) {
            Pms.Ajax.request({
                url: 'rest/plan/' + data.id,
                method: 'PUT',
                jsonData: data,
                success: function (response) {
                    win.close();
                    Pms.App.showNotification({
                        message: l('dataSaved'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('dataNotSaved')
                    });
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('checkFormFields')
            });
        }
    },

    commit: function (grid, rec, colIndex, rows) {
        var me = this,
            store = grid.getStore();
//            row = store.getAt(rowIndex),
//            rec = row.data;

        Ext.Msg.confirm(l('confirm'), l('plan.confirmQuestion'), function (btn) {
            if (btn === "yes") {
//                var ids = rows[0].data.id;
//                for(var i = 1; i < rows.length; i++) ids += ',' + rows[i].data.id;
                Pms.Ajax.request({
                    url: 'rest/plan/' + rec.data.id + '/approved',
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    statusBar: this.win.statusBar,
                    success: function (response) {
                        me.refreshOnClose();
                        Pms.App.showNotification({
                            message: l('plan.confirmed'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function () {
                        store.reload();
                        Pms.App.showNotification({
                            message: l('plan.notConfirmed')
                        });
                    }
                });

            }

        }, this);
    },

    delete: function (grid, rec, colIndex) {
        var me = this,
            store = grid.getStore();
//            row = store.getAt(rowIndex),
//            rec = row.data;

        if (rec.approved) {
            Pms.App.showNotification({message: l('plan') + ' <b>' + rec.data.name + '</b> - ' + l('confirmed') + '.<br/>' + l('constraint.entityIsUsed')});
            return false;
        }

        Ext.Msg.confirm('Удаление', 'Удалить тарифный план <b>' + rec.data.name + '</b>?', function (btn) {
            if (btn === 'yes') {
                store.remove(rec);
                store.sync({
                    success: function (response) {
                        me.refreshOnClose();
                    },
                    failure: function () {
                        store.reload();
                    }
                });
            }
        });
    },
    setByDefault: function (grid, rec) {
        var me = this,
            store = grid.getStore();
        Pms.Ajax.request({
            url: 'rest/plan/' + rec.data.id + '/default',
            method: 'PUT',
            success: function (response) {
                me.refreshOnClose();
                Pms.App.showNotification({
                    message: l('success'),
                    icon: Pms.notificationOk
                });
            },
            failure: function () {
                store.reload();
                Pms.App.showNotification({
                    message: l('error')
                });
            }
        });
    },

    refreshOnClose: function () {
        var planViewport = this.getPlanViewport();
        if (!Ext.isEmpty(planViewport)) {
            var planGrid = planViewport.down('planGrid');
            if (!Ext.isEmpty(planGrid)) {
                planGrid.getStore().reload();
                planGrid.getSelectionModel().clearSelections();
            }
        }
        var planSch = this.getPlanSch();
        if (!Ext.isEmpty(planSch)) {
            resourceStore = planSch.down('grid').getResourceStore();
            if (!Ext.isEmpty(resourceStore)) {
                resourceStore.reload();
            }
            eventStore = planSch.down('grid').getEventStore();
            if (!Ext.isEmpty(eventStore)) {
                eventStore.reload();
            }
        }
    },

//    buildItems: function () {
//        if (!Ext.isEmpty(this.win)) {
//            var view = Ext.widget('planViewport');
//            this.win.add(view);
//        }
//    }
});
