Ext.define('Pms.modules.settings.view.uploadWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.uploadWindow',
    requires: [
        'Ext.ux.upload.Dialog'
    ],
    title: l('settings.hotelPhoto'),
    width: 800,
    height: 500,
    bodyStyle: {
        backgroundColor: '#fff'
    },
    data: {},
    uploadType: null,
    roomTypeId: null,
    roomTypeStore: null,
    roomTypePhotos: [],

    initComponent: function () {
        var me = this;

        if (!me.uploadType) {
            Pms.App.showNotification({message: l('upload.noUploadType')});
        }
        else {
            me.items = [
                Ext.create('Ext.ux.upload.Panel', {
                    dialogTitle: '',//l('settings.photoUpload'),
                    uploadUrl: 'rest/document/upload',
                    uploadParams: {type: me.uploadType},
                    uploader: 'Ext.ux.upload.uploader.FormDataUploader'
                })
            ];
        }
        me.buttons = [
            {
                text: l('ok.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});