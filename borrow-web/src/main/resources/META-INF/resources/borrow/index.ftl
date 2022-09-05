<@module.page>
    <@module.head title="${title1}">
        <link  rel="stylesheet" type="text/css" href="http://localhost:8080/abistudy/borrow/css/index.css">
    </@module.head>
    <@module.body>
        <div id="body" class="eui-layout-container">
            <div class="eui-layout-header"></div>
            <div id="epanelsplitter" class="eui-layout-main"></div>
        </div>
    </@module.body>
    <script type="text/javascript">
        require(["borrow/js/index"],function (index){
            var index = new index.Index({
                wnd:window
            });

            EUI.addDispose(index,window);
        });
    </script>
</@module.page>
