Ext.define("Pms.modules.restriction.model.Restriction", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true, persist: false},
        {name: 'name', type: 'auto'},
        {name: 'minStay', type: 'int'},
        {name: 'minStayArrival', type: 'int'},
        {name: 'maxStay', type: 'int'},
        {name: 'closed', type: 'auto'},
        {name: 'closedToDeparture', type: 'boolean'},
    ]
});