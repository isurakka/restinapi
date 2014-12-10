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
    return $(row).find('.requestKey');
}

function findValue(row)
{
    return $(row).find('.requestValue');
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

function tryAddRow()
{
    console.log("tryAdd");

    var requestParams = $('.requestParams');
    var last = requestParams.last();
    var key = findKey(last).val();

    if (key.trim()) {
        var clone = last.clone();
        findKey(clone).val("");
        findValue(clone).val("");
        clone.insertAfter(last);

        last.off('change', tryAddRow);
        //findKey(clone).change(tryRemoveRow);
        findKey(clone).change(tryAddRow);
    }
}

function tryRemoveRow()
{
    var requestParams = $('.requestParams');
    var last = $(requestParams.last());
    var first = $(requestParams.first());
    var count = requestParams.length;
    requestParams.each(function(i, v) {
        if (i === 0 || i === count - 1)
        {
            return true;
        }

        var key = findKey(this).val();
        var val = findValue(this).val();

        if (!key.trim() && !val.trim())
        {
            v.remove();
            tryRemoveRow();
            return false;
        }
    });
}

$(document).ready(function() {
    var requestParams = $('.requestParams');
    window.setInterval(tryRemoveRow, 500);
    findKey(requestParams.first()).change(tryAddRow);

    $('#run').click(function() 
    {
        console.log($('.baseUri').val() + $('.relativeUri').val());
        var params = findParams();
        console.log(params);
        run($('.baseUri').val() + $('.relativeUri').val(), 'GET', {})
            .done(function(data)
            {
                var beauty = JSON.stringify(data, null, 4);
                $('.response').text(beauty);
            });;
    });
});