/*$(document).ready(function()
{
	fuzzer.selectedFuzz = "random_fuzz";
			
	var fuzz = fuzzer.getFuzz("random_fuzz");
	
	$.ajax(
	{
		type: "POST",
		url: "fuzz_layouts/" + fuzz.html_layout_file
	}).done(function(html) 
	{
		$('#controls_right').hide().html(html).fadeIn('fast');
		fuzzer.buildFieldList(fuzz.id);
		$('#fuzz_name').text(fuzz.name + " - " + fuzz.description);
		fuzz = null;
	});


	fuzzer.listFuzzTypes();
	
	$('#fuzz_type_list').on('click', 'td', function()
	{
		if(fuzzer.selectedFuzz != $(this).data('fuzzid'))
		{
			fuzzer.selectedFuzz = $(this).data('fuzzid');
			var fuzz = fuzzer.getFuzz($(this).data('fuzzid'));
			var last_url = null;
			
			if($('#fuzz_base_url').val().length > 0)
			{
				var last_url = $('#fuzz_base_url').val();
			}
			
			$.ajax(
			{
				type: "POST",
				url: "fuzz_layouts/" + fuzz.html_layout_file
			}).done(function(html) 
			{
				$('#controls_right').hide().html(html).fadeIn('fast');
				fuzzer.buildFieldList(fuzz.id);
				$('#fuzz_name').text(fuzz.name + " - " + fuzz.description);
				
				if(last_url !== null)
				{
					$('#fuzz_base_url').val(last_url);
				}
			});
		}
	});
	
	/*$('#fuzz_type_list').on('mouseover', 'td', function()
	{
		$(this).transition(
		{
			x: 15 + 'px'
		});
	});
	
	$('#fuzz_type_list').on('mouseout', 'td', function()
	{
		$(this).transition(
		{
			x: 0 + 'px'
		});
	});*/
	
	/*$('body').on('click', '#add_fields_button', function()
	{
		var table = "<tr><td class = 'field_cell'><input type = 'text' class = 'bigger_textbox' placeholder = 'Field' /></td>";
		
		if($(this).data('addvalue'))
		{
			table += "<td class = 'value_cell'><input type = 'text' class = 'bigger_textbox' placeholder = 'Value' /></td>";
		}
		
		if($(this).data('addlock'))
		{
			table += "<td class = 'lock_cell'><label><input type = 'checkbox' class = 'bigger_checkbox' />Lock</label></td>";
		}
		
		table += "<td class = 'close_cell'></td>";
		table += "<td class = 'auto_cell'></td>";
		table += "</tr>";
				
		$(this).parent('td').parent('tr').before(table);
	});
	
	$('body').on('click', '.close_cell', function()
	{
		$(this).parent('tr').remove();
	});
});*/