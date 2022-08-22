define(["eui/modules/ecoolbar","eui/modules/uibase","eui/modules/epanelsplitter", "eui/modules/ecoolbar"],
    function (epanelsplitter, ecoolbar) {
        "use strict";

        var EComponent = uibase.EComponent;
        var ECoolBar = ecoolbar.ECoolBar;
        var EPanelSplitter = epanelsplitter.EPanelSplitter;
        /**
         * 图书列表
         */
        function BookList(options){
            EComponent.call(this,option);

            this._initUI();
        }
        EUI.extendClass(BookList,EComponent,"BookList");
        /**
         * 销毁所持有的资源
         */
        BookList.prototype.dispose = function (){
            this.splitpaneobj.dispose();
            this.splitpaneobj = null;
            this.panelsplitterObj.dispose();
            this.panelsplitterObj = null;
            this.wnd = null;
            this.doc = null;
            this.parentElement = null;
            EComponent.prototype.dispose.call(this);
        }
        /**
         * 初始化
         */
        BookList.prototype._initUI = function () {
            //初始化分割面板
            this._initBody();
            //初始化左树
            this._initToolBar();
        }
        /**
         * 初始化分割面板
         */
        BookList.prototype._initBody = function (){
            this.splitpaneobj = new  EPanelSplitter({
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
            this.rightcontainer = this.vsplitpaneobj.getRightComponentContainer();
            EUI.addClassName(this.leftcontainer, "example_panel");
            EUI.addClassName(this.rightcontainer, "example_panel");
        }
        /**
         * 初始化左树
         */
        BookList.prototype._initToolBar = function (){
            var selt = this;
            this.coolbarObj = new ECoolBar({
                wnd:this.wnd,
                parentElement:this.leftcontainer,
                width: "100%",
                height: "100%",
                baseCss: "eui-coolbar-btn eui-inline-block eui-float-right"
            });
            var band = this.coolbarObj.addBand("band_1_name", true, true),
                groups = [];
            groups.push(band.addButton("", "机构 用户", 65));
            groups.push(band.addButton("", "角色 管理"));
            groups.push(band.addButton("", "免登录设置", function (item) {
                //初始化时调用的事件
                console.log("这是按钮【" + item.getCaption() + "】");
            }));
            var btn5 = band.addButton("", "权限审计");
            groups.push(btn5);
            band.addCheckAbleButtonGroups(groups, false);
            //选中
            btn5.setChecked(true);
        }

        return{
            BookList :BookList
        };
    });