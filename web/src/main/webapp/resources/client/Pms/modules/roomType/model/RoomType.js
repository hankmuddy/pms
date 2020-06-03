Ext.define("Pms.modules.roomType.model.RoomType", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true, persist: false},
        'name',
        {name: 'adults', type: 'integer'},
        {name: 'children', type: 'integer'},
        {name: 'additional', type: 'integer'},
        {name: 'defaultPrice', serialize: function (value) { return parseInt(value * 100) } },
        {name: 'type', serialize: function (val) { return 'roomType' } },
        {name: 'area', type: 'integer'},
        {name: 'smoking', serialize: function (val) { if (val == "") return null; else return val; } },
        {name: 'shortname', type: 'auto'},
        {name: 'details', type: 'auto'},
        {name: 'description', type: 'auto'},
        {name: 'approved', persist: false},
        {name: 'defaultBaseRoom', type: 'boolean', persist: false},
        {name: 'virtualRooms', type: 'auto', persist: false},
//        {name: 'virtualRoom', type: 'auto'},
        {name: 'roomType', type: 'auto', persist: false}
    ]
});