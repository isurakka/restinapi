var requestUrl;
var requestMethod;
var requestType;
var requestFields;
var requestFieldsJson;
var ongoingFuzzes = [];

$(document).ready(function() 
{
    requestUrl = $('#requestUrl').val();
    requestMethod = $('#requestMethod').val();
    requestType = $('#requestType').val();
    requestFields = $('#fields').val();
    requestFieldsJson = $.parseJSON(requestFields);
    
    console.log("Url: " + requestUrl);
    console.log("Method: " + requestMethod);
    console.log("Type: " + requestType);
    
    console.log("Fields:");
    console.log(requestFieldsJson);

    
    
    switch(requestType)
    {
        case "random_fuzz":
            console.log(324234234);
            randomFuzz();
            break;
        case "path_fuzz":
            pathFuzz();
            break;
        default:
            $('body').html("<h1>Fuzz type not supported</h1>");
            break;          
    }
    
});

var fuzz =
{
    requestNumber: i,
    requestData: sendThisData,
    request: function()
    {
        console.log(JSON.stringify(sendThisData));
        /*   */           
    }
};

function randomFuzz()
{
    for(var i = 0; i < 50; i++)
    {
        console.log(i);
        
        var sendThisData = requestFieldsJson;
        
        for(var j = 0; j < sendThisData.fields.length; j++)
        {
            if(!sendThisData.fields[j].islocked)
            {
                console.log("adadas " + i);
                sendThisData.fields[j].value = i;
            }
        }
        
        fuzz.requestNumber = i;
        fuzz.requestData = sendThisData;
        fuzz.request = function()
        {
            $.ajax(
            {
                url: requestUrl,
                type: requestMethod,
                data: sendThisData
            }).done(function(data, status, xhr)
            {
                createResult(self.requestNumber, xhr.status, JSON.stringify(sendThisData), JSON.stringify(data), 0);

            }).fail(function(xhr, status)
            {
                createResult(self.requestNumber, status, JSON.stringify(sendThisData), JSON.stringify(xhr), 1);

            });
        }

        ongoingFuzzes.push(fuzz);         
    }
    
    for(var i = 0; i < ongoingFuzzes.length; i++)
    {
        ongoingFuzzes[i].request();
    }
}

/*function pathFuzz()
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
}*/

function createResult(number, status, request, data, warningLevel)
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
    
    var r = "<tr class ='" + w + "'><td><div class = 'result_inner'><p>" + number + "</p></div></td>";
    r += "<td><div class = 'result_inner'><p>" + status + "</p></div></td>";
    r += "<td><div class = 'result_inner'><p>" + request + "</p></div></td>";
    r += "<td><div class = 'result_inner'><p>" + data + "</p></div></td></tr>";
    
    $('#result_table').append(r);
}