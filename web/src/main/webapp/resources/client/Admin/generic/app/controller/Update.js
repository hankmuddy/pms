Ext.define('Admin.generic.app.controller.Update', {
    extend: 'Ext.app.Controller',
    run: function (conf) {
        this.stage = conf.view;
        if (conf.links) {
            this.links = conf.links;
        }
        if (conf.name) {
            this.name = conf.name;
        }
        if (conf.breadcrumbs) {
            this.breadcrumbs = conf.breadcrumbs
        }
        this.getId();
        this.initModel();
        this.initView();
        this.setHandlersOnViewEvents(this.view);
        this.loadData();
    },
    getId: function () {
        var id = this.currentId = Admin.generic.Utils.getHashPart(3);
        if (!Admin.generic.Utils.isValidId(id)) {
            console.error(this, 'Incorrect Id in Url');
            Admin.getApplication().notFound();
        }
    },
    initModel: function () {
        Ext.create(this.modelClass);
        this.model = Ext.ClassManager.get(this.modelClass);
        this.model.getProxy().on('exception', this.onProxyException, this);
    },
    initView: function () {
        this.view = Ext.create(this.viewClass)
    },
    setBreadcrumbs: function (record) {
        if (this.breadcrumbs) {
            var href = this.links['edit'];
            var breadcrumbsConf = {};
            breadcrumbsConf[href] = {
                href: href + '/' + this.currentId,
                title: record.get(this._title || '')
            };
            this.breadcrumbs.setBreadcrumbs(href, breadcrumbsConf);
        }

    },
    setHandlersOnViewEvents: function () {
        this.view.on('updateSubmit', this.onUpdateSubmit, this)
        this.view.on('cancel', this.onCancel, this)
    },
    unsetHandlersOnViewEvents: function () {
        this.view.un('updateSubmit', this.onUpdateSubmit, this)
        this.view.un('cancel', this.onCancel, this)
    },
    onCreateSubmit: function (data) {
        var newObj = new this.model(data);
        newObj.save({
            scope: this,
            success: function () {
                this.view.getForm().reset();
                this.view.down('errorpanel').clear().hide();
                Admin.getApplication().navigateTo(this.links['list']);
            },
            failure: function () {
                var errorpanel = this.view.down('errorpanel');
                errorpanel.setErrorTitle(l('error.incorrect_form_data'));

            }
        });
    },
    onUpdateSubmit: function (record) {
        if (!record) return;
        record.save({
            scope: this,
            success: function () {
                Admin.getApplication().navigateTo(this.links['list']);
            },
            failure: function () {
                var errorpanel = this.view.down('errorpanel');

                errorpanel.setErrorTitle(l('error.incorrect_form_data'));
            }
        });
    },
    onCancel: function () {
//        history.back();
        var errorpanel = this.view.down('errorpanel')
        if (errorpanel) {
            errorpanel.clear();
            errorpanel.hide();
        }
        Admin.getApplication().navigateTo(this.links['list']);
    },
    onProxyException: function (proxy, response, operation) {
        var errorpanel = this.view.down('errorpanel');
        if (!errorpanel) return;
        errorpanel.printErrors(response, this.name);
        errorpanel.show();

    },
    loadData: function () {
        this.model.load(this.currentId, {
            scope: this,
            success: this.onSuccessLoad
        })

    },
    onSuccessLoad: function (record, operation) {
        this.currentRecord = this.convertBeforeRender(record);
        this.setBreadcrumbs(this.currentRecord);
        this.view.getForm().loadRecord(this.currentRecord);
        this.stage.add(this.view);
    },
    convertBeforeRender: function (record) {
        return record;
    }
})
