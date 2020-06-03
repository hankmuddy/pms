Ext.define('Pms.abstract.Chart', {
    extend: 'Ext.chart.Chart',
    alias: 'widget.abstractChart',
    storeParams: {},
    loadParams: {},
    filterParams: {},
    autoload: false,
    initComponent: function () {
        var me = this;
        me.storeParams.loadParams = me.loadParams;
        me.storeParams.filterParams = me.filterParams;

        me.store = Ext.create(me.store, me.storeParams);
        if(me.autoload){
            me.store.load();
        }
        this.callParent(arguments);
    }
});