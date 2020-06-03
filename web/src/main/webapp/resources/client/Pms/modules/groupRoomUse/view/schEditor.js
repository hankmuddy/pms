Ext.define('Pms.modules.groupRoomUse.view.schEditor', {
    extend: 'Sch.plugin.EventEditor',
    alias: 'widget.schEditor',

    height: 105,
    width: 280,

    initComponent: function () {
        Ext.apply(this, {
            timeConfig: {
                minValue: '00:00',
                maxValue: '24:00',
                increment: 60
            },
            hideOnBlur: false,
            dateConfig: {

            },
            dateFormat: 'd.m.Y',
            timeFormat: 'H:i',
            timeUnit: Sch.util.Date.DAY,
            durationUnit: Sch.util.Date.DAY,
            durationText: 'дней',
            durationConfig: {
                minValue: 1,
                maxValue: 730
            },
            buttonAlign: 'right',
            saveText: l('book'),
            deleteText: l('delete.btn'),
            cancelText: l('cancel.btn'),
            fieldsPanelConfig: {
                xtype: 'container',
                layout: 'card',
                items: [
                    {
                        // EventType: 'Meeting',
                        xtype: 'form',
                        layout: 'hbox',
                        style: 'background:#fff',
                        cls: 'editorpanel',
                        border: false,
                        items: [
                            {
                                padding: 10,
                                style: 'background:#fff',
                                border: false,
                                flex: 2,
                                layout: 'anchor',
                                defaults: {
                                    anchor: '100%'
                                },
                                items: [
                                    {
                                        //this.actionBox = new Ext.form.ComboBox({
                                        xtype: 'combobox',
                                        fieldLabel: l('action'),
                                        name: 'useType',
                                        store: Ext.create('Pms.modules.groupRoomUse.store.useType'),
                                        displayField: 'label',
                                        valueField: 'name',
                                        queryMode: 'remote',
                                        minChars: 1,
                                        listeners: {
                                            afterrender: function () {
                                                // this.setValue(this.defaultValue);
                                            }
                                        }
                                        //})
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            listeners: {
                beforeeventsave: function (editor, eventRecord, eOpts) {
                    var win = Ext.widget('groupRoomUseAddWindow');
                    var gruForm = win.down('groupRoomUseForm');
                    var gruData = eventRecord.data;
                    gruForm.getForm().setValues(gruData);
                },
                beforeeventdelete: function (editor, eventRecord, eOpts) {

                }
            }
        });
        this.callParent(arguments);
    },

    show: function (eventRecord) {
        this.callParent(arguments);
    }
});