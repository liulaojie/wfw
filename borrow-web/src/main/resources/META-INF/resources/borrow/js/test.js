define(["eui/modules/epanelsplitter", "eui/modules/ecoolbar","eui/modules/uibase"],
    function (epanelsplitter, ecoolbar,uibase) {
            // var splitpaneobj = new epanelsplitter.EPanelSplitter({
            //     wnd : window,
            //     ishorizontal: true,      //true-水平的，左右分隔；false-垂直的，上下分隔； 缺省的为true
            //     fixedright: false,       //true-固定下或右边； 缺省的是false
            //     parentElement : document.getElementById("epanelsplitter"),
            //     fixedSize: 270           //固定面板的宽度/高度
            //     //candrag: false   //中间的拖拽按钮是否显示
            // });
            // splitpaneobj.setSplitbarWidth(8);  //中间拖拽容器的宽度，可用来作左右布局的间距；去掉时，默认为4px； 这里数字设置不是4/8的时候，请将中间的的拖拽按钮隐藏；
            // //因为看不清，将2边都设置上背景色，方便查看
            // var leftcontainer = splitpaneobj.getLeftComponentContainer(),
            //     rightcontainer = splitpaneobj.getRightComponentContainer();
            // EUI.addClassName(leftcontainer, "example_panel");
            // EUI.addClassName(rightcontainer, "example_panel");
        var Epanelsplitter = epanelsplitter.EPanelSplitter;
        var EComponent = uibase.EComponent;

        function Test(options){
            var options = options||{};
            EComponent.call(this,options)
            this._initUI();
        }
        Test.prototype._initUI = function (){
            this.panelsplitterObj = new Epanelsplitter({
                wnd:this.wnd,
                ishorizontal: true,
                parentElement:document.getElementById("epanelsplitter"),
                fixedSize:270
            });
            var splitpaneobj = this.panelsplitterObj;
            splitpaneobj.setSplitbarWidth(8);
            var leftcontainer = splitpaneobj.getLeftComponentContainer(),
                rightcontainer = splitpaneobj.getRightComponentContainer();
            EUI.addClassName(leftcontainer, "example_panel");
            EUI.addClassName(rightcontainer, "example_panel");
        }
        return{
            Test:Test
        }
    })