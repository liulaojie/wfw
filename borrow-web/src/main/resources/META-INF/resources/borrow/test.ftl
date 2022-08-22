<@module.page>
    <@module.head title="${title1}">
        <style>
            .eui-layout-container{
                margin: 8px;
            }
            .eui-layout-left-navbar{
                padding-right:  8px;
            }
            .content{
                margin: 0px 12px;
                padding-bottom: 8px;
                border: 1px solid black;
            }
            .test{
                border: 1px solid red;
            }
            .left{
                background-color: rgb(55, 47, 49);
            }
        </style>

    </@module.head>
    <@module.body>
        helloworld
<#--        <div id ="epanelsplitter" ></div>-->

    </@module.body>
    <script type="text/javascript">

        require(["borrow/js/booklist"],function (booklist){
            var booklist = new booklist.BookList({
                wnd:window
            });
            EUI.addDispose(booklist,window);
        });
    </script>
</@module.page>
