Ext.define('Pms.abstract.field.MoneyColumn', {
    extend: 'Ext.grid.column.Number',
    alias: ['widget.moneycolumn'],
    renderer: function(val) {
        return Ext.util.Format.number(val ? val / 100 : 0, '0.00');
    }
})