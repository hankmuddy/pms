Ext.define('Pms.modules.user.store.User', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.user.model.User',
    alias: 'widget.userStore',
    url: 'rest/user'
});