Ext.define('Admin.Lookup', {
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
            userType: ['admin', 'managerSupervisor', 'manager', 'user'],
            language: ['ru', 'en', 'uk']
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
