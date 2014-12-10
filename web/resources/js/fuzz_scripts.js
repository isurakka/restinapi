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
                    
                    for(var i = 0; i < requestFieldsJson.fields.length; i++)
                    {
                        if(!requestFieldsJson.fields[i].islocked)
                        {
                            requestFieldsJson.fields[i].value = chance.word();
                        }
                    }
                    
                    $.ajax(requestUrl,
                    {
                        type: requestMethod,
                        data: requestFieldsJson,
                        success: function(data, status, xhr)
                        {
                            createResult(self.requestNumber, xhr.status, JSON.stringify(this.data), JSON.stringify(data), 0);
                        },
                        fail: function(xhr, status)
                        {
                            createResult(self.requestNumber, status, JSON.stringify(this.data), JSON.stringify(xhr), 1);
                        }
                    });              
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

function pathFuzz()
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
    
    var r = "<tr class ='" + w + "'><td><div class = 'result_inner'><p>" + number + "</p></div></td>";
    r += "<td><div class = 'result_inner'><p>" + status + "</p></div></td>";
    r += "<td><div class = 'result_inner'><p>" + data + "</p></div></td></tr>";
    
    $('#result_table').append(r);
}

function request(url, method, params)
{
    
}