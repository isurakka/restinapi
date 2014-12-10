$(document).ready(function() 
{
    var jBubby;
    try
    {
    jBubby = $.parseJSON($('#jsonned').val().toString());
    }
    catch(eeee)
    {
        console.log(eeee);
    }

    for(var i = 0; i < jBubby.fields.length; i++)
    {
        console.log(jBubby.fields[i].name + ", " + jBubby.fields[i].value + ", " + jBubby.fields[i].islocked);
    }
});