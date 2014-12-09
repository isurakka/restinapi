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

$(document).ready(function() {
    $('#run').click(function() 
    {
        console.log($('.baseUri').val() + $('.relativeUri').val());
        var response = run($('.baseUri').val() + $('.relativeUri').val(), 'GET', {})
            .done(function(data)
            {
                var beauty = JSON.stringify(data, null, 4);
                $('.response').text(beauty);
            });;
    });
});