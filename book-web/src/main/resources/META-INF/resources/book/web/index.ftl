<@module.page>
    <@module.head title="${title1}">
    </@module.head>
    <@module.body>
        <div id="body" class="eui-layout-container">
            <div class="eui-layout-header"></div>
            <div id="epanelsplitter" class="eui-layout-main"></div>
        </div>
    </@module.body>
    <script type="text/javascript">
        require(["borrow/web/js/index"],function (index){
            var index = new index.Index({
                wnd:window
            });

            EUI.addDispose(index,window);
        });
    </script>
</@module.page>
