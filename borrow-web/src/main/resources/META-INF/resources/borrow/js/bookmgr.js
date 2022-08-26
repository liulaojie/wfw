define(["eui/modules/uibase", "eui/modules/ecoolbar", "eui/modules/elist", "eui/modules/epagebar"],
    function (uibase, ecoolbar,elist,epagebar) {

        var EComponent = uibase.EComponent;
        var ECoolBar = ecoolbar.ECoolBar;
        var EList= elist.EList;
        var EPageBar = epagebar.EPageBar;
        BookMgr.prototype.pageIndex =0;
        BookMgr.prototype.bcaption;
        BookMgr.prototype.scaption;
        /**
         * 自定义分页条
         */
        var customSort = [
            {id:"first",text:"首页",cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"previous",text:"上一页",cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"main",text:"第,页  共{0}页",cssText:";color:#000000;font-weight: bold;"},
            {id:"next",text:"下一页",cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"last",text:"末页",cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"total",text:"每页三十条共{0}条",cssText:";color:#000000;font-weight: bold;"}
            ]
        /**
         * BookMgr的构造函数
         */
        function BookMgr(options){
            var self = this;
            EComponent.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            self.bcaption=options.bcaption;
            self.scaption=options.scaption
            this._initUI();
            this._initData(self.bcaption,self.scaption,self.pageIndex);
        }
        EUI.extendClass(BookMgr,EComponent,"BookMgr");

        /**
         * 销毁所持有的资源
         */
        BookMgr.prototype.dispose = function (){
            this.dialogObj.dispose();
            this.dialogObj=null;
            this.coolbarObj.dispose();
            this.coolbarObj = null;
            this.listObj.dispose();
            this.listObj = null;
            this.pageBarObj.dispose();
            this.pageBarObj = null;

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
            this._initCoolBar();
            this._initPageBar();
        }

        /**
         * 初始化工具栏
         */
        BookMgr.prototype._initCoolBar = function (){
            var self = this;
            this.coolbarObj = new ECoolBar({
                parentElement: document.getElementById("toolbardom"),
                width:"100%",
                height: "100%",
                baseCss:"eui-coolbar-btn"
            });
            var band = this.coolbarObj.addBand("band_name1",true,true);
            var dom= band.addButton(null,"新建图书");
            dom.setName("newBook");
            dom.setOnAfterClick(function (){
                self._showDialog(true,null);

            })
        }

        /**
         * 初始化基础表格
         */
        BookMgr.prototype._initList = function (){
            var  self = this;
            this.listObj = new EList({
                parentElement:document.getElementById("listdom"),
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
                    caption:"大类",
                    id:"bcaption",
                    sort:false,

                },{
                    caption:"小类",
                    id:"scaption",
                    indexConlum:true,
                    start:10,//序号列的起始值
                    hint:true,      //该列是否开启提示

                },{
                    caption:"书名",
                    id:"name",
                    hint:true,      //该列是否开启提示
                },{
                    caption:"描述",
                    id:"desc",
                    width: 500,
                    hint:true,      //该列是否开启提示
                },{
                    caption:"操作",
                    width:"200px",
                    dataRender:function (cell){
                        var strhtml = '<a class="eui-btn eui-btn-m">编辑</a>'
                        cell.innerHTML = strhtml;
                    },
                    onCellClick: function (rowdata,td,evt){
                        var target = evt.target;
                        if (target.tagName==="A"){
                            self._showDialog(false,rowdata);
                        }
                    }
                }
                ],

            })
        }
        /**
         * 初始化分页条
         */
        BookMgr.prototype._initPageBar = function (){
            var self = this;
            this.pageBarObj = new EPageBar({
                parentElement:document.getElementById("pagebardom"),
                paramobj:{
                    style:"text",
                    pageSize:30,
                    totalCount:76,
                    pageIndex:0,
                    listpage:false
                },
                isCustom:true,
                baseCss: "eui-pagebar-list",
                showNumberClick:false,//是否显示数字点击项，默认显示
                customSort:customSort,
                onshowpage:function (pageIndex,pageSize){
                    self.pageIndex = pageIndex
                    self._initData(self.bcaption,self.scaption,pageIndex);
                }
            })
        }
        /**
         * 显示对话框
         */
        BookMgr.prototype._showDialog = function (isnew,datas){
            /**
             * (1)对话框公用，不存在才创建对话框
             * (2)对话框创建在rootwindow上， EUI.getRootWindow()
             * (3)对话框的引用记录在Object01对象上 this.exportdlg
             */
            var self = this;
            if(!self.bookdialog) {
                var options = {wnd:EUI.getRootWindow(),};
                EUI.extendObj(options,{isnew:isnew});
                var dlg = require("borrow/js/bookdialog");
                self.bookdialog = new dlg.BookDialog(options);
                //设置点击确定的回调函数
                self.bookdialog.setOnok(function (){
                    self._initData(self.bcaption,self.scaption,self.pageIndex);
                })
            }
            self.bookdialog.showModal();
            //打开对话框
            self.bookdialog.open();
            //设置数据
            if (!isnew){
                self.bookdialog.setValue(datas);
            }

        }
        /**
         * 隐藏对话框
         */
        BookMgr.prototype._hideDialog = function (){
            var self = this;
        }
        /**
         * 初始化列表中数据
         */
        BookMgr.prototype._initData = function (bcaption,scaption,pageIndex){
            var self = this;
            EUI.post({
                url:EUI.getContextPath()+"web/borrow/bookList.do",
                data:{
                    bcaption:bcaption,
                    scaption:scaption,
                    pageIndex:pageIndex
                },
                callback:function (queryObj){
                    var obj = queryObj.getResponseJSON();
                    if (!!obj){
                        self.listObj.clear(true);
                        for (var i=0;i<obj.length;i++){
                           self.listObj.addRow(obj[i]);
                        }
                    }
                }
            })
        }



        return{
            BookMgr :BookMgr
        };
    });