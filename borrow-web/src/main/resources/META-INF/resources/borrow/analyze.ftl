<@module.page>
    <@module.head title="${title1}">
    </@module.head>
    <@module.body>
        <div id="body" class="eui-layout-container">
        </div>
    </@module.body>
    <script type="text/javascript">
        var pwnd = window.parent;
        var datas = pwnd.getTabData();
        var type = datas.type;
        require(["borrow/js/graph/"+type],function (graph){
            var graph = new graph.Analyze({
                wnd:window,
                datas:datas.data
            });
            EUI.addDispose(graph,window);
        });
    </script>
</@module.page>
