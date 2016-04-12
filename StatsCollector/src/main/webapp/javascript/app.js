var app = angular.module('app', ['appGerritStats', 'appSonarStats', 'formly', 'formlyBootstrap'], function config(formlyConfigProvider) {
	formlyConfigProvider.setWrapper({
	      name: 'horizontalBootstrapLabel',
	      template: [
	        '<label for="{{::id}}" class="col-sm-4 control-label">',
	          '{{to.label}}: {{to.required ? "*" : ""}}',
	        '</label>',
	        '<div class="col-sm-8">',
	          '<formly-transclude></formly-transclude>',
	        '</div>',
	        '<br/>',
	        '<br/>'
	      ].join(' ')
	    });
	    
	    formlyConfigProvider.setWrapper({
	      name: 'horizontalBootstrapCheckbox',
	      template: [
	        '<div class="col-sm-offset-4 col-sm-8">',
	          '<formly-transclude></formly-transclude>',
	        '</div>',
	        '<br/>',
	        '<br/>'
	      ].join(' ')
	    });
	    
	    formlyConfigProvider.setType({
	      name: 'horizontalInput',
	      extends: 'input',
	      wrapper: ['horizontalBootstrapLabel', 'bootstrapHasError']
	    });
	    
	    formlyConfigProvider.setType({
	      name: 'horizontalCheckbox',
	      extends: 'checkbox',
	      wrapper: ['horizontalBootstrapCheckbox', 'bootstrapHasError']
	    });
	  });

/*
 * This is where the magic of OIM comes into play, we generate the field
 * config based on the values in the model. You would write this function
 * to generate the config based on the config format of your server's model
 * meta data.
 */
app.factory('getOIMConfig', function getOIMConfigDefinition(deepMerge) {    
  return function getOIMConfig(model, options) {
    options = options || {};
    var fields = [];
    angular.forEach(model, function(value, key) {
      var opts = options[key] || {};
      if (opts.NO_FIELD) {
        return;
      }
      fields.push(getOptionsFromValue(value, key, opts));
    });
    return fields;
  };
  
  function getOptionsFromValue(value, key, propMetaData) {
    var commonOptions = {
      key: key,
      templateOptions: {
        label: makeHumanReadable(key)
      }
    };
    
    if (propMetaData.hasOwnProperty('required')) {
      commonOptions.templateOptions.required = propMetaData.required;
    }
    
    var typeOf = propMetaData.type || typeof value;
    var typeOptions = {};
    switch (typeOf) {
      case 'enum': {
        var totalOptions = propMetaData.options.length;
        var type = 'radio';
        if (totalOptions > 5) {
          type = 'select';
        }
        typeOptions = {
          type: type,
          templateOptions: {
            options: propMetaData.options.map(function(option) {
              return {
                name: makeHumanReadable(option),
                value: option
              };
            })
          }
        };
        break;
      }
      case 'boolean': {
        typeOptions = {
          type: 'horizontalCheckbox',
      	  defaultValue: false
        };
        break;
      }
      case 'number': {
        typeOptions = {
          type: 'horizontalInput',
          templateOptions: {type: 'number'}
        };
        break;
      }
      case 'string':
      default:
    	  if(key.match(/password/i)){
  	        typeOptions = {
  	        		type: 'horizontalInput',
  	        		templateOptions: {type: 'password'}
  	        };  
    	  }else{
	        var type = (value && value.length) > 80 ? 'textarea' : 'horizontalInput';
	        typeOptions = {type: type};
    	  }
    }
    return deepMerge(commonOptions, typeOptions, propMetaData.formlyOptions);
  }
  
  function makeHumanReadable(key) {
    var words = key.match(/[A-Za-z][a-z]*/g);
    return words.map(capitalize).join(" ");
  }

  function capitalize(word) {
    return word.charAt(0).toUpperCase() + word.substring(1);
  }
});

app.constant('deepMerge', (function() {
    var objectPrototype = Object.getPrototypeOf({});
    var arrayPrototype = Object.getPrototypeOf([]);

    return deepMerge;

    function deepMerge() {
      var res = arguments[0];
      angular.forEach(arguments, function (src, index) {
        if (src && (index > 0 || false)) {
          angular.forEach(src, function (val, prop) {
            if (typeof val === "object" && val !== null && isObjectOrArrayLike(val)) {
              var deepRes = res[prop];
              if (!deepRes && Array.isArray(val)) {
                deepRes = [];
              } else if (!deepRes) {
                deepRes = {};
              }
              res[prop] = deepMerge(deepRes, val);
            } else {
              res[prop] = val;
            }
          });
        }
      });
      return res;
    }
    
    function isObjectOrArrayLike(val) {
      var proto = Object.getPrototypeOf(val);
      return proto === objectPrototype || proto === arrayPrototype;
    }
  })());