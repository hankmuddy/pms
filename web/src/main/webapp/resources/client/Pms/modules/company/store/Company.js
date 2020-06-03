Ext.define('Pms.modules.company.store.Company', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.company.model.Company',
    alias: 'widget.companyStore',
    url: 'rest/company',
    sorters: [
        {
            property: 'name',
            direction: 'ASC'
        }
    ],
    extravailable: true
});