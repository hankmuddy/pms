Ext.define('Pms.modules.group.store.Group', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.group.model.Group',
    alias: 'widget.groupStore',
    url: 'rest/customerGroup'
});