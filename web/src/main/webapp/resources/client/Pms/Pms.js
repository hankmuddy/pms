var Pms = {
    version: "0.1",
//    wallpaper: 'themes/default/images/wallpaper.png',
    logoSrc: 'themes/default/images/logo_pms.png',
    logo: '<img src="themes/default/images/logo_pms.png" />',
    grayIcons: 'themes/default/images/icons/gray/icons/',
    iconOk: '<img src="themes/default/images/icons/gray/icons/ok.png" />',
    notificationOk: '<img src="themes/default/images/icons/gray/icons/notification-ok.png" />',
    notificationError: '<img src="themes/default/images/icons/gray/icons/notification-error.png" />',
    iconAccept: '<img src="themes/default/images/icons/accept.png" alt="Утвержден"/>',
    iconNotAccept: '<img src="themes/default/images/icons/gears.png" alt="Не утвержден"/>',
    iconError: '<img src="themes/default/images/icons/gray/icons/error.png" />',
    iconInfo: '<img src="themes/default/images/icons/gray/icons/info.png" />',
    iconRemove: '<img src="themes/default/images/icons/gray/icons/remove.png" />',
    iconPrice: '<img src="themes/default/images/icons/gray/icons/priceing.png" />',
    iconCross: '<img src="themes/default/images/icons/gray/icons/cross.gif" />',
    iconSave: '<img src="themes/default/images/icons/16/floppy_disk_16.png" />',
    emptyImgSrc: 'themes/default/images/empty-photo.jpg',
    requiredStatus: '<span style="color:red;font-weight:bold" data-qtip="' + l('common.required') + '">*</span>',
    facilities: {},
    allFacilities: {},
    timeZoneOffset: _('timeZone'),
    showHints: false,

    fromUTC: function (v, withOffset) {
        var offset = withOffset ? _('timeZone') : 0;
        if (Ext.isEmpty(v)) return null;
        v = v.toString().length > 10 ? v : v * 1000;
//        console.log('fromUTC================================================');
        var _date = new Date(v);
//        console.log('_date', _date, '(' + v + ')');

        var _userOffset = _date.getTimezoneOffset() * 60;
//        console.log('_userOffset', _userOffset);

        var _getTime = _date.getTime();
//        console.log('_getTime', _getTime);

        var _Time = new Date(parseInt(_getTime) + parseInt(_userOffset) * 1000 + parseInt(offset));
//        console.log('_Time', _Time, Date.parse(_Time) / 1000);
//        console.log('=======================================================');

        return _Time;
    },
    toUTC: function (v, withOffset) {
        var offset = withOffset ? _('timeZone') / 1000 : 0;
        if (Ext.isEmpty(v)) return null;

//        console.log('toUTC--------------------------------------------------');
        var _date = v;
        if (!(_date instanceof Date)) {
//            console.log('input_date:', _date);
            _date = new Date(parseInt(_date) * 1000);
        }
//        console.log('_date:', _date);

        var _userOffset = _date.getTimezoneOffset() * 60;
//        console.log('_userOffset', _userOffset);

        var _getTime = _date.getTime();
//        console.log('_getTime', _getTime);

        var _time = parseInt(_getTime) / 1000 - parseInt(_userOffset) - parseInt(offset);
//        console.log('_time', _time);
//        console.log('-------------------------------------------------------');
        return _time;
    },
    convertUTC: function (v, rec) {
//        console.log('convertUTC+++++++++++++++++++++++++++++++++++++++++++++');
        var _Time = this.fromUTC(v);
//        console.log('+++++++++++++++++++++++++++++++++++++++++++++++++++++++');
        return _Time;
    },
    serializeUTC: function (v, rec) {
        var offset = 0;//_('timeZone')
        if (Ext.isEmpty(v)) return null;

//        console.log('serializeUTC*******************************************');
        var _date = v;
//        console.log('_date:', _date);

        var _userOffset = _date.getTimezoneOffset() * 60;
//        console.log('_userOffset', _userOffset);

        var _getTime = _date.getTime();
//        console.log('_getTime', _getTime);

        var _time = parseInt(_getTime) / 1000 - 2 * parseInt(_userOffset) - 2 * parseInt(offset);
//        console.log('_time', _time);
//        console.log('*******************************************************');
        return _time;
    },
    money: function (name) {
        return {name: name, type: 'int',
            serialize: function (v) {
                return Math.ceil(v * 100);
            },
            convert: function (v) {
                return parseFloat(v) / 100;
            }}
    },
    moneyEditor: {
        xtype: 'numberfield',
        alias: 'widget.moneyEditor',
        getValue: function () {
            return Math.ceil(this.rawValue * 100)
        },
        mouseWheelEnabled: false,
        hideTrigger: true,
        emptyText: l('common.only_numeric'),
        enableKeyEvents: true,
        validateOnChange: true,
        decimalPrecision: 2,
        decimalSeparator: '.',
        minValue: 0,
        step: 0.01
    },
    setDateFromMinModel: function (name) {
        return {
            name: name,
            type: 'date',
            convert: function (v) {
                return new Date(new Date(0).setMinutes(v + new Date(0).getTimezoneOffset()));
            }
        }
    },
    setDateFromMin: function (val) {
        if (val == null) return null
        var offset = new Date(0).getTimezoneOffset();
        var val = new Date(new Date(0).setMinutes(val + offset));
        return val
    },

    stringFieldUseNull: function (name) {
        return {name: name, type: 'string', serialize: function (val) {
            if (val == '') return null
            return val
        }}
    },

    bankDetailsRequired: function (res, options) {
        var code = res.responseText ? JSON.parse(res.responseText)[0].code : res.code;
        if (code == 'refund.bankDetails.required') {
            var bankDetailsWin = Ext.create('Pms.modules.bankDetails.view.BankDetailsSelectWindow', {
                url: options.url,
                data: options.jsonData,
                success: options.onSuccessCallback || Ext.emptyFn,
                method: options.method || 'POST'
            });
            bankDetailsWin.show();
        }
        else {
            Pms.App.showNotification({
                message:l(code)
            });
        }
    },
    createPreloader: function () {
        var loading = document.createElement('div'),
            img = document.createElement('img');
//        img.width = 120;
//        img.height = 120;
        img.src = 'themes/default/images/preloader.gif';
        loading.id = 'custom-preloader';
        loading.appendChild(img);
        Ext.getBody().appendChild(loading);
    },
    removePreloader: function () {
        var loading = document.getElementById('custom-preloader');
        if(loading){
            loading.parentNode.removeChild(loading);
        }
    },
    checkForCheckInOut: function (checkIn, val) {
        if(!val) return ''
        var start = checkIn ? _('hotel').earlyCheckInStart : _('hotel').lateCheckOutStart,
            end = checkIn ? _('hotel').earlyCheckInEnd : _('hotel').lateCheckOutEnd,
            valDate = new Date(val*1000);
        val = valDate.getMinutes() + 60 * valDate.getHours() + new Date().getTimezoneOffset();
        if(start <= val && val < end){
            return Pms.iconOk
        }
        return Pms.iconCross
    }
};
