Ext.define('Pms.modules.accommodation.store.Accommodation', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.accommodationStore',
    model: 'Pms.modules.accommodation.model.Accommodation',
    autoSync: false,
    url: 'rest/accommodation'
});