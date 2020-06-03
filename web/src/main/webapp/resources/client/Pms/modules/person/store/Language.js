Ext.define("Pms.modules.person.store.Language", {
    extend: "Pms.abstract.Store",
    alias: 'widget.languageStore',
    fields: [
        'code', 'language'
    ],
    data: [
        {code: 'ru', language: 'Русский'},
        {code: 'uk', language: 'Українська'},
        {code: 'en', language: 'English'},
    ],
    autoLoad: true


//    extraParams: {enum_id: 3}
});