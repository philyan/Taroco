<html>
<head>
    <meta charset="utf-8" />
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
<form id="confirmationForm" name="confirmationForm" action="${request.contextPath}/oauth/authorize" method='post'>
    <input name='user_oauth_approval' value='true' type='hidden'/>
    <#list scopes as s>
    <input name="${s}" value="true" type="hidden"/>
    </#list>
</form>
<form id="denialForm" name="denialForm" action="${request.contextPath}/oauth/authorize" method='post'>
    <input name='user_oauth_approval' value='false' type='hidden'/>
</form>
<div class="ui modal" id="modal">
    <div class="header title">授权中心
        <span>用户: ${userName}</span>
    </div>
    <div class="content">
        <p style="line-height: 2.5">
            将允许应用 ${authorizationRequest.clientId} 拥有以下权限：<br>
            <i class="user icon"></i>获取你的用户信息
        </p>
    </div>
    <div class="actions">
        <div class="ui ok button" id="ok">允许</div>
        <div class="ui cancel button" id="no">拒绝</div>
    </div>
</div>
</body>
<script src="${request.contextPath}/webjars/jquery/jquery.min.js" ></script>
<script src="${request.contextPath}/webjars/Semantic-UI/semantic.min.js" ></script>
<script>
    $("#modal").modal({closable: false}).modal('show');
    $("#ok").click(function () {
        $("#confirmationForm").submit();
    });
    $("#no").click(function () {
        $("#denialForm").submit();
    });
</script>
</html>
