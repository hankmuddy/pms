Ext.define('Pms.modules.season.view.SeasonAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.seasonAddWindow',
    title: l('periodAdd'),
    resizable: false,
    autoShow: false,
    width: 300,
    height: 180,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=save]').fireHandler()
                },
                scope: this
            });
        }
    },

    data: {},

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'seasonForm',
                data: me.data
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                iconCls: 'app-icon-ok',
                action: 'add',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});