function run(url, method, params)
{
    var dfd = $.Deferred();

    var jqHXR = $.ajax(
    {
        type: method,
        url: url,
        data: params,
        async: true,
        cache: false
    })
    .always(function(data)
    {
        console.log(data);
        dfd.resolve(data);
    });

    return dfd;
}

function findKey(row)
{
    return $(row).find('.key');
}

function findValue(row)
{
    return $(row).find('.value');
}

function findParams()
{
    var params = {};

    var requestParams = $('.requestParams');
    requestParams.each(function(i, v) {
        var key = findKey(this).val();
        var val = findValue(this).val();

        console.log("i " + i);
        console.log("key " + key);
        console.log("val " + val);

        params[key] = val;
    });

    return params;
}

function tryAddRow(selector)
{
    console.log("tryAdd");

    var last = $(selector).last();
    var key = findKey(last).val();

    if (key.trim()) {
        var clone = last.clone();
        findKey(clone).val("");
        findValue(clone).val("");
        clone.insertAfter(last);

        last.off('change');
        //findKey(clone).change(tryRemoveRow);
        findKey(clone).change(function() { tryAddRow(selector) });
    }
}

function tryRemoveRow(selector)
{
    var count = $(selector).length;
    $(selector).each(function(i, v) {
        if (i === 0 || i === count - 1)
        {
            return true;
        }

        var key = findKey(this).val();
        var val = findValue(this).val();

        if (!key.trim() && !val.trim())
        {
            v.remove();
            tryRemoveRow(selector);
            return false;
        }
    });
}

function tryEvalBefore(settings, code, where)
{
    try {
        eval(code);
    } catch (e) {
        alert(where + ": " + e.message);
    }
}

function tryEvalAfter(response, code, where)
{
    try {
        eval(code);
    } catch (e) {
        alert(where + ": " + e.message);
    }
}

$(document).ready(function() {
    var requestSelector = '.requestParams';
    var requestParams = $(requestSelector);
    var projectSelector = '.projectParams';
    var projectParams = $(projectSelector);
    /*
    window.setInterval(function() { 
        tryRemoveRow(requestSelector);
        tryRemoveRow(projectSelector);
    }, 500);
    findKey(requestParams.first()).change(function() { tryAddRow(requestSelector); });
    findKey(projectParams.first()).change(function() { tryAddRow(projectSelector); });
    */

    $('#run').click(function() 
    {
        var params = findParams();
        var method = $( ".requestMethod option:selected" ).text();
        var baseUri = $('.baseUri').val();
        var relativeUri = $('.relativeUri').val();
        console.log(params);
        console.log($('.baseUri').val() + $('.relativeUri').val());

        var projectBeforeScript = $('.projectBeforeScript').val();
        var projectAfterScript = $('.projectAfterScript').val();
        var requestBeforeScript = $('.requestBeforeScript').val();
        var requestAfterScript = $('.requestAfterScript').val();

        var settings = {
            type: method,
            url: baseUri,
            method: method,
            data: params,
            async: true,
            cache: false
        };

        /*
        {
            type: method,
            url: baseUri,
            method: method,
            data: params,
            async: true,
            cache: false
        };
        */

        tryEvalBefore(settings, projectBeforeScript, "Project before script");
        tryEvalBefore(settings, requestBeforeScript, "Request before script");

        $.ajax(settings)
        .always(function(data)
        {
            console.log(data);
            var status = data.status;
            var todo = data;
            if (data.responseJSON != null)
            {
                todo = data.responseJSON;
            }
            else if (data.responseText != null)
            {
                todo = data.responseText;
            }

            tryEvalAfter(todo, requestAfterScript, "Request after script");
            tryEvalAfter(todo, projectAfterScript, "Project after script");

            var beauty = JSON.stringify(todo, null, 4);
            $('.response').text(beauty);
        });
    });
});