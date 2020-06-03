Ext.define('Pms.Lookup', {
    statics: {
        get: function () {
            return this.data;
        },
        getById: function (id) {
            if (id in this.data) return this.data[id];
            return null;
        },
        getLookupValue: function (lookupType, id) {
            // var item = this.getById(id);
            // if (item)return item.value
            // return "";
            var res,
                items = this.data[lookupType];
            items.some(function (item) {
                if (item.id == id) {
                    res = item.value;
                    return true
                }
            });

            return res;
        },

        getByType: function (type) {
            // var id, items = []
            // for (id in this.data) {
            //     var item = this.data[id];
            //     if (item.type == type)items.push(item);
            // }
            // return items;
            return this.data[type];
        },
        getByTypeAndCode: function (type, code) {
            var id, items = []
            for (id in this.data) {
                var item = this.data[id];
                if (item.type == type && item.code == code) {
                    return item;
                }
            }
            return null;
        },
        data: {
            entryType: ['VISA', 'RESIDENCE_PERMIT', 'TEMP_RESIDENCE_PERMIT', 'NO_DOCUMENT'],
            visaType: [
                'UNDEFINED',
                'ENTRY',
                'ENTRY_EXIT',
                'COMMON',
                'COMMON_PRIVATE',
                'COMMON_BUSINESS',
                'COMMON_TOURIST',
                'COMMON_TOURIST_GROUP',
                'COMMON_STUDY',
                'COMMON_WORKING',
                'COMMON_HUMANITARIAN',
                'POLITICAL_ASYLUM',
                'DUTY',
                'DIPLOMATIC',
                'TRANSIT_1',
                'TRANSIT_2',
                'TEMPORARY_RESIDENCE'
            ],
            closed: [ 'OPEN', 'CLOSED', 'CLOSED_TO_CHECKIN'],
            paymentType: ['CARD', 'CASH', 'TRANSFER', 'INTERNATIONAL_ACCOUNT'],
            useType: ['BOOKING_FREE', 'BOOKING_WARRANTY', 'LIVING', 'OUTGO', 'REFUSE', 'NOT_ARRIVED'],
            boardType: ['NB', 'BB', 'HB', 'FB', 'AI'],
            language: ['ru', 'en', 'uk'],
            pov: ['TOURISM', 'BUSINESS'],
            closed: ['OPEN', 'CLOSED', 'CLOSED_TO_CHECKIN'],
            variation: [ 'FIXED_DISCOUNT', 'PERCENTAGE_DISCOUNT', 'FIXED_INCREASE', 'PERCENTAGE_INCREASE'],
            smoking: ['SMOKING', 'NON_SMOKING', 'BOTH'],
            source: ['FRONT_DESK', 'EXPEDIA', 'BOOKING', 'HOTEL_DE', 'ITWG', 'IN_ITALIA', 'HOTELS_COM', 'RESERVER_IT', 'ACCOMODATIONZ_COM',
                'HOTEL_BEDS', 'VENERE_COM', 'HRS_COM', 'TRAVEL_EUROPE', 'ATRAPALO', 'MBE_TRAVEL', 'ESCAPIO', 'BB_PLANET', 'SPLENDIA', 'AGODA',
                'HOSTELS_CLUB', 'LASTMINUTE', 'TRAVELOCITY', 'SABRE', 'BUDGETPLACES', 'ORBITZ', 'LATE_ROOMS', 'IN_TOSCANA', 'HOSTEL_WORLD', 'BOOKING_BUTTON','OSTROVOK']
        }
    }
}, function () {
    for (var type in this.data) {
        var codes = this.data[type];
        this.data[type] = [];
        codes.forEach(function (item) {
            this.data[type].push({id: item, value: l(type + '.' + item)})
        }, this)
    }
});
