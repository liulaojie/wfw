<@module.page>
    <@module.head title="${title1}">
        <link  rel="stylesheet" type="text/css" href="../../borrow/css/analyze.css">
    </@module.head>
    <@module.body>
        <div id="body" class="eui-layout-container body">

        </div>
    </@module.body>
    <script type="text/javascript">
        var datas = ${datas}
        require(["borrow/js/analyze"],function (analyze){
            var analyze = new analyze.Analyze({
                wnd:window
            });
            EUI.addDispose(analyze,window);
        });
    </script>
</@module.page>
