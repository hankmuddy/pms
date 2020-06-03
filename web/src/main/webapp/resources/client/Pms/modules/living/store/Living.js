Ext.define('Pms.modules.living.store.Living', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.living.model.Living',
    alias: 'widget.livingStore',
    url: 'rest/living',
    sorters: [{
        property: 'id',
        direction: 'ASC'
    }],

    extravailable: true
});