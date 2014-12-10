var requestUrl;
var requestMethod;
var requestType;

$(document).ready(function() 
{
    requestUrl = $('#requestUrl').val();
    requestMethod = $('#requestMethod').val();
    requestType = $('#requestType').val();
    
    console.log("Url: " + requestUrl);
    console.log("Method: " + requestMethod);
    console.log("Type: " + requestType);
    
    console.log("Fields:");
    
    var fields = $.parseJSON($('#fields').val().toString());

    for(var i = 0; i < fields.fields.length; i++)
    {
        console.log("Name: " + fields.fields[i].name + ", Value: " + fields.fields[i].value + ", Locked: " + fields.fields[i].islocked);
    }
    
    randomFuzz();
});

var ongoingFuzzes = [];

function randomFuzz()
{
    var i = 0;
    
    var fuzzGenerator = setInterval(function()
    {
        if(i < 50)
        {
            var fuzz = 
            {
                requestNumber: i,
                request: function()
                {
                    var self = this;
                    
                    try
                    {
                        $.ajax(requestUrl,
                        {
                            type: requestMethod,
                            success: function(data, status, xhr)
                            {
                                createResult(self.requestNumber, xhr.status, JSON.stringify(data), 0);
                            },
                            fail: function(xhr, status)
                            {
                                createResult(self.requestNumber, status, JSON.stringify(xhr), 1);
                            }
                        });              
                    }
                    catch(requestError)
                    {
                        createResult(self.requestNumber, requestError, "");
                    }
                }
            };
            
            fuzz.request();
            ongoingFuzzes.push(fuzz);
        }
        else
        {
            clearInterval(fuzzGenerator);
        }
        
        i++;
        
    }, 75);
}

function createResult(number, status, data, warningLevel)
{
    if(warningLevel == 0)
    {
        var w = "success"
    }
    else if(warningLevel == 1)
    {
        var w = "warning";
    }
    else if(warningLevel == 2)
    {
        var w = "danger";
    }
    else
    {
        var w = "";
    }
    
    var r = "<tr class ='" + w + "'><div class = 'result_inner'><td>" + number + "</td>";
    r += "<td>" + status + "</td>";
    r += "<td>" + data + "</td></div></tr>";
    
    $('#result_table').append(r);
}

function request(url, method, params)
{
    
}