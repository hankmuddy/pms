Ext.define('Pms.modules.restriction.store.Restriction', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.restriction.model.Restriction',
    alias: 'widget.restrictionStore',
    url: 'rest/compactRestriction'
});