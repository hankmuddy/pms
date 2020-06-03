Ext.define('Admin.modules.bookingButton.controller.Create', {
    extend: 'Admin.generic.app.controller.Create',
    modelClass: 'Admin.modules.bookingButton.Model',
    viewClass: 'Admin.modules.bookingButton.view.Create',
    onCreateSubmit: function (data) {
        var keyValues = [];
        for (var i in data.key) {
            keyValues.push({
                key: data.key[i],
                value: '#' + data.value[i]
            });
        }
        data.keyValues = keyValues;
        var newObj = new this.model(data);

        newObj.save({
            scope: this,
            success: this.onSuccessSubmit,
            failure: this.onFailureSubmit
        });
    }
});
