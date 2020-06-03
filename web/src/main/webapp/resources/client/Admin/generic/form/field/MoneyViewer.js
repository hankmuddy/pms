Ext.define('Admin.generic.form.field.MoneyViewer', {
    extend: 'Ext.form.field.Display',
    alias: 'widget.moneyviewer',
    renderer: function(val){
        if(!val)return '';
        return Ext.util.Format.currency(val,new String(""),2);
    }
});