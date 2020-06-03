Ext.define('Pms.modules.settings.view.galleryUploadForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.galleryUploadForm',
//    requires: ['Pms.abstract.field.filesfield'],

    autoScroll: false,
    fileUpload: true,
    frame: true,

    data: {},

    url: 'rest/document/upload',

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'filefield',
                fieldLabel: l('imageUpload'),
                labelWidth: 135,
                width: 600,
                name: 'file',
                margin: 5
            }
        ];

        me.callParent();
    }
});