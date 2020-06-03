Ext.define('Pms.modules.person.store.Person', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.person.model.Person',
    alias: 'widget.personStore',
    url: 'rest/adult',
    sorters: [{
        property: 'lastName',
        direction: 'ASC'
    }],
    extravailable: true
});