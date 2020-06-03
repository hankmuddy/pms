Ext.define('Pms.modules.settings.store.Settings', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.settingsStore',
    model: 'Pms.modules.settings.model.Settings',
    url: 'rest/settings',
    extravailable: true
});