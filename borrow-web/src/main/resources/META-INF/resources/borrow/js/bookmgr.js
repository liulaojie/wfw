define(["eui/modules/uibase","eui/modules/emenu", "eui/modules/ecoolbar", "eui/modules/elist", "eui/modules/eform"],
    function (uibase, emenu,ecoolbar,elist,eform) {

        var EComponent = uibase.EComponent;
        var ECoolBar = ecoolbar.ECoolBar;
        var EList= elist.EList;

        var datasobj = [
            {name: "AAA", resid: "ES$11$AAA", jdbcpool: "缺省链接池", tablename: "DIM_AAA", isslowgrow: "否", isiscache: "否", state: "默认"},
            {name: "BBB", resid: "ES$11$报表户类型", jdbcpool: "ds1", tablename: "Q_DIM_BTYPE", isslowgrow: "否", isiscache: "是", state: "进行中"},
            {name: "AAA", resid: "ES$11$报表户类型", jdbcpool: "ds1", tablename: "Q_DIM_BTYPE", isslowgrow: "否", isiscache: "否", state: "失败"},
            {name: "BBB", resid: "ES$11$报表户类型", jdbcpool: "ds1", tablename: "Q_DIM_BTYPE", isslowgrow: "否", isiscache: "否", state: "警告"},
            {name: "AAA", resid: "ES$11$报表户类型", jdbcpool: "ds1", tablename: "Q_DIM_BTYPE", isslowgrow: "否", isiscache: "是", state: "成功"},
            {name: "BBB", resid: "ES$11$报表户类型", jdbcpool: "ds1", tablename: "Q_DIM_BTYPE", isslowgrow: "否", isiscache: "否", state: "失败"}
        ];
        /**
         * BookMgr的构造函数
         */
        function BookMgr(options){
            EComponent.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            this._initUI();
            this._initData();
        }
        EUI.extendClass(Index,EComponent,"BookMgr");

        /**
         * 销毁所持有的资源
         */
        BookMgr.prototype.dispose = function (){
            this.listObj.dispose();
            this.listObj = null;

            // this.wnd = null;
            // this.doc = null;
            // this.parentElement = null;
            EComponent.prototype.dispose.call(this);
        }
        /**
         * 初始化UI界面
         */
        BookMgr.prototype._initUI = function (){
            this._initList();
        }
        /**
         * 初始化基础表格
         */
        BookMgr.prototype._initList = function (){
            this.listObj = new EList({
                width:"100%",
                height:"100%",
                columnResize: true,     //是否拖动列宽
                autoTotalWidth: false,  //true:表示百分比对应的宽度是列表的宽度; false:表示表示百分比对应的宽度是（列表的宽度-固定列的宽度）
                ctrlMultSelect: true,   //列表是否支持Ctrl多选
                shiftMultSelect: true,  //列表是否支持Shift多选
                showblank: true,        //当表格无数据时，列表是否支持显示空白，为false是空白，为true是默认dom样式
                columns:[{
                    checkbox:true,
                },{
                    indexConlum:true,
                    start:10//序号列的起始值
                },{
                    caption:"大类",
                    id:"bcaption",
                    sort:false,
                    dataRender:function (span,data){
                        //图标加文字写法
                        data = data||"";
                        var strhtml = '<i class="eui-icon eui-icon-warn">&#xe0ca;</i>'
                    }
                }

                ],
            })
        }
    });