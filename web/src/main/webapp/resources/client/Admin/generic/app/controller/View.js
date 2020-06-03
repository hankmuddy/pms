Ext.define('Admin.generic.app.controller.View', {
    extend: 'Ext.app.Controller',
    run: function (conf) {
        this.stage = conf.view;
        if (conf.links) {
            this.links = conf.links;
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
    },
    initView: function () {
        this.view = Ext.create(this.viewClass)
    },
    setBreadcrumbs: function (record, conf) {
        if (this.breadcrumbs) {
            var href = this.links['view'];
            var breadcrumbsConf = conf || {};
            breadcrumbsConf[href] = {
                href: href + '/' + this.currentId,
                title: record.get(this._title || '')
            };
            this.breadcrumbs.setBreadcrumbs(href, breadcrumbsConf);
        }
    },
    setHandlersOnViewEvents: function () {
        this.view.on('edit', this.onEdit, this);
        this.view.on('post', this.onPost, this)
    },
    unsetHandlersOnViewEvents: function () {
        this.view.un('edit', this.onEdit, this);
        this.view.un('post', this.onPost, this)
    },
    onEdit: function () {
        Admin.getApplication().navigateTo(this.links['edit'] + '/' + this.currentId)
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
});
