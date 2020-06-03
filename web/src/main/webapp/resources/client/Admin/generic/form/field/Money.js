Ext.define('Admin.generic.form.field.Money', {
    extend: 'Ext.form.field.Number',
//    maskRe:  /^\d+\.?\d*$/,
    alias: 'widget.moneyfield',
    mouseWheelEnabled: false,
    hideTrigger: true,
    emptyText:l('common.only_numeric'),
    enableKeyEvents: true,
    validateOnChange: true,
    decimalPrecision: 2,
    validator: function(value){
        if(value >= 0){
            return true
        }
        return l('common.wrongNumber')
    }
});
