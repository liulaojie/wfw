define(["eui/modules/etree","eui/modules/uibase","eui/modules/epanelsplitter", "eui/modules/etabctrl"],
    function (etree, uibase,epanelsplitter,etabctrl) {
        "use strict";

        var EComponent = uibase.EComponent;
        var ETree = etree.ETree;
        var EPanelSplitter = epanelsplitter.EPanelSplitter;
        var ETabCtrl = etabctrl.ETabCtrl;
        /**
         * 图书列表构造函数
         */
        function Index(options){
            EComponent.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            this._initUI();
            this._initData();
        }
        EUI.extendClass(Index,EComponent,"Index");

        /**
         * 初始化
         */
        Index.prototype._initUI = function () {
            //初始化分割面板
            this._initBody();
            //初始化左树
            this._initTree();
            //初始化标签页
            this._initTabctrl();
        }
        Index.prototype._initData = function (){
            this._initTreeData();
        }

        /**
         * 初始化分割面板
         */
        Index.prototype._initBody = function (){
            var self = this;
            this.splitpaneobj = new EPanelSplitter({
                wnd: this.wnd,
                ishorizontal: true,      //true-水平的，左右分隔；false-垂直的，上下分隔； 缺省的为true
                fixedright: false,       //true-固定下或右边； 缺省的是false
                parentElement: document.getElementById("epanelsplitter"),
                fixedSize: 270           //固定面板的宽度/高度
                //candrag: false   //中间的拖拽按钮是否显示
            });
            this.splitpaneobj.setSplitbarWidth(8);  //中间拖拽容器的宽度，可用来作左右布局的间距；去掉时，默认为4px； 这里数字设置不是4/8的时候，请将中间的的拖拽按钮隐藏；
            //因为看不清，将2边都设置上背景色，方便查看
            this.leftcontainer = this.splitpaneobj.getLeftComponentContainer();
            this.rightcontainer = this.splitpaneobj.getRightComponentContainer();
            EUI.addClassName(this.leftcontainer, "example_panel");
            EUI.addClassName(this.rightcontainer, "example_panel");

        }
        /**
         * 初始化左树
         */
        Index.prototype._initTree = function (){
            var self = this;
            this.treeObj = new ETree({
                wnd:this.wnd,
                parentElement:this.leftcontainer,//添加到分割面板的左边部分
                width: "100%",
                height: "100%",
                baseCss: "eui-etree-btn eui-inline-block eui-float-left eui-icon eui-tree-container etree" ,
                oncontextmenu:function (){
                    return false;
                },
            });
            this.treeObj.addBaseCss("left");
            this.treeObj.setOnClick(function(item){
                var tabobj= self.tabctrlObj;
                tabobj.add(item.userObj.caption);
                var i = tabobj.getCount();
                var dom = tabobj.getBodyDom(i-1);
                dom.style.border="solid";
            });
        }

        /**
         * 初始化树中数据
         */
        Index.prototype._initTreeData=function (){
            var self = this;
            var rootitem = self.treeObj.getRootItem();
            var data1 = [{
                id:"fwq",
                caption:"服务器",
                level:1,
                img0:"&#xe1ab;"
            }];
            rootitem.loadFromArray(data1,function (item){
                var data2 = [{
                    id: "booklist",
                    haschild:true,
                    caption: "图书管理",
                    level: 2,
                    img0:"&#xe367;"
                },{
                    id: "borrowlist",
                    caption: "记录查询",
                    level: 2,
                    img0:"&#xe1cd;"
                },{
                    id:"analysetable",
                    caption: "分析表管理",
                    level: 2,
                    img0:"&#xe23d;"
                }
                ];
                item.loadFromArray(data2,function (item,userObj){
                        getCategoryList(item,userObj);
                    }

                );
                var bmgr = item.getChildItem(0);
                bmgr.selectSelf();
                bmgr.doClick();
            });
        }
        /**
         * 获取大类列表
         */
        var getCategoryList = function (item,userObj){
            if (userObj.id!="booklist"){return;}
            var self = this;
            EUI.get({
                url:EUI.getContextPath()+"web/borrow/categoryList.do",
                callback:function (queryObj){
                    var obj1 = queryObj.getResponseJSON();
                    if (!!obj1){
                        var data3 = new Array();
                        for (var i=0;i<obj1.length;i++){
                            data3[i]={
                                id : obj1[i].id,
                                caption:obj1[i].caption,
                                img0:"&#xe1da;",
                                haschild:true,
                                level:3
                            }
                        }
                        item.loadFromArray(data3,
                            function (item,userObj) {
                                getTypeList(item,userObj);

                            }
                        );
                    }
                }
            })
        }
        /**
         * 获取大类对应的小类列表
         */
        var getTypeList = function (item,userObj){
            var self = this;
            EUI.get({
                url: EUI.getContextPath() + "web/borrow/typeList.do",
                data:{cid:userObj.id},
                callback: function (queryObj) {
                    var obj2 = queryObj.getResponseJSON();
                    if (!!obj2) {
                        var data4 = new Array();
                        for (var i = 0; i < obj2.length; i++) {
                            data4[i] = {
                                id: obj2[i].id,
                                caption: obj2[i].caption,
                                img0: "&#xe1da;",
                                level: 4
                            }
                        }
                    }
                    item.loadFromArray(data4);
                }
            })
        }
        /**
         * 初始化标签页
         */
        Index.prototype._initTabctrl =function (){
            this.tabctrlObj = new ETabCtrl({
                wnd:this.wnd,
                parentElement:this.rightcontainer,
                enableclosed:true,
                style:"level1-mini ",
                baseCss:"eui-layout-row-1 eui-layout-row-first body"
            })
        }

        /**
         * 销毁所持有的资源
         */
        Index.prototype.dispose = function (){
            this.splitpaneobj.dispose();
            this.splitpaneobj = null;
            this.treeObj.dispose();
            this.treeObj = null;
            this.tabctrlObj.dispose();
            this.tabctrlObj = null;
            // this.wnd = null;
            // this.doc = null;
            // this.parentElement = null;
            EComponent.prototype.dispose.call(this);
        }

        return{
            Index :Index
        };
    });