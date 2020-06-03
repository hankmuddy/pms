Ext.define('Admin.generic.app.Controller', {
    requires: ['Admin.generic.Utils'],
    extend: 'Ext.app.Controller',
    defaultAction: 'list',
    getAction: function (action) {
        if (!action) return null;
        if (this[action]) return action;
    },
    getNameSpacePrefix: function () {
        // return this.$className.split('controller')[0];
        return 'Admin.' + this.name + '.';
    },
    dispatchAction: function (_action, _params) {
        while (1) {
            var action = this.getAction(_action);
            if (action) break;

            var actionPart = Admin.generic.Utils.getHashPart(2);
            if ((actionPart === null) && this.defaultAction) {
                action = this.getAction(this.defaultAction);
                if (action)break;
            }

            if (Admin.generic.Utils.isNotEmptyString(actionPart)) {
                action = this.getAction(actionPart)
                if (action)break;
            }
            console.error(this, 'Unknown Action:', action);
            Admin.getApplication().notFound();
            break;
        }
        this.loadPage(action)
    },
    loadPage: function (action) {
        var panel = Ext.ComponentQuery.query('#' + this.name)[0]
        if (!this.wrapperView) {
            this.wrapperView = Ext.create(this.getNameSpacePrefix() + 'view.Wrapper');
        }
        this.breadcrumbs = this.wrapperView.down('breadcrumbs')
        if (!this.mainModel) {
            var modelName = this.getNameSpacePrefix() + 'model.Main';
            Ext.create(modelName);
            this.mainModel = Ext.ClassManager.get(modelName);
            this.mainModel.getProxy().on('exception', this.onProxyException, this);
        }
        var view = this[action]();
        this.wrapperView.getLayout().setActiveItem(view);
        panel.add(this.wrapperView);

        var tabpanel = panel.up('tabpanel');
        while (tabpanel) {
            delete panel.initialConfig['href'];
            tabpanel.suspendEvent('tabchange');
            tabpanel.setActiveTab(panel.getItemId());
            // tabpanel.getLayout().setActiveItem(panel);
            tabpanel.resumeEvent('tabchange');
            panel = tabpanel.up('panel');
            tabpanel = panel.up('tabpanel');
        }


        // var contentWrapper = panel.up('contentwrapper');
        var contentWrapper = Ext.ComponentQuery.query('#contentwrapper')[0];
        contentWrapper.getLayout().setActiveItem(this.card);
        var viewport = panel.up('viewport');
        viewport.setVisible(true);
    },
    list: function () {
        history.replaceState(null, null, '#' + this.name + '/list');
        if (this.breadcrumbs) {
            this.breadcrumbs.setBreadcrumbs(this.name + '/list');
            this.breadcrumbs.hide();
        }
        if (!this.mainStore) {
            this.mainStore = Ext.create(this.getNameSpacePrefix() + 'store.Main', {scope: this})
        }
        if (!this.listView) {
            if (this.mainList) {
                this.listView = Ext.create(this.mainList, {store: this.mainStore, name: this.name});
            } else  this.listView = Ext.create(this.getNameSpacePrefix() + 'view.List', {store: this.mainStore, name: this.name});
            // this.listView = Ext.create(this.getNameSpacePrefix() + 'view.List',{store: this.mainStore, name: this.name});
            this.listView.getView().bindStore(this.mainStore)
            this.listView.on('delete', this.onDelete, this)
            this.listView.on('edit', this.onEdit, this)
            this.listView.on('view', this.onView, this)
            this.listView.on('add', this.onAdd, this)
            this.listView.on('groupAction', this.onGroupAction, this)
            this.listView.on('message', this.onMessage, this)
        }
        this.mainStore.load();
        return this.listView;
    },
    add: function () {
        this.breadcrumbs.setBreadcrumbs(this.name + '/add')
        if (!this.createView) {
            this.createView = Ext.create(this.getNameSpacePrefix() + 'view.Create');
            this.createView.on('createSubmit', this.onCreateSubmit, this)
            this.createView.on('cancel', this.onCancel, this)
        }
        return this.createView;
    },
    view: function () {
        var id = Admin.generic.Utils.getHashPart(3);
        if (!Admin.generic.Utils.isValidId(id)) {
            console.error(this, 'Incorrect Id in Url');
            Admin.getApplication().notFound();
        }
        if (this.viewBreadcrumbs) {
            this.viewBreadcrumbs()
        } else this.breadcrumbs.setBreadcrumbs(this.name + '/view')

        if (!this.viewView) {

            if (this.mainView) {
                this.viewView = Ext.create(this.mainView);
            } else  this.viewView = Ext.create(this.getNameSpacePrefix() + 'view.View');
            this.viewView.on('updateSubmit', this.onUpdateSubmit, this);

            this.viewView.on('cancel', this.onCancel, this);
        }

        this.mainModel.load(id, {
            scope: this,
            success: function (record, operation) {
                this.viewView.currentRecord = record;
                if (this.beforeViewView)this.beforeViewView(record);
                if (this.breadcrumbs) {
                    var href = this.name + '/view';
                    var breadcrumbsConf = {};
                    breadcrumbsConf[href] = {
                        href: href + id,
                        title: record.get('_title')
                    };
                    this.breadcrumbs.setBreadcrumbs(href, breadcrumbsConf);
                }
                this.viewView.getForm().loadRecord(record);
                // var title = this.getViewTitle(record);
                // this.viewView.setTitle(title);
            },
        })
        return this.viewView;
    },
    edit: function () {
        var id = Admin.generic.Utils.getHashPart(3);
        if (!Admin.generic.Utils.isValidId(id)) {
            console.error(this, 'Incorrect Id in Url');
            Admin.getApplication().notFound();
        }

        this.breadcrumbs.setBreadcrumbs(this.name + '/edit')
        if (!this.updateView) {
            this.updateView = Ext.create(this.getNameSpacePrefix() + 'view.Update');
            this.updateView.on('updateSubmit', this.onUpdateSubmit, this)
            this.updateView.on('cancel', this.onCancel, this)
        }
        this.mainModel.load(id, {
            scope: this,
            success: function (record, operation) {
                if (this.beforeUpdateView)this.beforeUpdateView(record);
                if (this.breadcrumbs) {
                    var href = this.name + '/view';
                    var breadcrumbsConf = {};
                    breadcrumbsConf[href] = {
                        href: href + id,
                        title: record.get('_title')
                    };
                    this.breadcrumbs.setBreadcrumbs(href, breadcrumbsConf);
                }
                this.updateView.getForm().loadRecord(record);
                if (this.getViewTitle) this.updateView.setTitle(this.getViewTitle(record));
            },
        })
        return this.updateView;
    },
    onProxyException: function (proxy, response, operation) {
        var errorpanel = this.wrapperView.getLayout().getActiveItem().down('errorpanel');
        if (!errorpanel) return;
        errorpanel.printErrors(response, this.label_name);
        errorpanel.show();

    },
    onCreateSubmit: function (data) {
        var newObj = new this.mainModel(data);
        newObj.save({
            scope: this,
            success: function () {
                this.createView.getForm().reset();
                this.createView.down('errorpanel').clear().hide();
                Admin.getApplication().navigateTo(this.name + '/list');
            },
            failure: function () {
                var errorpanel = this.wrapperView.getLayout().getActiveItem().down('errorpanel');
                errorpanel.setErrorTitle(l('error.incorrect_form_data'));

            }
        });
    },
    onUpdateSubmit: function (record) {
        if (!record) return;
        record.save({
            scope: this,
            success: function () {
                Admin.getApplication().navigateTo(this.name + '/list');
            },
            failure: function () {
                var errorpanel = this.wrapperView.getLayout().getActiveItem().down('errorpanel');
                errorpanel.setErrorTitle(l('error.incorrect_form_data'));

            }
        });
    },
    onEdit: function (record) {
        Admin.getApplication().navigateTo(this.name + "/edit/" + record.getId())
    },
    onView: function (record) {
        Admin.getApplication().navigateTo(this.name + "/view/" + record.getId())
    },
    onAdd: function (record) {
        Admin.getApplication().navigateTo(this.name + "/add")
    },
    onDelete: function (record) {
        if (!record) return;
        var store = this.mainStore;
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
        }
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
                })
            return
        }
        var store = this.mainStore;
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
        }
        Ext.MessageBox.confirm(l('common.confirm.delete.title'), l('common.confirm.delete.message'), afterConfirm);
    },
    onCancel: function () {
        var errorpanel = this.wrapperView.getLayout().getActiveItem().down('errorpanel');
        if (errorpanel) {
            errorpanel.clear();
            errorpanel.hide();
        }
        Admin.getApplication().navigateTo(this.name + "/list");
    }
});
