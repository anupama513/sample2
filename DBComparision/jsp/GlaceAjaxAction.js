AjaxConnect={
	browser_type:[
		'MSXML2.XMLHTTP.3.0',
		'MSXML2.XMLHTTP',
		'Microsoft.XMLHTTP'
	],
	
	/**
	* Determines if a default header of
	* Content-Type of 'application/x-www-form-urlencoded'
	* will be added to any client HTTP headers sent for POST
	* transactions.
	* @private
	* @type boolean
	*/
    _use_default_post_header:true,
    
	/**
	* Determines if a default header of
	* Content-Type of 'application/x-www-form-urlencoded'
	* will be added to any client HTTP headers sent for POST
	* transactions.
	* @private
  	* @type boolean
	*/
    _default_post_header:'application/x-www-form-urlencoded',    
    
   /**
   * A transaction counter that increments the transaction id for each transaction.
   * @private
   * @type int
   */
    _transaction_id:0,
    

  /**
   * Object literal of HTTP header(s)
   * @private
   * @type object
   */
	_http_header:{},

  /**
   * Determines if HTTP headers are set.
   * @private
   * @type boolean
   */
	_has_http_headers:false,
 /**
  * Collection of polling references to the polling mechanism in handleReadyState.
  * @private
  * @type object
  */
    _poll:{},	
  /**
   * The polling frequency, in milliseconds, for HandleReadyState.
   * when attempting to determine a transaction's XHR readyState.
   * The default is 50 milliseconds.
   * @private
   * @type int
   */
    _polling_interval:50,
    
 /**
  * Queue of timeout values for each transaction callback with a defined timeout value.
  * @private
  * @type object
  */
    _timeOut:{},    
	
	_currentRequestObjects:{},
    
    
    getRequestObject:function(){
    	var requestObject;
    	var tId = this._transaction_id;
    	
    	try{
    		requestObject = this.createRequestObject(tId);
			if(requestObject){
				this._transaction_id++;
			}
    		
    	}catch(e){
    	
    	}finally{
    		return requestObject;
    	}
    },
    
	setHeader:function(o){
		o.conn.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	},    
    
	createRequestObject:function(transactionId){
		var obj,http;
		try{
			// Instantiates XMLHttpRequest in non-IE browsers and assigns to http.
			http = new XMLHttpRequest();
			//  Object literal with http and tId properties
			obj = { conn:http, tId:transactionId, isTimeOut:false, status:0 };
		}
		catch(e)
		{
			for(var i=0; i<this.browser_type.length; ++i){
				try
				{
					// Instantiates XMLHttpRequest for IE and assign to http.
					http = new ActiveXObject(this.browser_type[i]);
					//  Object literal with http and tId properties
					obj = { conn:http, tId:transactionId, isTimeOut:false, status:0 };
					break;
				}
				catch(e){}
			}
		}
		finally
		{
			return obj;
		}
	},
	
	initHeader:function(label,value){
		if(this._http_header[label] == undefined){
			this._http_header[label] = value;
		}
		else{
			// Concatenate multiple values, comma-delimited,
			// for the same header label,
			this._http_header[label] =  value + "," + this._http_header[label];
		}

		this._has_http_headers = true;
	},
    
    
    sendAsynchRequest:function(method, uri, callback, data){

    	var requestObject = this.getRequestObject();
    	if(!requestObject){
    		return null;
    	}else{
    		data=((data)?data:"")+"&GlaceAjaxRequest="+true;
    		if(method=="GET"){
    				uri+="?"+data;
    		}
    		else if(method=="POST"){
    			//uri+="?"+data
    		}
    		requestObject.method=method;
    		requestObject.uri=uri;
    		requestObject.callback=callback;
    		requestObject.data=data;
    		requestObject.conn.open(method, uri, true);
    		if(data && this._use_default_post_header){
	    		this.initHeader('Content-Type', this._default_post_header);
	    	}
    		if(this._has_http_headers){
				this.setHeader(requestObject);
			}
	    	requestObject.argument = callback.argument;
			var oConn = this;
			if(callback && callback.timeout){
				requestObject.isTimeOut=true;
				this._timeOut[requestObject.tId] = window.setTimeout(function(){ oConn.abortTimeOut(requestObject); }, callback.timeout);
	        }
			requestObject.conn.onreadystatechange = function(){
			if(requestObject.conn && requestObject.conn.readyState == 4){
				if(callback && callback.timeout){
                    window.clearTimeout(oConn._timeOut[requestObject.tId]);
                    delete oConn._timeOut[requestObject.tId];
                }
				delete oConn._currentRequestObjects[requestObject.tId];
				oConn.handleTransactionResponse(requestObject, callback);
			}else{
				oConn.handleTransactionProgress(requestObject, callback);
			}
			}
			requestObject.conn.send(data?data:null);
			this._currentRequestObjects[requestObject.tId] = requestObject;
			return requestObject;
    	}
    },
    
   
  /**
   * This method will do the needed function while getting the server response 
   * @private
   * @param {object} responseObject The connection object
   * @param {object} callback - User-defined callback object
   * @param {boolean} determines if the transaction was aborted.
   * @return void
   */
   
  handleTransactionProgress:function(requestObject, callback){
	if(callback.progress){
		if(!callback.scope){
			callback.progress(requestObject);
		}
		else{
			// If a scope property is defined, the callback will be fired from
			// the context of the object.
			callback.progress.apply(callback.scope, [requestObject]);
		}  	
  	}
  },  
   
    
  /**
   * This method attempts to interpret the server response and
   * determine whether the transaction was successful, or if an error or
   * exception was encountered.
   *
   * @private
   * @param {object} o The connection object
   * @param {object} callback - User-defined callback object
   * @param {boolean} determines if the transaction was aborted.
   * @return void
   */
    handleTransactionResponse:function(requestObject, callback){
		// If no valid callback is provided, then do not process any callback handling.
		if(!callback){
			this.releaseObject(requestObject);
			return;
		}

		var httpStatus, responseObject, sessionCallerAlert;

		try{
			if(requestObject.conn.status !== undefined && requestObject.conn.status != 0){
				httpStatus = requestObject.conn.status;
			}
			else{
				httpStatus = 13030;
			}
		}
		catch(e){
			// 13030 is the custom code to indicate the condition -- in Mozilla/FF --
			// when the o object's status and statusText properties are
			// unavailable, and a query attempt throws an exception.
			httpStatus = 13030;
		}

		if(httpStatus >= 200 && httpStatus < 300){
			try
			{
				responseObject = this.createResponseObject(requestObject, callback.argument);
				sessionCallerAlert = false;
				if("--sessionexpired--"==(""+responseObject.responseText)){//Checks data from jsp AjaxSessionTimeout.jsp
					sessionCallerAlert = this.callSessionAlert(responseObject,callback);
				}
				if(!sessionCallerAlert){
					if(callback.success){
						if(!callback.scope){
							callback.success(responseObject);
						}
						else{
							// If a scope property is defined, the callback will be fired from
							// the context of the object.
							callback.success.apply(callback.scope, [responseObject]);
						}
					}
				}
			}
			catch(e){/*alert('Alert' +  e.message);*/}
		}
		else{
			try
			{
				switch(httpStatus){
					// The following case labels are wininet.dll error codes that may be encountered.
					case 12002: // Server timeout
					case 12029: // 12029 to 12031 correspond to dropped connections.
					case 12030:
					case 12031:
					case 12152: // Connection closed by server.
					case 13030: // See above comments for variable status.
					default:
						responseObject = this.createResponseObject(requestObject, callback.argument);
						if(callback.failure){
							if(!callback.scope){
								callback.failure(responseObject);
							}
							else{
								callback.failure.apply(callback.scope, [responseObject]);
							}
						}
				}
			}
			catch(e){}
		}
		this.releaseObject(requestObject);
		responseObject = null;
    },
    
    callSessionAlert:function(responseObject, callback){
    	if(callback.session){
			if(!callback.scope){
				callback.session(responseObject);
			}
			else{
				callback.session.apply(callback.scope, [responseObject]);
			}
			return true;
		}else{
			return false;
		}
    },
    
    createResponseObject:function(requestObject, callbackArg)
    {
		var obj = {};
		var headerObj = {};

		try
		{
			var headerStr = requestObject.conn.getAllResponseHeaders();
			var header = headerStr.split('\n');
			for(var i=0; i < header.length; i++){
				var delimitPos = header[i].indexOf(':');
				if(delimitPos != -1){
					headerObj[header[i].substring(0,delimitPos)] = header[i].substring(delimitPos + 2);
				}
			}
		}
		catch(e){alert(e)}
		obj.tId = requestObject.tId;
		obj.status  = (requestObject.status==-1) ?  -1:requestObject.conn.status;
		obj.statusText = requestObject.conn.statusText;
		obj.getResponseHeader = headerObj;
		obj.getAllResponseHeaders = headerStr;
		obj.responseText = requestObject.conn.responseText;
		obj.responseXML = requestObject.conn.responseXML;
		obj.method=requestObject.method;
		obj.uri=requestObject.uri;
		obj.callback=requestObject.callback;
		obj.data=requestObject.data;
		obj.isTimeOut=requestObject.isTimeOut
		if(typeof callbackArg != undefined){
			obj.argument = callbackArg;
		}
		return obj;
    },    
    
    
  /**
   * If a transaction cannot be completed due to dropped or closed connections,
   * there may be not be enough information to build a full response object.
   * The failure callback will be fired and this specific condition can be identified
   * by a status property value of 0.
   *
   * If an abort was successful, the status property will report a value of -1.
   *
   * @private
   * @param {int} tId Transaction Id
   * @param callbackArg The user-defined arguments
   * @param isAbort Determines if the exception is an abort.
   * @return object
   */
    createExceptionObject:function(callbackArg)
    {
		var obj = {};

		if(callbackArg){
			obj.argument = callbackArg;
		}

		return obj;
    },    
    
     /**
   * Dereference the XHR instance and the connection object after the transaction is completed.
   * @private
   * @param {object} o The connection object
   * @return void
   */
	releaseObject:function(requestObject)
	{
		//dereference the XHR instance.
    	try{
    		requestObject.conn.onreadystatechange = null;
    	}catch(e){}
		requestObject.conn = null;
		//dereference the connection object.
		requestObject = null;
	},
	
	/**
   * Public method to check if the transaction is still being processed.
   * @public
   * @param {object} o The connection object returned by asyncRequest
   * @return boolean
   */
	isCallInProgress:function(o)
	{
		// if the XHR object assigned to the transaction has not been dereferenced,
		// then check its readyState status.  Otherwise, return false.
		if(o.conn){
			return o.conn.readyState != 4 && o.conn.readyState != 0;
		}
		else{
			//The XHR object has been destroyed.
			return false;
		}
	},
	
	abort:function(requestObject){
		if(this.isCallInProgress(requestObject)){
			if(requestObject.conn)
				requestObject.conn.abort();
		}
	},
	
	abortTimeOut:function(requestObject){
		if(requestObject.isTimeOut){
			requestObject.status=-1;
			window.clearTimeout(this._timeOut[requestObject.tId]);
			delete this._timeOut[requestObject.tId];
			this.abort(requestObject);
		}
	},
	
	abortRequests:function(){
		for(var prop in this._currentRequestObjects){
			if(this._currentRequestObjects.hasOwnProperty(prop)){
				this.abort(this._currentRequestObjects[prop]);
			}
		}
	}	
	
    
};