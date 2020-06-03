Ext.define('Admin.generic.data.Model',{
    extend: 'Ext.data.Model',
    fields:[
    {
        name:'_title',
        type:'string',
        persist:false,
        convert: function(v,record){
            // return record.data.id;
            return record.setTitle()
        }
    }   
    ],
    setTitle: function(){
        // retutn this.data.id
        return this.getId();
    }
})
