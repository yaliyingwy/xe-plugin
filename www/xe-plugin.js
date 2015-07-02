var exec = require('cordova/exec');

var pop = function() {
	exec(null, null, 'XEPlugin', 'normalCommand', ['2']);
}

var push = function(args) {
	exec(null, null, 'XEPlugin', 'normalCommand', args.splice(0, 0, '1'));
}

module.exports = {
	nav: {
		pop: pop,
		push: push
	}
}
