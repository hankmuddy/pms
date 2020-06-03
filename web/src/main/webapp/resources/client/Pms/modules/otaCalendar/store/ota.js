Ext.define('Pms.modules.otaCalendar.store.ota', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.otaCalendar.model.ota',
    alias: 'widget.otaStore',
    url: "rest/baseRoomValue"
});