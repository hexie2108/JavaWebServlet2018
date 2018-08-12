<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>

</head>
<body>
<h1>Hello Worldo!</h1>
<script>

    $(document).ready(function () {
        $(".js-example-basic-single").select2({
            placeholder: 'Search',
            selectOnClose: false,
            tags: false,
            width: "30%",
            ajax: {
                dataType: "json",
                url: "${pageContext.request.contextPath}/test",
                data: function (params) {
                    return {
                        q: params.term, // search term
                        page: params.page
                    };
                },
                processResults: function (data) {
                    return {
                        results: data
                    };
                },
                cache: true
            }
        });

    });
</script>
<select class="js-example-basic-single" name="state"></select>

</body>
</html>