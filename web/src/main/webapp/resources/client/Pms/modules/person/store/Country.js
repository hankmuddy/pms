Ext.define("Pms.modules.person.store.Country", {
    extend: "Pms.abstract.Store",
    alias: 'widget.countryStore',
    fields: [
        'abbr', 'country'
    ],
    autoLoad: 'true',
    data: countries
//    proxy: {
//        type: 'ajax',
//        url : '/client/JSON/countries.json',
//        reader: {type: 'json'},
//        writer: {type: 'json'}
//    },
});