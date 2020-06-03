Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseOutgoForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseOutgoForm',

    layout: 'vbox',
    autoscroll: true,
    fileupload: true,
    isGroup: null,

    items: [
        {
            xtype: 'hidden',
            name: 'id'
        },
        {
            name: 'arrivalTime',
            xtype: 'timefield',
            format: 'H : i',
            submitFormat: 'H : i',
            fieldLabel: l('departTime') + Pms.requiredStatus,
            allowBlank: false,
            increment: 5,
            value: new Date(Pms.fromUTC(new Date(), true))
        },
        {
            xtype: 'checkboxfield',
            fieldLabel: l("isAllGroup"),
            inputValue: true,
            labelWidth: 320,
            width: 340,
            hidden: this.isGroup
        }
    ]
});
