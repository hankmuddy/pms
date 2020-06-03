Ext.define('Admin.modules.hotel.view.LocationMap', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.locationMap',
    requires: ['Admin.generic.ux.GMapPanel'],

//    layout: 'fit',
    autoscroll: true,
    fileupload: true,
    width: 450,
    height: 450,
    data: null,
    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'gmappanel',
                center: {
                    geoCodeAddr: 'Kiev, Kyiv city, Ukraina',
                    marker: {
                        title: 'Kiev city'
                    }
                },
                height: 450,
                width: 450,
                markers: [

                ],
                searchBox: true
            }
//            Ext.create('Ext.Component', {
//                autoEl: 'input',
////                width: 300,
////                padding: 20,
//                padding: '10px 11px 0 13px',
//                margin: '30px 0 0 0',
//                style: {
//                    backgroundColor: '#fff',
//                    lineHeight: 15
//                },
//                renderTo: Ext.getBody()
//            })

        ]
        ;
        this.callParent(arguments);
    }
});
