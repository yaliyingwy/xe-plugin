var exec = require('cordova/exec');

//nav

var pop = function() {
	exec(null, null, 'XEPlugin', 'normalCommand', ['2']);
}

var popTo = function(index) {
	exec(null, null, 'XEPlugin', 'normalCommand', ['2', index])
}

var push = function(args) {
	args.splice(0, 0, '1');
	exec(null, null, 'XEPlugin', 'normalCommand', args);
}

//command
var run = function(args, success, err) {
	exec(success, err, 'XEPlugin', 'normalCommand', args);
}

//db

var put = function(key, data) {
	try {
		localStorage.setItem(key, data);
	}
	catch(e) {
		console.error('no space for localStorage!!');
	}
}

var get = function(key) {
	return localStorage.getItem(key);
}

var del = function(key) {
	localStorage.removeItem(key);
}

var clear = function() {
	localStorage.clear();
}

//http
var  httpPost = function(url, params, encrypt, key, iv, cb) {
		var data;
		if (encrypt) {
			var plainText = JSON.stringify(params);
			data = CryptoJS.AES.encrypt(plainText, key, { iv: iv });
		}
		else
		{
			data = params;
		}

		if (uuid === undefined || version === undefined || client_type === undefined) {
			console.error('uuid: ' + uuid + ', version: ' + version + ', client_type: ' + client_type);
			return;
		}

		console.group();
		console.log('data: %s', JSON.stringify(data));

		var paramDic = {
			uuid: uuid,     //后台根据这个参数存储每个手机的des密钥
            version: version,   //后台根据这个参数做升级时的老版本兼容
            client_type: client_type, //2 android， 3 ios
            data: data  //明文为json格式
		}

		
		console.log('请求 url: %s', url);
		console.log('发送参数: %s', JSON.stringify(paramDic));
		

		$.post(url, paramDic, function(data, status, xhr){
			console.log('返回 data: %s \n status: %o \n xhr: %o \n', JSON.stringify(data), status, xhr);
			console.groupEnd();
			cb(data, status, xhr);

		}, 'json');
	}

var post = function(url, params, cb) {
		xe.http.httpPost(url, params, false, '12345678', new Array(1,2,3,4,5,6,7,8), cb);
	}

//消息，显示时间，显示位置top,center,bottom
var toast = function(message, time, postion) {
	var args = ['show', message];
	if (time !== undefined) {
		args.push(time);
	}
	if (postion !== undefined) {
		args.push(postion);
	}
	exec(null, null, 'XEPlugin', 'toast', args);
}

var showSuccess = function(message) {
	exec(null, null, 'XEPlugin', 'toast', ['success', message]);
}

var showErr = function(message) {
	exec(null, null, 'XEPlugin', 'toast', ['error', message]);
}

//消息，是否禁用点击
var show = function(message, force) {
	var args = ['show', message];
	if (force !== undefined) {
		args.push(force);
	}
	else
	{
		args.push(true);
	}
	exec(null, null, 'XEPlugin', 'loading', args);
}


var hide = function() {
	exec(null, null, 'XEPlugin', 'loading', ['hide']);
}


module.exports = {
	nav: {
		pop: pop,
		popTo: popTo,
		push: push
	},
	run: run,
	db: {
		put: put,
		get: get,
		del: del,
		clear: clear
	},

	http: {
		post: post,
		httpPost: httpPost
	},

	toast: {
		show: toast,
		success: showSuccess,
		err: showErr
	},
	
	loading: {
		show: show,
		hide: hide
	}
}
