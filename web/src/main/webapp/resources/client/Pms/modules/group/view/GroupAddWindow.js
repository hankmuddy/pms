Ext.define('Pms.modules.group.view.GroupAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupAddWindow',

    requires: ['Pms.modules.group.view.GroupForm'],

    title: l('newCompany'),
    width: 700,
    height: 450,

    initComponent: function () {
        this.items = [
            Ext.widget('groupForm')
        ];

        this.callParent(arguments);
    }
});