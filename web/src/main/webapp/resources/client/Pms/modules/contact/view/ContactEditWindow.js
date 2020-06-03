Ext.define('Pms.modules.contact.view.ContactEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.contactEditWindow',
    title: l('companyContactEdit'),
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