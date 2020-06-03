Ext.define('Pms.abstract.Reader', {
    extend: 'Ext.data.reader.Json',
    alias: 'widget.abstractReader',
    root: 'content',
    totalProperty: 'page.totalCount',
    getResponseData: function (response) {
        var data, error, me = this;
        try {
            data = Ext.decode(response.responseText);
            if(me.parseResponseData){
                data = me.parseResponseData(data);
            }
            return this.readRecords(data);
        } catch (ex) {
            error = new Ext.data.ResultSet({
                total: 0,
                count: 0,
                records: [],
                success: false,
                message: ex.message
            });

            this.fireEvent('exception', this, response, error);

            Ext.Logger.warn('Unable to parse the JSON returned by the server');

            return error;
        }
    },
});