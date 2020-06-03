Ext.define('Admin.generic.form.FieldConfig', {
    items: {},
    constructor: function (items) {
        items = items || {};
        Ext.apply(this.items, items)
    },
    toArray: function (keyfield) {
        keyfield = keyfield || 'name';
        var item, obj, items = [];
        for (item in this.items) {
            obj = {};
            obj[keyfield] = item;
            items.push(Ext.apply(obj, this.items[item]))
        }
        return items;
    },
    getConfigItems: function () {
        return this.toArray();
    },
    getRestrictedConfigItems: function (restrictAttributes) {
        var item, oldItem, newItem, items = [];
        for (item in this.items) {
            oldItem = Ext.apply({name: item}, this.items[item]);
            newItem = {};
            restrictAttributes.forEach(function (attr) {
                if (attr in oldItem) {
                    newItem[attr] = oldItem[attr];
                }
            });
            items.push(newItem)
        }
        return items;
    },
    restrict: function (attrs) {
        var items = {};
        attrs.forEach(function (attr) {
            if (attr in this.items) {
                items[attr] = this.items[attr];
            }
        }, this)
        this.items = items;
        return this;
    },
    reject: function (attrs) {
        var item, items = {};
        for (item in this.items) {
            if (attrs.indexOf(item) != -1) continue;
            items[item] = this.items[item]
        }
        this.items = items
        return this
    },
    merge: function (items) {
        var item, _items = {};
        for (item in this.items) {
            if (item in items) {
                _items[item] = items[item];
            } else {
                _items[item] = this.items[item]
            }
        }
        this.items = _items;
        return this
        // var _items = Ext.clone(this.items)
        // Ext.apply(_items, items)
        // return _items
    },
    replaceItemKeys: function (keyObj) {
        var key, field, item, items = {};
        for (item in this.items) {
            field = this.items[item];
            for (key in keyObj) {
                field[key] = keyObj[key];
            }
            items[item] = field;
        }
        this.items = items
        return this
    },
    restrictItemKeys: function (attrs) {
        var key, field, item, items = {};
        for (item in this.items) {
            items[item] = {};
            field = this.items[item];
            for (key in field) {
                if (attrs.indexOf(key) != -1) {
                    items[item][key] = field[key];
                }
            }
        }
        this.items = items
        return this
    },
    getViewConfigItems: function () {
        return this.getRestrictedConfigItems(['name', 'fieldLabel'])
    }
})

