Ext.define('Pms.modules.role.store.Role', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.role.model.Role',
    alias: 'widget.roleStore',
    url: 'rest/role'
});