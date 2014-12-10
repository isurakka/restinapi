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



function randomFuzz()
{
    for(var i = 0; i < 50; i++)
    {
        var sendThisData = $.extend(true, {}, requestFieldsJson);
        
        for(var j = 0; j < sendThisData.fields.length; j++)
        {
            if(!sendThisData.fields[j].islocked)
            {
                sendThisData.fields[j].value = chance.word();
            }
        }
        
        var fuzz = 
        {
            requestNumber: i,
            requestData: $.extend(true, {}, sendThisData),
            request: function()
            {
                var self = this;
                
                $.ajax(
                {
                    url: requestUrl,
                    type: requestMethod,
                    data: self.requestData,
                }).done(function(data, status, xhr)
                {
                    createResult(self.requestNumber, xhr.status, JSON.stringify(self.requestData), this.url, JSON.stringify(data), 0);

                }).fail(function(xhr, status)
                {
                    createResult(self.requestNumber, status, JSON.stringify(self.requestData), this.url, JSON.stringify(xhr), 2);

                });
            }
        }
        
        ongoingFuzzes.push(fuzz);         
    }
    
    for(var i = 0; i < ongoingFuzzes.length; i++)
    {
        ongoingFuzzes[i].request();
    }
}

function pathFuzz()
{
    for(var i = 0; i < 50; i++)
    {
        var newUrl = requestUrl.toString();
        newUrl = newUrl.replace("(+)", chance.word());
        console.log(newUrl);
        
        var fuzz = 
        {
            requestNumber: i,
            requestUrl: newUrl.toString(),
            request: function()
            {
                var self = this;
                
                $.ajax(
                {
                    url: self.requestUrl,
                    type: requestMethod,
                    data: self.requestData,
                }).done(function(data, status, xhr)
                {
                    createResult(self.requestNumber, xhr.status, self.requestUrl, this.url.toString(), JSON.stringify(data), 0);
                    console.log(self.requestUrl);

                }).fail(function(xhr, status)
                {
                    createResult(self.requestNumber, status, self.requestUrl, this.url.toString(), JSON.stringify(xhr), 2);
                    console.log(self.requestUrl);
                });
            }
        }
        
        ongoingFuzzes.push(fuzz);         
    }
    
    for(var i = 0; i < ongoingFuzzes.length; i++)
    {
        ongoingFuzzes[i].request();
    }
}

function createResult(number, status, request, url, data, warningLevel)
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
    
    var r = "<tr class ='" + w + "'><td class = 'result_number'><div class = 'result_inner'><p>" + number + "</p></div></td>";
    r += "<td class = 'result_status'><div class = 'result_inner'><p>" + status + "</p></div></td>";
    r += "<td class = 'result_url'><div class = 'result_inner'><p>" + unescape(url) + "</p></div></td>";
    r += "<td class = 'result_request'><div class = 'result_inner'><p>" + request + "</p></div></td>";
    
    r += "<td class = 'result_data'><div class = 'result_inner'><p>" + data + "</p></div></td><td class = 'auto'></td></tr>";

    $('#result_table').append(r);
}