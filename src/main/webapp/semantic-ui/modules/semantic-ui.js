appDirectives.directive('suiSelect', function() {
	return {
		restrict : "A",
		link : function(scope, element, attrs) {
			$(element).dropdown();
		}
	};
});

appDirectives.directive('suiDropdown', function() {
	return {
		restrict : "A",
		link : function(scope, element, attrs) {
			$(element).dropdown({
				on : 'hover'
			});
		}
	};
});

appDirectives.directive('suiTab', function() {
	return {
		restrict : "A",
		link : function(scope, element, attrs) {
			// $(element).tab();
			$('.menu .item').tab();
		}
	};
});

appDirectives.directive('suiSidebar', function() {
	return {
		restrict : "A",
		link : function(scope, element, attrs) {
			$(element).sidebar('setting', 'transition', 'overlay');
		}
	};
});
