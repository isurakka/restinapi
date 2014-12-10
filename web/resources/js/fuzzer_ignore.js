/*var fuzzer = 
{
	// PROPERTIES
	
	selectedFuzz: null,
	
	fuzzTypes:
	{
		random_fuzz:
		{
			id: "random_fuzz",
			name: "Random",
			description: "Sends randomized data in given fields that are not locked",
			html_layout_file: "random_fuzz.htm",
			options:
			{
				lockable_fields: true,
				value_field: false
			}
		},
		sql_fuzz:
		{
			id: "sql_fuzz",
			name: "SQL",
			description: "Sends various known SQL-injection strings in given fields that are not locked",
			html_layout_file: "sql_fuzz.htm",
			options:
			{
				lockable_fields: true,
				value_field: false
			}
		},
		file_fuzz:
		{
			id: "file_fuzz",
			name: "File",
			description: "Sends malformed files",
			html_layout_file: "file_fuzz.htm",
			options:
			{
				lockable_fields: true,
				value_field: false
			}
		},
		mutation_fuzz:
		{
			id: "mutation_fuzz",
			name: "Mutation",
			description: "Sends accepted data, but with small mutations",
			html_layout_file: "mutation_fuzz.htm",
			options:
			{
				lockable_fields: true,
				value_field: true
			}
		},
		dictionary_fuzz:
		{
			id: "dictionary_fuzz",
			name: "Dictionary",
			description: "Sends random, well-formed data in given fields that are not locked",
			html_layout_file: "dictionary_fuzz.htm",
			options:
			{
				lockable_fields: true,
				value_field: false
			}
		},
		method_fuzz:
		{
			id: "method_fuzz",
			name: "Method",
			description: "Performs same requests, but with different request-methods",
			html_layout_file: "method_fuzz.htm",
			options:
			{
				lockable_fields: false,
				value_field: true
			}
		},
		header_fuzz:
		{
			id: "header_fuzz",
			name: "Header",
			description: "Sends random and unnecessary headers",
			html_layout_file: "header_fuzz.htm",
			options:
			{
				lockable_fields: false,
				value_field: true
			}
		},
		path_fuzz:
		{
			id: "path_fuzz",
			name: "Path",
			description: "Appends base path with random extensions and peforms requests on those paths",
			html_layout_file: "path_fuzz.htm",
			options:
			{
				lockable_fields: true,
				value_field: false
			}
		},
		parameter_fuzz:
		{
			id: "parameter_fuzz",
			name: "Parameter",
			description: "Leaves out some given fields and / or adds extra fields to requests",
			html_layout_file: "parameter_fuzz.htm",
			options:
			{
				lockable_fields: false,
				value_field: false
			}
		}
	},
	
	// METHODS
	
	listFuzzTypes: function()
	{
		for(var key in this.fuzzTypes)
		{
			$('#fuzz_type_list').append("<tr><td data-fuzzid = '" + this.fuzzTypes[key].id + "'>" + this.fuzzTypes[key].name + "</td></tr>");
		}
		$('#fuzz_type_list').append("<tr><td></td></tr>");
	},
	
	getFuzz: function(id)
	{
		for(var key in this.fuzzTypes)
		{
			if(id == this.fuzzTypes[key].id)
			{
				return this.fuzzTypes[key];
			}
		}
		
		return null;
	},
	
	buildFieldList: function(id)
	{
		var options = this.getFuzz(id).options;
		var table = "<tr><td class = 'field_cell'><input type = 'text' class = 'bigger_textbox' placeholder = 'Field' /></td>";
		
		if(options.value_field)
		{
			table += "<td class = 'value_cell'><input type = 'text' class = 'bigger_textbox' placeholder = 'Value' /></td>";
		}
		
		if(options.lockable_fields)
		{
			table += "<td class = 'lock_cell'><label><input type = 'checkbox' class = 'bigger_checkbox' />Lock</label></td>";
		}
		
		table += "<td class = 'auto_cell'></td>";
		
		table += "</tr><tr><td><input type = 'button' id = 'add_fields_button' class = 'bigger_button'" + 
				"data-addlock = '" + options.lockable_fields + "'"+
				"data-addvalue = '" + options.value_field + "' value = 'Add field'/></td></tr>";
		
		$('#fuzz_field_table').html(table);
	}
};*/