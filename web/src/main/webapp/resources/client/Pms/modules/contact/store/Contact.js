Ext.define('Pms.modules.contact.store.Contact', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.contact.model.Contact',
    alias: 'widget.contactStore',
    url: 'rest/companyContact'
});