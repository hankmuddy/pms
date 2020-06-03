Ext.define('Admin.modules.hotel.view.Form', {
    extend: 'Admin.generic.form.FieldConfig',
    items: {
        name: {
            fieldLabel: l('hotel.name'),
            required: true
        },
        address: {
            fieldLabel: l('hotel.address'),
            required: true
        },
        city: {
            fieldLabel: l('city')
        },
        country: {
            fieldLabel: l('country')
        },
        hotelId: {
            fieldLabel: l('hotel.id')
        },
        wuName: {
            fieldLabel: l('hotel.wuName')
        },
        lcode: {
            fieldLabel: l('hotel.lcode')
        },
        postAddress: {
            fieldLabel: l('hotel.postAddress')
        },
        index: {
            fieldLabel: l('hotel.index')
        },
        officialAddress: {
            fieldLabel: l('hotel.officialAddress')
        },
        bookPhone: {
            fieldLabel: l('hotel.bookPhone')
        },
        accountPhone: {
            fieldLabel: l('hotel.accountPhone')
        },
        infoPhone: {
            fieldLabel: l('hotel.infoPhone')
        },
        email: {
            fieldLabel: l('hotel.email')
        },
        webSite: {
            fieldLabel: l('hotel.webSite')
        },
        description: {
            fieldLabel: l('hotel.description')
        },
        multiplier: {
            fieldLabel: l('hotel.multiplier')
        },
        tourismTax: {
            fieldLabel: l('hotel.tourismTax')
        },
        maxRooms: {
            fieldLabel: l('hotel.maxRooms')
        },
        vat: {
            fieldLabel: l('hotel.vat')
        },
        checkIn: {
            fieldLabel: l('hotel.info.checkIn')
        },
        earlyCheckIn: {
            fieldLabel: l('hotel.info.earlyCheckIn')
        },
        checkOut: {
            fieldLabel: l('hotel.info.checkOut')
        },
        lateCheckOut: {
            fieldLabel: l('hotel.info.lateCheckOut')
        }
    }
});
