Ext.define("Pms.modules.virtualRoom.model.VirtualRoom", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true, persist: false},
        {name: 'adults', type: 'integer'},
        {
            name: 'defaultPrice',
            type: 'integer',
            serialize: function (value) {
                return parseInt(value * 100)
            }
        },
        {name: 'children', type: 'integer'},
        {name: 'type',serialize: function (val) {
            return 'virtualRoom'
        }},
        {name: 'shortname', type: 'auto'},
        {name: 'name', type: 'auto'},
        {name: 'additional', type: 'integer'},
        {name: 'approved', persist: false},
        {
            name: 'roomType',
            type: 'auto',
            serialize: function (val) {
                return {id: val, type: 'roomType'}
            }
        },
        {name: 'roomType.id', type: 'int', persist: false},
        {name: 'defaultBaseRoom', type: 'boolean', persist: false}
    ]
});