<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Taroco-Index</title>
    <link rel="stylesheet" href="/css/reset.css"/>
    <link rel="stylesheet" href="/css/common.css"/>
    <link rel="stylesheet" href="/css/font-awesome.min.css"/>
</head>
<body>
<div>
    <p style="font-size: 24px">你好 <b id="username"></b>! 欢迎来到Taroco
        <button id="logout">退出登录</button>
    </p>
</div>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script>
    $(function () {
        $('#logout').click(function () {
            window.location.href = '/authentication/logout'
        });

        getUserInfo();
        function getUserInfo() {
            $.ajax({
                url: "/authentication/user",
                type: "GET",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data, textStatus, jqXHR) {
                    if (data) {
                        $('#username').text(data.username)
                    }
                }
            });
        }
    });
</script>
</body>
</html>
