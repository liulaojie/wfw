<@module.page>
    <@module.head title='${title!I18N.getString("demo.xxx.demopage.ftl.title","没传标题的页面")}'>
    </@module.head>
    <@module.body>
        <div class="eui-layout-container">
            <div id="toolbardom" class="eui-layout-row-1 eui-layout-row-first"></div>
            <div id="listdom" class="eui-layout-row-3 eui-layout-row-offset-1 eui-layout-row-offsetbottom-1 eui-padding-left-20 eui-padding-right-20"></div>
            <div id="pagebardom" class="eui-layout-row-1 eui-layout-row-last eui-padding-top-10 eui-padding-left-10 eui-padding-right-10 eui-margin-bottom-5 eui-align-center"></div>
        </div>
    </@module.body>
    <script type="text/javascript">
        <@menu path="menu_demo.xml" jsvar="menuxml" module="borrow-web"/>
        var mgrobj;
        require(["../borrow/js/demopage"], function(demopage) {
            var options = {
                wnd : window,
                menuxml : menuxml,
                demoId : "${demoId!''}",
                canManage : ${canManage?string('true','false')}
            };
            mgrobj = new demopage.DemoPageMgr(options);
            //这里注意，new对象后，要立刻调用EUI.addDispose
            EUI.addDispose(mgrobj);
        });

        /**
         * 工具栏的触发事件入口方法，对应xml中配置的方法
         */
        function execmd(cmd, paramstr){
            if(!mgrobj) {
                return '';
            }
            return mgrobj.execmd(cmd, paramstr);
        }
    </script>
</@module.page>