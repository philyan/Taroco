<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Taroco-Index</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <link rel="stylesheet" href="${request.contextPath}/webjars/Semantic-UI/semantic.min.css"/>
</head>
<style type="text/css">
    body {
        background-color: #47b77d;
    }
    .title {
        font-size: 28px !important;
        font-weight: 400 !important;
    }
    .modal {
        height: 300px;
    }
    .title span{
        font-size: 16px;
        float: right;
    }
    .title a{
        font-size: 16px;
        float: right;
    }
</style>
<body>
<div class="ui modal" id="modal">
    <div class="header title">Taroco 统一认证中心
        <a href="${request.contextPath}/authentication/logout">
            [退出]
        </a>
        <span>${userName} &nbsp;|&nbsp;&nbsp;</span>
    </div>
    <div class="content">
        <p>登录成功</p>
    </div>
</div>
</body>
<script src="${request.contextPath}/webjars/jquery/jquery.min.js" ></script>
<script src="${request.contextPath}/webjars/Semantic-UI/semantic.min.js" ></script>
<script>
    $("#modal").modal({closable: false}).modal('show');
</script>
</html>
