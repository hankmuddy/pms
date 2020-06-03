Ext.define('Pms.abstract.field.Currency', {
    extend: 'Ext.form.field.Number',
    alias: ['widget.currencyField'],

    config: {
        thousandSeparator: ' ',
        currencyAtEnd: false
    },

    listeners: {
        focus: function (me, eOpts) {
            me.inputEl.dom.value = this.getValue();
        }
    },

    valueToCurrency: function (value) {
        var format = Ext.util.Format;
        format.currencyPrecision = this.decimalPrecision;
        format.thousandSeparator = this.thousandSeparator;
        format.currencySign = this.currencySign;
        format.currencyAtEnd = true;
        return format.currency(value);
    },

    valueToRaw: function (value) {
        return this.valueToCurrency(value);
    },

    processRawValue: function (value) {
        return this.parseValue(this.callParent(arguments));
    },

    parseValue: function (value) {
        value = String(value).replace(this.thousandSeparator, "");
        value = parseFloat(String(value).replace(this.decimalSeparator, '.'));
        return isNaN(value) ? null : value;
    }
});