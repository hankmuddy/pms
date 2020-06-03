Ext.define('Admin.generic.Utils', {
    singleton: true,
    getHashPart: function (n) {
        var hash = location.hash;
        if (hash[0] == '#')hash = hash.slice(1);
        var path_array = hash.split('/');
        if (path_array[n - 1]) {
            return path_array[n - 1];
        }
        else return null
    },
    getIdFromHash: function (position) {
        var id = this.getHashPart(position);
        if (!Admin.generic.Utils.isValidId(id)) {
            console.error(this, 'Incorrect Id in Url');
            Admin.getApplication().notFound();
        }
        return id;
    },
    isString: function (str) {
        if (typeof str == 'string') return true;
        if (str instanceof String) return true;
        return false;
    },
    isNotEmptyString: function (str) {
        if (!this.isString(str)) return false;
        return (str.length > 0);
    },
    isNumeric: function (value) {
        return !isNaN(parseFloat(value)) && isFinite(value);
    },
    isInt: function (value) {
        if (!this.isNumeric(value)) return false;
        if (parseFloat(value) != parseInt(value, 10)) return false;
        return true;
    },
    isValidId: function (id) {
        if (!id) return false;
        if (!this.isNumeric(id)) return false;
        if (!this.isInt(id)) return false;
        if (id < 1) return false;
        return true;
    },
    encode: function (ob) {
        var enc = function (obj, prefix) {
            var str = [];

            for (var p in obj) {
                var k = prefix ? prefix + "[" + p + "]" : p, v = obj[p];
                str.push(typeof v == "object" ? arguments.callee(v, k) : k + "=" + v);
            }

            return str.join("&");
        };

        var ar = enc(ob);
        var res = {};
        ar = ar.split('&');
        ar.forEach(function (item) {
            var a = item.split('=');
            if (a[0])res[a[0]] = a[1]
        });
        return res;
    },
    request: function (conf) {
        Ext.Ajax.request(conf)
    },
    fromUTC: function (v, withOffset) {
        var offset = withOffset ? new Date().getTimezoneOffset() * 60 : 0;
        if (Ext.isEmpty(v)) return null;
        v = v.toString().length > 10 ? v : v * 1000;
        var _date = new Date(v);

        var _userOffset = _date.getTimezoneOffset() * 60;

        var _getTime = _date.getTime();

        return new Date(parseInt(_getTime) + parseInt(_userOffset) * 1000 + parseInt(offset));
    }

});
