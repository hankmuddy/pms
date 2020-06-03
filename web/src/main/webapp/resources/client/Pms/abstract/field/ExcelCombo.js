Ext.define('Pms.abstract.field.ExcelCombo', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.excelcombo',
    store: Ext.create('Ext.data.Store',{
        fields: ['id', 'abbr'],
        data: [
            {id: 1, abbr: 'A'},
            {id: 2, abbr: 'B'},
            {id: 3, abbr: 'C'},
            {id: 4, abbr: 'D'},
            {id: 5, abbr: 'E'},
            {id: 6, abbr: 'F'},
            {id: 7, abbr: 'G'},
            {id: 8, abbr: 'H'},
            {id: 9, abbr: 'I'},
            {id: 10, abbr: 'J'},
            {id: 11, abbr: 'K'},
            {id: 12, abbr: 'L'},
            {id: 13, abbr: 'M'},
            {id: 14, abbr: 'N'},
            {id: 15, abbr: 'O'},
            {id: 16, abbr: 'P'},
            {id: 17, abbr: 'Q'},
            {id: 18, abbr: 'R'},
            {id: 19, abbr: 'S'},
            {id: 20, abbr: 'T'},
            {id: 21, abbr: 'U'},
            {id: 22, abbr: 'V'},
            {id: 23, abbr: 'W'},
            {id: 24, abbr: 'X'},
            {id: 25, abbr: 'Y'},
            {id: 26, abbr: 'Z'}
        ]
    }),
    displayField: 'abbr',
    valueField: 'id',
    queryMode: "local",
    editable: false
});