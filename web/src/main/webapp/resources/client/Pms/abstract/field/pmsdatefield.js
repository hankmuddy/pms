Ext.define('Pms.abstract.field.pmsdatefield', {
    extend: 'Ext.form.field.Date',
    alias: 'widget.pmsdatefield',
    onTriggerClick: function() {
        var me = this;
        Ext.form.DateField.prototype.onTriggerClick.apply(me, arguments);
        var picker = me.getPicker();
//        console.log(picker);
        picker.todayBtn.hide();
        picker.setHeight(195);
    }
});