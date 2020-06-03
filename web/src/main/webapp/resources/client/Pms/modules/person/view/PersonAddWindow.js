Ext.define('Pms.modules.person.view.PersonAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.personAddWindow',
    title: l('person.addPerson'),
    resizable: false,
    width: 1230,
    height: 550,

    initComponent: function () {
        this.items = [
            {
                xtype: 'personForm'
            }
        ];

        this.callParent(arguments);
    }
});