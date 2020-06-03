Ext.define('Pms.modules.settings.model.Settings', {
    extend: 'Ext.data.Model',
    fields: [
//        'name',
//        'logo',
//        'postAddress',
//        'officialAddress',
//        'bookPhone',
//        'accountPhone',
//        'infoPhone',
//        'email',
//        'webSite',
//        'description',
//        'multiplier',
//        Pms.setDateFromMinModel('earlyCheckInStart'),
//        Pms.setDateFromMinModel('earlyCheckInEnd'),
//        Pms.setDateFromMinModel('lateCheckOutStart'),
//        Pms.setDateFromMinModel('lateCheckOutEnd'),
//        Pms.setDateFromMinModel('earlyCheckOutEnd'),
//        Pms.setDateFromMinModel('checkIn'),
//        Pms.setDateFromMinModel('checkOut'),
//        {name:'earlyCheckInEnd',type:'date',convert: function(v){
//            return new Date (new Date(0).setMinutes(v));
//        }},
//        {name:'lateCheckOutStart',type:'date',convert: function(v){
//            return new Date (new Date(0).setMinutes(v));
//        }},
//        {name:'lateCheckOutEnd',type:'date',convert: function(v){
//            return new Date (new Date(0).setMinutes(v));
//        }},
//        {name:'checkIn',type:'date',convert: function(v){
//            return new Date (new Date(0).setMinutes(v));
//        }},
//        {name:'checkOut',type:'date',convert: function(v){
//            return new Date (new Date(0).setMinutes(v));
//        }},
//        'timeZone',
//        'tourismTax',
        {
            name: 'hotelInfo',
            persist: false,
            convert: function (value) {
                value.checkOut = Pms.setDateFromMin(value.checkOut);
                value.checkIn = Pms.setDateFromMin(value.checkIn);
                value.lateCheckOutEnd = Pms.setDateFromMin(value.lateCheckOutEnd);
                value.lateCheckOutStart = Pms.setDateFromMin(value.lateCheckOutStart);
                value.earlyCheckInEnd = Pms.setDateFromMin(value.earlyCheckInEnd);
                value.earlyCheckInStart = Pms.setDateFromMin(value.earlyCheckInStart);
                return value;
            }
        },
        'facilities'
    ]
});