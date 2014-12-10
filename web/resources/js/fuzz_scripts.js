$(document).ready(function() 
{
    console.log($('#requestUrl').val());
    console.log($('#requestMethod').val());
    
    var fields = $.parseJSON($('#fields').val().toString());

    for(var i = 0; i < fields.fields.length; i++)
    {
        console.log(fields.fields[i].name + ", " + fields.fields[i].value + ", " + fields.fields[i].islocked);
    }
    
});