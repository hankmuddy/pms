Ext.define('Admin.generic.panel.ErrorPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.errorpanel',
    hidden: true,
    items: [
        {
            html: '<div style="margin:5px;padding:5px;border:2px solid red; color: red; background: pink"><h4 class="error-title"></h4><ul class="error-list"></ul></div>',
        }
    ],
    getTitleEl: function () {
        return Ext.get(Ext.query('.error-title', this.body.dom)[0]);
    },
    getListEl: function () {
        return Ext.get(Ext.query('.error-list', this.body.dom)[0]);
    },
    printErrors: function (response, entityName) {
        this.clearErrorList();
        console.log(response);
        var errors = Ext.decode(response.responseText);
        if (!errors)return;
        errors.forEach(function (error) {
            var errorMsg = "";
            if (error.source) errorMsg = l('error.field') + ' ' + l(entityName + '.' + error.source) + ': ';
            if (error.code) errorMsg += l('error.' + error.code);
            this.getListEl().insertHtml('afterBegin', '<li>' + errorMsg + '</li>');
        }, this);
        this.doLayout();
    },
    setErrorTitle: function (text) {
        this.getTitleEl().setHTML(text);
        this.doLayout();
    },
    clearErrorTitle: function () {
        this.getTitleEl().setHTML("")
    },
    clearErrorList: function () {
        this.getListEl().setHTML("")
    },
    clear: function () {
        this.clearErrorTitle();
        this.clearErrorList();
        return this
    }
});
