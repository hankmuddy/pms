Ext.define('Pms.modules.living.controller.Living', {
    extend: 'Pms.abstract.Controller',
    views: [
        "Pms.modules.living.view.LivingGrid"
    ],
    stores: [
        "Pms.modules.living.store.Living"
    ],
    models: [
        "Pms.modules.living.model.Living"
    ],

    refs: [
        {ref: 'livingGrid', selector: 'livingGrid'}
    ],

    extravailable: true,

    init: function () {
        var me = this;

        me.control({
            'seasonEditWindow button[action=update-living]': {
                click: me.update
            },
            'planEditWindow button[action=update-living]': {
                click: me.update
            }
        });
    },

    update: function (button) {
        var win = button.up('window'),
            me = this,
            grid = win.down('livingGrid'),
            store = grid.getStore(),
            updatedRecords = store.getUpdatedRecords(),
            recArr = [];

        if (!Ext.isEmpty(updatedRecords[0])) {
            var url = win.xtype == 'seasonEditWindow' ? 'bySeason' : 'byPlan',
                urlId = win.xtype == 'seasonEditWindow' ? updatedRecords[0].data.season.id : updatedRecords[0].data.plan.id;

            Ext.each(updatedRecords, function (rec) {
                for (var changeVal in rec.modified) {
                    rec.raw[changeVal] = Math.ceil(rec.data[changeVal] * 100);
                }
                recArr.push(rec.raw);
            });

            Pms.Ajax.request({
                url: 'rest/living/' + url + '/' + urlId,
                method: 'PUT',
                jsonData: recArr,
                success: function (response) {
                    me.refreshOnClose();
                    Pms.App.showNotification({
                        message: l('dataSaved'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    me.refreshOnClose();
                    Pms.App.showNotification({
                        message: l('dataNotSaved')
                    });
                }
            });
        }

        me.refreshOnClose();
    },

    refreshOnClose: function () {
        var livingGrid = this.getLivingGrid();
        if (!Ext.isEmpty(livingGrid)) {
            livingGrid.getStore().reload();
            livingGrid.getSelectionModel().clearSelections();
        }
    }
});