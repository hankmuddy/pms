Ext.define("Pms.modules.catalog.model.Catalog", {
    extend: "Ext.data.Model",
    alias: 'widget.roomModel',
    fields: [
        'id',
        'title',
        {
            name: 'price',
            serialize: function (value) {
                return parseInt(value * 100)
            }
        },
        'deprecated',
        'type',
        'measure'
    ]
});