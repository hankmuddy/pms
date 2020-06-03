Ext.define('Pms.modules.person.view.ImportContactsWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.importContactsWindow',
    title: l('person.import'),
    width: 340,
    height: 460,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=import]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'importContactsForm'
            }
        ];

        me.buttons = [
            {
                text: l('import.btn'),
                action: 'import',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent();
    }
});