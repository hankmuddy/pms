Ext.define('Admin.generic.app.controller.Create', {
    extend: 'Ext.app.Controller',
    run: function (conf) {
        this.stage = conf.view;
        if (conf.name) {
            this.name = conf.name;
        }
        if (conf.links) {
            this.links = conf.links;
        }
        if (conf.breadcrumbs) {
            this.setBreadcrumbs(conf.breadcrumbs)
        }
        this.initModel();
        this.initView();
        this.setHandlersOnViewEvents(this.view);
        this.stage.add(this.view);
    },
    initModel: function () {
        Ext.create(this.modelClass);
        this.model = Ext.ClassManager.get(this.modelClass);
        this.model.getProxy().on('exception', this.onProxyException, this);
    },
    initView: function () {
        this.view = Ext.create(this.viewClass)
    },
    setBreadcrumbs: function (breadcrumbs) {
        history.replaceState(null, null, '#' + this.links['add']);
        breadcrumbs.setBreadcrumbs(this.links['add'])

    },
    setHandlersOnViewEvents: function () {
        this.view.on('createSubmit', this.onCreateSubmit, this)
        this.view.on('cancel', this.onCancel, this)
    },
    unsetHandlersOnViewEvents: function () {
        this.view.un('createSubmit', this.onCreateSubmit, this)
        this.view.un('cancel', this.onCancel, this)
    },
    onCreateSubmit: function (data) {
        var newObj = new this.model(data);
        newObj.save({
            scope: this,
            success: this.onSuccessSubmit,
            failure: this.onFailureSubmit
        });
    },
    onSuccessSubmit: function () {
        history.back();
    },
    onFailureSubmit: function () {
        var errorpanel = this.view.down('errorpanel');
        errorpanel.setErrorTitle(l('error.incorrect_form_data'));
    },
    onCancel: function () {
//        history.back();
        // var errorpanel = this.wrapperView.getLayout().getActiveItem().down('errorpanel');
        var errorpanel = this.view.down('errorpanel')
        if (errorpanel) {
            errorpanel.clear();
            errorpanel.hide();
        }
        // Admin.getApplication().navigateTo(this.name + "/list");
        Admin.getApplication().navigateTo(this.links['list']);
    },
    onProxyException: function (proxy, response, operation) {
        var errorpanel = this.view.down('errorpanel')
        if (!errorpanel) return;
        errorpanel.printErrors(response, this.name);
        errorpanel.show();

    }
});