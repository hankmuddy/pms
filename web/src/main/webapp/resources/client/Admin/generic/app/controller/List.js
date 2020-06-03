Ext.define('Admin.generic.app.controller.List', {
    extend: 'Ext.app.Controller',
    run: function (conf) {

        this.stage = conf.view;
        if (conf.links) {
            this.links = conf.links;
        }
        if (conf.breadcrumbs) {
            this.setBreadcrumbs(conf.breadcrumbs)
        }
        this.initStore();
        this.initView();
        this.setHandlersOnViewEvents(this.view);
        this.stage.add(this.view);
        this.refreshGrid();
    },
    initView: function () {
        this.view = Ext.create(this.viewClass, {store: this.store})
        if (!this.view.columns[1].renderer) {
            this.view.columns[1].renderer = function (value, metaData, record) {
                return '<a style="color: black" href=#' + Admin.generic.Utils.getHashPart(1) + '/view/' + record.getId() + '>' + value + '</a>'
            }
        }
    },
    initStore: function () {
        this.store = Ext.create(this.storeClass);
        this.store.load();
    },
    setBreadcrumbs: function (breadcrumbs) {
        history.replaceState(null, null, '#' + this.links['list']);
        breadcrumbs.setBreadcrumbs(this.links['list']);
        breadcrumbs.hide();
    },
    setHandlersOnViewEvents: function () {
        this.view.on('delete', this.onDelete, this);
        this.view.on('edit', this.onEdit, this);
        this.view.on('view', this.onView, this);
        this.view.on('_add_', this.onAdd, this);
        this.view.on('message', this.onMessage, this);
        this.view.on('groupAction', this.onGroupAction, this);
        this.view.on('destroy', this.clearInterval, this);
    },
    unsetHandlersOnViewEvents: function () {
        this.view.un('delete', this.onDelete, this);
        this.view.un('edit', this.onEdit, this);
        this.view.un('view', this.onView, this);
        this.view.un('_add_', this.onAdd, this);
        this.view.un('message', this.onMessage, this);
        this.view.un('groupAction', this.onGroupAction, this);
    },
    onEdit: function (record) {
        if (record.getId)
        Admin.getApplication().navigateTo(this.links['edit'] + '/' + record.getId())
    },
    onView: function (record) {
        Admin.getApplication().navigateTo(this.links['view'] + '/' + record.getId())
    },
    onAdd: function (record) {
        Admin.getApplication().navigateTo(this.links['add'])
    },
    onDelete: function (record) {
        if (!record) return;
        var store = this.store;
        var afterConfirm = function (btn) {
            if (btn == 'yes') {
                // record.set('active', false);
                store.remove(record);
                record.destroy({
                    success: function () {
                        store.load();
                    }
                });
            }
        };
        Ext.MessageBox.confirm(l('common.confirm.delete.title'), l('common.confirm.delete.message'), afterConfirm);
    },
    onGroupAction: function (action) {
        if (action.operation == 'delete') {
            this.onGroupDelete(action);
        }
    },
    onGroupDelete: function (action) {
        var selModel = this.listView.getSelectionModel();
        var records = selModel.getSelection();
        if (!records.length) {
            Ext.MessageBox.alert(l('common.confirm.delete.title'),
                l('common.confirm.groupDelete.emptySelection'),
                function () {
                    action.target.reset();
                });
            return
        }
        var store = this.store;
        var afterConfirm = function (btn) {
            action.target.reset()
            if (btn == 'yes') {
                var count = records.length;
                records.forEach(function (record) {
                    store.remove(record)
                    record.destroy({
                        success: function () {
                            if (!--count)store.load();
                        }
                    });
                })
            }
        };
        Ext.MessageBox.confirm(l('common.confirm.delete.title'), l('common.confirm.delete.message'), afterConfirm);
    },
    refreshGrid: function () {
        this.clearInterval();
        var store = this.store;
        Admin.getApplication().timer = setInterval(function () {
            store.reload();
        }, 120000);
    },
    clearInterval: function () {
        if (Admin.getApplication().timer)
            clearInterval(Admin.getApplication().timer);
    }
})
