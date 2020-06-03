Ext.define('Pms.abstract.field.Money', {
    extend: 'Ext.form.field.Number',
//    maskRe:  /^\d+\.?\d*$/,
    alias: ['widget.moneyfield'],
    mouseWheelEnabled: false,
    hideTrigger: true,
    emptyText: l('common.only_numeric'),
    enableKeyEvents: true,
    validateOnChange: true,
//    decimalPrecision: 2,
    decimalSeparator: '.',
    minValue: 0,
    step: 0.01,
    validator: function(value) {
        if(value >= 0) {
            return true
        }
        return l('common.wrongNumber')
    },
    getValue: function() {
        return parseInt(this.rawValue * 100)
    },
    setValue: function(value) {
        var me = this;
        if(!me.rawValue) {
//            me.value = value/100;
            me.setRawValue(value / 100);
        }
        else {
//            me.value = value;
            me.setRawValue(value);
        }
//        me.checkChange();
        return me;
    }
//    submitLocaleSeparator: false
});
