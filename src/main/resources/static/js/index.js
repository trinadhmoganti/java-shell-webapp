$(document).ready(function() {
	// $("#loading").show();

	$("#submit").click(function() {
		$("#loading").show();
		$("#result").empty();
		var $this = $(this);
		$this.prop('disabled', true);
		var selectScript = $("#selectScript").val();
		$.post("/executeScript", {
			selectScript : selectScript
		}, function(data, status) {
			var resultArray = JSON.parse(data)
			for (var i = 0; i < resultArray.length; i++) {
				$("#result").append("<code>" + resultArray[i] + "</code><br>");
			}
			$("#loading").hide();
			$("#result").show();
			setTimeout(function() {
				$("#result").empty();
				$("#result").hide();
			}, 10000);
			$this.prop('disabled', false);
		});
	});
});