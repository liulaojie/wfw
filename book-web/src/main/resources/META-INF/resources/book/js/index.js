define(["eui/modules/etree","eui/modules/uibase","eui/modules/epanelsplitter", "eui/modules/etabctrl"],
    function (etree, uibase,epanelsplitter,etabctrl) {
        "use strict";

        var EComponent = uibase.EComponent;
        var ETree = etree.ETree;
        var EPanelSplitter = epanelsplitter.EPanelSplitter;
        var ETabCtrl = etabctrl.ETabCtrl;
        /**
         * Index的构造函数
         */
        function Index(options){
            var self = this;
            EComponent.call(this,options);
            this._initUI();
            this._initData();
            //在window上绑定得到左树根节点的方法
            options.wnd.getTreeRootItem = function (){
                return self.treeObj.getRootItem();
            }
            //在window上绑定得到标签页中数据的方法
            options.wnd.getTabData = function (){
                var index = self.tabctrlObj.getActiveIndex();
                return self.tabctrlObj.getData(index,"datas");
            }
        }
        EUI.extendClass(Index,EComponent,"Index");

        /**
         * 初始化
         */
        Index.prototype._initUI = function () {
            var self = this;
            //引入css文件
            var dom=this.doc.getElementById("body");
            var div=this.doc.createElement("div");
            div.innerHTML = '<link  rel="stylesheet" type="text/css" href="'+EUI.getContextPath()+'book/css/index.css">'
            dom.appendChild(div);
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
                baseCss: "eui-etree-btn eui-inline-block eui-float-left eui-icon eui-tree-container eui-padding-3n" +
                    "eui-padding-top-4n eui-padding-bottom-4n eui-padding-left-6n eui-padding-right-6n" ,
                oncontextmenu:function (){
                    //屏蔽系统右键
                    return false;
                },
            });
            this.treeObj.addBaseCss("left");
            this.treeObj.setOnClick(function(item){
                var userobj = item.userObj;
                var add = true;
                if (userobj.img0=="&#xe1ab;"||userobj.img0=="&#xe23d;"){
                    add=false;
                }else{
                    var index = self.tabctrlObj.getIndex(userobj.caption);
                    if (index>=0){
                        add = false;
                    }
                    if (add){
                        //第一次打开新建标签页，并加载对应子项
                        self.addtab(item,userobj);
                    }else{
                        //存在对应标签页，跳转
                        self.tabctrlObj.setActive(index);
                    }
                }
            });
            this.treeObj.setOnExpand(function (item){//展开事件
                var userobj = item.userObj
                if (userobj.img0=="&#xee5a;"){
                    if (!item.hasVisibleChildren()){//没导入过才会导入
                        getCategoryList(item,userobj);
                    }
                }
                if (userobj.img0=="&#xe1da;"){
                    if (!item.hasVisibleChildren()){//没导入过才会导入
                        getTypeList(item,userobj);
                    }
                }

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
                caption:I18N.getString("borrow.web.js.index.js.server", "服务器"),
                level:1,
                img0:"&#xe1ab;"
            }];
            rootitem.loadFromArray(data1,function (item){
                var data2 = [{
                    id: "booklist",
                    haschild:true,
                    caption: I18N.getString("borrow.web.js.index.js.bookmgr", "图书管理"),
                    level: 2,
                    img0:"&#xee5a;"
                },{
                    id: "borrowlist",
                    caption: I18N.getString("borrow.web.js.index.js.borrowmgr", "借阅管理"),
                    level: 2,
                    img0:"&#xe266;"
                },{

                    id:"analysetable",
                    caption: I18N.getString("borrow.web.js.index.js.analyzemgr", "分析表管理"),
                    level: 2,
                    img0:"&#xe23d;"
                }
                ];
                item.loadFromArray(data2);
                var bmgr = item.getChildItem(0);
                bmgr.selectSelf();
                bmgr.doClick();
            });
        }
        /**
         * 获取大类列表
         * @param item 触发获取大类列表的ETreeItem
         * @param userObj 这个item中的用户数据
         */
        var getCategoryList = function (item,userObj){
            if (userObj.id!="booklist"){return;}
            var self = this;
            EUI.post({
                url:EUI.getContextPath()+"book/categoryList.do",
                callback:function (queryObj){
                    var obj = queryObj.getResponseJSON();
                    if (!!obj){
                        item.loadFromArray(obj,function (item) {
                            item.setHasChildren(true);
                            item.setItemImage("&#xe1da;")
                            EUI.extendObj(item.userObj,{img0:"&#xe1da;"});
                        });
                    }
                },
                waitMessage: {message: I18N.getString("ES.COMMON.LOADING", "正在加载..."),
                    finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
            })
        }
        /**
         * 获取对应大类的小类列表
         * @param item 触发获取小类列表的大类的ETreeItem
         * @param userObj 这个item中的用户数据
         */
        var getTypeList = function (item,userObj){
            var self = this;
            EUI.post({
                url: EUI.getContextPath() + "book/typeList.do",
                data:{cid:userObj.id},
                callback: function (queryObj) {
                    var obj = queryObj.getResponseJSON();
                    if (!!obj){
                        item.loadFromArray(obj,function (item) {
                            item.setItemImage("&#xe1cf;")
                            EUI.extendObj(item.userObj,{img0:"&#xe1cf;"});
                        });
                    }
                },
                waitMessage: {message: I18N.getString("ES.COMMON.LOADING", "正在加载..."),
                    finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
            })
        }
        /**
         * 初始化标签页
         */
        Index.prototype._initTabctrl =function (){
            var self = this;
            this.tabctrlObj = new ETabCtrl({
                wnd:this.wnd,
                parentElement:this.rightcontainer,
                enableclosed:true,
                style:"level1-mini ",
                baseCss:"eui-layout-row-1 eui-layout-row-first indexbody",
                onswitched:function (index){//切换标签页，对应左树高亮
                    var id = self.tabctrlObj.getData(index,"id");
                    var item = self.treeObj.getRootItem();
                    item.browseAllChildrenItems(function (item){
                        var userObj = item.userObj;
                        if (userObj.id==id){
                            self.treeObj.clearSelectedItems();
                            item.selectSelf();
                        }
                    },null,true)
                }
            })
        }
        /**
         * 新建标签页
         * @param item 触发新建标签页的ETreeItem
         * @param userobj 这个item中的用户数据
         */
        Index.prototype.addtab = function (item,userobj){
            var self = this;
                var tabobj= self.tabctrlObj;
                tabobj.add(userobj.caption,null,{
                    data:{
                        id:userobj.id,
                        level:userobj.level,
                        datas:userobj.datas
                    }
                });
                var i = tabobj.getCount();
                var dom = tabobj.getBodyDom(i-1);
                EUI.addClassName(dom,"body");
                switch (userobj.img0){
                    case "&#xee5a;"://图书管理
                        var strhtml = '<iframe style="width: 100% ;height: 100%;border: none"';
                        strhtml +='src="'+EUI.getContextPath()+'web/borrow/bookmgr.do" ></iframe>';
                        dom.innerHTML=strhtml;
                        break;
                    case "&#xe266;"://借阅管理
                        var strhtml = '<iframe src="'+EUI.getContextPath()+"web/borrow/borrowmgr.do";
                        strhtml +='" style="width: 100% ;height: 100%;border: 0px"></iframe>';
                        dom.innerHTML=strhtml;
                        break;
                    case "&#xe1da;"://大类
                        var strhtml = '<iframe src="'+EUI.getContextPath()+"web/borrow/bookmgr.do";
                        strhtml +="?cid="+userobj.id;
                        strhtml +='" style="width: 100% ;height: 100%;border: 0px"></iframe>';
                        dom.innerHTML=strhtml;
                        break;
                    case "&#xe1cf;"://小类
                        var strhtml = '<iframe src="'+EUI.getContextPath()+"web/borrow/borrowmgr.do";
                        strhtml +="?tid="+userobj.id;
                        strhtml +='" style="width: 100% ;height: 100%;border: 0px"></iframe>';
                        dom.innerHTML=strhtml;
                        break;
                    case "&#xe881;"://分析表
                        var strhtml = '<iframe src="'+EUI.getContextPath()+"web/borrow/analyze.do";
                        if (userobj.datas.type=="barchart"){
                            strhtml +='" style="width: 100% ;height: 100%;border: 0px;"></iframe>';
                        }else {
                            strhtml +='" style="width: 100% ;height: 100%;border: 0px;"></iframe>';
                        }

                        dom.innerHTML=strhtml;
                        break;
                    default:
                        break;
                }
        }
        /**
         * 销毁Index所持有的资源
         */
        Index.prototype.dispose = function (){
            this.tabctrlObj.dispose();
            this.tabctrlObj = null;
            this.treeObj.dispose();
            this.treeObj = null;
            this.splitpaneobj.dispose();
            this.splitpaneobj = null;
            EComponent.prototype.dispose.call(this);
        }
        return{
            Index :Index
        };
    });