Ext.define("Pms.core.Ajax", {
    extend: "Ext.data.Connection",
    mixins: {
        observable: "Ext.util.Observable"
    },
    singleton: true,

    constructor: function () {

        this.addEvents({
            "sessionExpired": true,
            "showerror": true
        });

        this.callParent.apply(this, arguments);

        this.on("beforerequest", this.onBeforeRequest, this);
        this.on("requestcomplete", this.onRequestComplete, this);
        this.on("requestexception", this.onRequestError, this);
    },

    request: function (options) {
        options = options || {};
        var me = this,
            scope = options.scope || window,
            username = options.username || me.username,
            password = options.password || me.password || '',
            async,
            requestOptions,
            request,
            headers,
            xhr;

        if (options.success) {
            options.onSuccessCallback = options.success;
            delete options.success;
        }

        if (options.failure) {
            options.onFailureCallback = options.failure;
            delete options.failure;
        }
        if (me.fireEvent('beforerequest', me, options) !== false) {

            requestOptions = me.setOptions(options, scope);

            if (this.isFormUpload(options) === true) {
                this.upload(options.form, requestOptions.url, requestOptions.data, options);
                return null;
            }

            // if autoAbort is set, cancel the current transactions
            if (options.autoAbort === true || me.autoAbort) {
                me.abort();
            }

            // create a connection object
            xhr = this.getXhrInstance();

            async = options.async !== false ? (options.async || me.async) : false;
            // open the request
            if (username) {
                xhr.open(requestOptions.method, requestOptions.url, async, username, password);
            } else {
                xhr.open(requestOptions.method, requestOptions.url, async);
            }

            headers = me.setupHeaders(xhr, options, requestOptions.data, requestOptions.params);
            // create the transaction object
            request = {
                id: ++Ext.data.Connection.requestId,
                xhr: xhr,
                headers: headers,
                options: options,
                async: async,
                timeout: setTimeout(function () {
                    request.timedout = true;
                    me.abort(request);
                }, options.timeout || me.timeout)
            };
            me.requests[request.id] = request;
            // bind our statechange listener
            if (async) {
                xhr.onreadystatechange = Ext.Function.bind(me.onStateChange, me, [request]);
            }
            // start the request!
            xhr.send(requestOptions.data);

            if (!async) {
                return this.onComplete(request);
            }
            return request;
        } else {
            Ext.callback(options.callback, options.scope, [options, undefined, undefined]);
            return null;
        }
    },

    onBeforeRequest: function (conn, options) {
        //disable request button
        if (Ext.ComponentQuery.query('button[requestDisable=true]').length) {
            Ext.each(Ext.ComponentQuery.query('button[requestDisable=true]'), function (item) {
                item.disable();
            })
        }
        if (options.el) {
            options.el.mask(options.msg || "Loading...", "x-mask-loading");
        }
        else if (options.statusBar) {
            options.statusBar.showBusy(options.msg || "Loading...");
        }
        else if (options.method != 'GET') Ext.getBody().mask(options.msg || "Loading...", "x-mask-loading");
        options.submitEmptyText = false;
        options.scope = options.scope || this;
        options.params = options.params || {};
        options.params.ajax_request = true;
    },

    onRequestComplete: function (conn, response, options) {

        if (options.el) {
            options.el.unmask();
        }
        else if (options.method != 'GET') Ext.getBody().unmask();
        //enable request button
        if (Ext.ComponentQuery.query('button[requestDisable=true]').length) {
            Ext.each(Ext.ComponentQuery.query('button[requestDisable=true]'), function (item) {
                item.enable();
            })
        }

        if (response.status === 200 || response.status === 201) {
            var data = {
                success: true
            };


            try {
                data = Ext.decode(response.responseText);
            } catch (e) {

            }

            if (data.success == true) {
                if (options && options.onSuccessCallback) {
                    options.onSuccessCallback.call(options.scope, data, options);
                }
                if (options.statusBar) {
                    options.statusBar.setStatus({
                        text: data.message || "Action completed",
                        iconCls: 'x-status-valid'
                    });
                }
            } else {
                delete data;
                this.onRequestError(conn, response, options);
            }
        } else {
            this.onRequestError(conn, response, options);
        }

    },

    onRequestError: function (conn, response, options) {
        var me = this;
        //enable request button
        if (Ext.ComponentQuery.query('button[requestDisable=true]').length) {
            Ext.each(Ext.ComponentQuery.query('button[requestDisable=true]'), function (item) {
                item.enable();
            })
        }

        if (options.el) {
            options.el.unmask();
        } else if (options.method != 'GET') Ext.getBody().unmask();

        var data = {};
        try {
            data = Ext.decode(response.responseText)[0];
        } catch (e) {
            response.statusText = "Unknown server response, please try again.";
        }

        if (data && data.status) {
            // @todo catch errors
            // switch(data.error.code){
            // 	case 0	: 	//timeout
            // 		data.message = "There was an error, please try it again later.";
            // 		break;
            // 	case 404: 	//not foud
            // 		data.message = "Resource not found, please resport this issue to the administrator.";
            // 		break;
            // 	case 403: 	//session expired
            // 		data.message = "Session expired, please login again.";
            // 		this.fireEvent("sessionexpired",data); //show login form here!!
            // 		break;
            // 	case 401: 	//access denied
            // 		data.message = "Access denied, you don't have the rights to get this resource.";
            // 		break;
            // 	case 6000: 	//system error
            // 		data.message = "An error ocurred in this request, please try again later";
            // 		break;
            // 	default	:
            // 		data.message = response.statusText;
            // 	break;
            // }
            if (data.code == 401) {
//                this.fireEvent("sessionexpired", data); //show login form here!!
//                document.location.href = "/";
            }
        } else if (!options.prevError) {
            switch (response.status) {
                case 0  : //timeout
                    data.message = "There was an error, please try it again later.";
                    break;
                case 404: //not foud
//                    data.message = "Resource not found, please resport this issue to the administrator.";
                    break;
                case 403: //session expired
//                    data.message = "Session expired, please login again.";
//                    this.fireEvent("sessionexpired", data); //show login form here!!
                    Pms.App.showNotification({
                        message: l('deleteError')
                    });
                    break;
                case 401: //access denied
                    data.code = 401;
                    data.message = "Access denied, you don't have the rights to get this resource.";
                    me.fireEvent("sessionExpired", me, data);
                    break;
                case 400:
                    if (response.responseText != "") {
                        var responseObj = JSON.parse(response.responseText)[0];
                        if (responseObj) {
                            Ext.Msg.alert({title: l('source') + ': ' + l(responseObj.source), msg: l(responseObj.code)});
                        }
                    } else {
                        data.message = 'An error ocurred in this request, please try again later';
                        Ext.Msg.alert({
                            title: l('source'),
                            msg: l(data.message)
                        });
                    }

                    break;
                case 500: //system error
                    data.message = "An error occurred in this request, please try again later";
                    Ext.Msg.alert({
                        title: 'error:',
                        msg: l(data.message)
                    });
                    break;
                default:
//                    data.message = response.statusText;
                    data.message = 'dsdsd';
                    break;
            }
        }


        //show notification!
        this.fireEvent("showerror", data);
        if (options.statusBar) {
            options.statusBar.setStatus({
                text: data.message || "There was an error",
                iconCls: 'x-status-error'
            });
        }

        if (options && options.onFailureCallback) {
            options.onFailureCallback.call(options.scope, data, options);
        }
        else {
            var source = data.source,
                code = data.code;

            if (data.code != 401) {
                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l(code));
            }
        }
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
        var ar = enc(ob),
            res = {};
        ar = ar.split('&');
        ar.forEach(function (item) {
            var a = item.split('=');
            if (a[0])res[a[0]] = a[1]
        });
        return res;
    }
});

Pms.Ajax = Pms.core.Ajax;