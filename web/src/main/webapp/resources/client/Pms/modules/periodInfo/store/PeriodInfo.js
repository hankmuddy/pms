Ext.define('Pms.modules.periodInfo.store.PeriodInfo', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.periodInfo.model.PeriodInfo',
    alias: 'widget.periodInfoStore',
    url: 'rest/periodRoomTypeInfo'
});