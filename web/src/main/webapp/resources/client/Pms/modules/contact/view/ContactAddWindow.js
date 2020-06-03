Ext.define('Pms.modules.contact.view.ContactAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.contactAddWindow',
    title: l('newCompany'),
    width: 350,
    height: 220,

    initComponent: function () {
        this.items = [
            Ext.widget('contactForm')
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);
    }
});