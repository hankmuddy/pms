Ext.define('Pms.modules.restriction.view.RestrictionAddWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.restrictionAddWindow',
    title: l('restriction.addWinTittle'),
    width: 400,
    height: 280,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=add-restriction]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'restrictionForm'
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'add-restriction',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});