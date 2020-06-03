Ext.define('Admin.generic.Error', {
    singleton: true,
    handle: function (error) {
        if (this[error.name + 'Handler']) {
            this[error.name + 'Handler'](error)
        }
    },
    throw: function (errorName, params) {
        throw new this[errorName](params);
    },
    restartApplication: function (conf) {
        this.name = "restartApplication";
        this.controller = conf.controller;
        this.action = conf.action;
        this.params = conf.params;
    },
    restartApplicationHandler: function (error) {
        Admin.getApplication().start({controller: error.controller, action: error.action, params: error.params})
    }
});
