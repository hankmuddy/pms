Ext.define('Pms.modules.contact.view.ContactForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.contactForm',

    items: [
        {
            xtype: "hidden",
            name: "id"
        },
        {
            xtype: "hidden",
            name: "company"
        },
        {
            fieldLabel: l('fio') + Pms.requiredStatus,
            name: "name",
            allowBlank: false
        },
        {
            fieldLabel: l('email') + Pms.requiredStatus,
            name: "email",
            allowBlank: false
        },
        {
            fieldLabel: l('company.phone'),
            name: "phone",
            allowBlank: true
        },
        {
            fieldLabel: l('description'),
            name: "description",
            allowBlank: true
        },
        {
            fieldLabel: l('person.position'),
            name: "post",
            allowBlank: true
        }
    ]
});