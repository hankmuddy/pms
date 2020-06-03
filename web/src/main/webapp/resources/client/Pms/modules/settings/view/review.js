Ext.define('Pms.modules.settings.view.review', {
    extend: 'Pms.abstract.Panel',
//    extend: 'Ext.view.View',
    alias: 'widget.review',

    overflowY: 'scroll',
    overflowX: false,
    width: '95%',
    border: 0,

    data: {},

    initComponent: function () {
        var me = this,
            hotelInfo = me.data.hotelInfo,
            facilities = me.groupFacilities(me.data.facilities),
            facilityItems = [];

        for (var groupName in facilities) {
            var names = Ext.Array.pluck(facilities[groupName], 'name');
            Ext.Array.forEach(names, function (item, index, array) {
                array[index] = l(item);
            });
            facilityItems.push({
                html: '<b>' + l('facility.' + groupName) + '</b><p>' + names.join(', ') + '</p>'
            });
        }

        me.items = [
            {
                xtype: 'container',
                layout: 'hbox',
                margin: 5,
                border: '0 0 1 0',
                style: {
                    borderColor: '#ddd'
                },
                items: [
                    {
                        //logo
                        xtype: 'panel',
                        margin: 5,
                        border: 0,
                        width: '20%',
                        height: 125,
                        layout: 'fit',
                        data: {src: hotelInfo.logo ? _('imagesUrlPrefix') + hotelInfo.logo : Pms.logoSrc},
                        tpl: ['<img src="{src}" style="max-width:100%;max-height:100%;">']
                    },
                    {
                        //title, contacts
                        xtype: 'panel',
                        margin: 5,
                        border: 0,
                        width: '50%',
                        data: hotelInfo,
                        tpl: [
                            '<span style="float:left;">',
                            '<h1>{name}</h1>',
                            '<p>',
                            '<tpl if="webSite"><a href="{webSite}">{webSite}</a><br /></tpl>',
                            '<tpl if="officialAddress">{officialAddress}<br /></tpl>',
                            '<tpl if="postAddress"><tpl if="postAddress != officialAddress">' + l('hotel.postAddress') + ': {postAddress}<br /></tpl></tpl>',
                            '<tpl if="infoPhone">' + l('hotel.infoPhone') + ': {infoPhone}<br /></tpl>',
                            '<tpl if="bookPhone">' + l('hotel.bookPhone') + ': {bookPhone}<br /></tpl>',
                            '<tpl if="accountPhone">' + l('hotel.accountPhone') + ': {accountPhone}<br /></tpl>',
                            '<tpl if="webSite"><a href="{email}">{email}</a><br /></tpl>',
                            '</p>',
                            '</span>'
                        ]
                    },
                    {
                        //checkIn-checkOut
                        xtype: 'container',
                        border: 0,
                        width: '30%',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'container',
                                border: 0,
                                width: '50%',
                                items: [
                                    {
                                        //checkIn
                                        xtype: 'panel',
                                        title: l('hotel.info.checkIn'),
                                        margin: 5,
                                        bodyPadding: 5,
                                        html: hotelInfo.checkIn ? Ext.Date.format(hotelInfo.checkIn, 'H:i') : '&mdash;',
                                    },
                                    {
                                        //checkOut
                                        xtype: 'panel',
                                        title: l('hotel.info.checkOut'),
                                        margin: 5,
                                        bodyPadding: 5,
                                        html: hotelInfo.checkOut ? Ext.Date.format(hotelInfo.checkOut, 'H:i') : '&mdash;',
                                    }
                                ]
                            },
                            {
                                xtype: 'container',
                                border: 0,
                                width: '50%',
                                items: [
                                    {
                                        //earlyCheckIn
                                        xtype: 'panel',
                                        title: l('hotel.info.earlyCheckIn'),
                                        margin: 5,
                                        bodyPadding: 5,
                                        html: hotelInfo.earlyCheckInStart && hotelInfo.earlyCheckInEnd ? Ext.Date.format(hotelInfo.earlyCheckInStart, 'H:i') + ' - ' + Ext.Date.format(hotelInfo.earlyCheckInEnd, 'H:i') : '&mdash;',
                                    },
                                    {
                                        //lateCheckOut
                                        xtype: 'panel',
                                        title: l('hotel.info.lateCheckOut'),
                                        margin: 5,
                                        bodyPadding: 5,
                                        html: hotelInfo.lateCheckOutStart && hotelInfo.lateCheckOutEnd ? Ext.Date.format(hotelInfo.lateCheckOutStart, 'H:i') + ' - ' + Ext.Date.format(hotelInfo.lateCheckOutEnd, 'H:i') : '&mdash;'
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'container',
                layout: 'hbox',
                defaults: {
                    xtype: 'container',
                    width: '50%',
//                    padding: 5,
                    margin: 5
                },
                items: [
                    {
                        //photos, roomTypes
                        items: [
                            {
                                //hotelPhotos
                                xtype: 'panel',
                                title: l('settings.hotelPhoto'),
                                bodyPadding: 5,
                                collapsed: false,
                                collapsible: true,
                                items: [
                                    {
                                        xtype: 'gallery',
                                        data: me.data,
                                        type: 'hotel',
                                        imgViewHeight: 250,
                                        thumbsHeight: 150
                                    }
                                ]
                            },
                            {
                                // roomtypes
                                xtype: 'container',
                                border: 0,
                                items: me.buildRoomTypes()
                            },
                            {
                                //bookingRules
                                xtype: 'panel',
                                title: l('settings.bookingRules'),
                                bodyPadding: 5,
                                margin: '5 0 0 0',
                                html: '<h4>' + l('settings.bookingRules') + '</h4>'
                            }
                        ]
                    },
                    {
                        items: [
                            {
                                //hotelDescription
                                xtype: 'panel',
                                title: l('settings.description'),
                                bodyPadding: 5,
                                data: hotelInfo,
                                tpl: '{description}',
                                hidden: hotelInfo.description ? false : true,
//                                overflowX: 'auto',
//                                overflowY: 'auto',
                                autoScroll: true
                            },
                            {
                                // facilities,
                                title: l('facilities'),
                                margin: '10 0 0 0',
                                border: 1,
                                padding: 0,
                                defaults: {
                                    xtype: 'panel',
                                    bodyStyle: {
                                        border: '0px',
                                        borderBottom: '1px solid #ddd'
                                    },
                                    bodyPadding: '5 10 0 10'
                                },
                                items: facilityItems,
                                hidden: Ext.isEmpty(facilityItems) ? true : false
                            }
                        ]
                    }
                ]
            }
        ];

        me.callParent();
    },

    getRoomTypes: function () {
        var records = [];
        /*,
         filter = Pms.Ajax.encode({filter: [{
         field: 'approved',
         comparison: 'eq',
         data: {
         type: 'BOOLEAN',
         value: true
         }
         }]
         });*/
//        console.log(filter);
        Pms.Ajax.request({
            url: 'rest/roomType',//?' + filter,
            method: 'GET',
            async: false,
            success: function (response) {
                records = response.content;
            },
            failure: function () {
                Pms.App.showNotification({
                    message: l('review.noRoomTypeData')
                });
            }
        });
        return records;
    },

    buildRoomTypes: function () {
        var me = this,
            roomTypes = me.getRoomTypes(),
            items = [];

        for (var i in roomTypes) {
            items.push({
                xtype: 'panel',
                layout: null,
                border: 1,
                margin: '10 0 10 0',
                collapsible: true,
                collapsed: true,
                title: roomTypes[i].name,
                overflowY: 'auto',
                overflowx: false,
                items: [
                    {
                        xtype: 'gallery',
                        roomTypeId: roomTypes[i].id,
                        data: me.data,
                        type: 'roomType',
                        margin: 5,
                        imgViewHeight: 250,
                        thumbsHeight: 80,
                        photoSelect: true
                    },
                    {
                        xtype: 'panel',
                        border: 0,
                        margin: 6,
                        data: roomTypes[i],
                        tpl: [
                            '<div>',
                            '<tpl if="adults"><b>' + l('booking.adults') + ':</b> {adults}<br /></tpl>',
                            '<tpl if="children"><b>' + l('desktop.children') + ':</b> {children}<br /></tpl>',
                            '<tpl if="additional"><b>' + l('roomType.createDetails') + ':</b> {additional}<br /></tpl>',
                            '<tpl if="defaultPrice"><b>' + l('virtualRoom.defaultPrice') + ':</b> {defaultPrice/100}</tpl>',
                            '</div>'
                        ]
                    }
                ]
            });
        }

        return items;
    },

    groupFacilities: function (content) {
        var allItems = content,
            groupedItems = {};

        for (var i in allItems) {
            var nameArr = allItems[i].facility.name.split('.'),
                groupName = nameArr[1];

            if (!groupedItems[groupName]) groupedItems[groupName] = [];

            allItems[i].facility.chargeFree = allItems[i].chargeFree;

            groupedItems[groupName].push(allItems[i].facility);
        }

        return groupedItems;
    }
});