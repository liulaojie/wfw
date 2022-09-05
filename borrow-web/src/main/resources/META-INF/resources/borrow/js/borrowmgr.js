define(["eui/modules/uibase", "eui/modules/ecoolbar", "eui/modules/elist", "eui/modules/epagebar"],
    function (uibase, ecoolbar,elist,epagebar) {
        var EComponent = uibase.EComponent;
        var ECoolBar = ecoolbar.ECoolBar;
        var EList= elist.EList;
        var EPageBar = epagebar.EPageBar;
        var chedata = new Map() ;//记录列表中被选择的数据的ID
        /**
         * 自定义分页条
         */
        var customSort = [
            {id:"first",text:I18N.getString("borrow.js.first", "首页"),cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"previous",text:I18N.getString("borrow.js.Previous", "上一页"),cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"main",text:I18N.getString("borrow.js.main", "第,页  共{0}页"),cssText:";color:#000000;font-weight: bold;"},
            {id:"next",text:I18N.getString("borrow.js.next", "下一页"),cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"last",text:I18N.getString("borrow.js.last", "下一页"),cssText:";color:#87CEEB;font-weight: bold;"},
            {id:"total",text:I18N.getString("borrow.js.total", "每页三十条共{1}条"),cssText:";color:#000000;font-weight: bold;"}
            ]
        /**
         * BorrowMgr的构造函数
         */
        function BorrowMgr(options){
            var self = this;
            EComponent.call(this,options);
            // var options = options||{};
            this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            self.scaption=options.scaption
            this._initUI();
            this._initData(0);
        }
        EUI.extendClass(BorrowMgr,EComponent,"BorrowMgr");

        /**
         * 销毁所持有的资源
         */
        BorrowMgr.prototype.dispose = function (){
            this.borrowdialog.dispose();
            this.borrowdialog=null;
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
        BorrowMgr.prototype._initUI = function (){
            this._initList();
            this._initCoolBar();

        }

        /**
         * 初始化工具栏
         */
        BorrowMgr.prototype._initCoolBar = function (){
            var self = this;
            this.coolbarObj = new ECoolBar({
                parentElement: document.getElementById("toolbardom"),
                width:"100%",
                height: "100%",
                baseCss:"eui-coolbar-btn"
            });
            var band = this.coolbarObj.addBand("band_name1",true,true);
            if (self.scaption==''){
                var createAdom= band.addButton(null,I18N.getString("borrow.js.createanalyze", "生成分析表"));
                createAdom.setName("createAnalyze");
                createAdom.setOnAfterClick(function (){//生成分析表
                    self.createAnalyze();
                })
            }
            var addBdom= band.addButton(null,I18N.getString("borrow.js.borrowmgr.js.addBorrow", "新建借阅记录"));
            addBdom.setName("addBorrow");
            addBdom.setOnAfterClick(function (){
                self._borrowDialog();
            })
        }

        /**
         * 初始化基础表格
         */
        BorrowMgr.prototype._initList = function (){
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
                columns:self._initColumns()
            })
        }
        /**
         * 初始化列表项
         */
        BorrowMgr.prototype._initColumns = function (){
            var self = this;
            var columns = new Array();
            columns.push({
                checkbox:self.scaption=='',
                start: 1
            });
            columns.push({
                indexColumn: true,
                start: 1           //序号列的起始值
            });
            if (self.scaption==''){
                columns.push({
                    caption:I18N.getString("borrow.js.bcaption", "大类"),
                    id:"bcaption",
                });
                columns.push({
                    caption:I18N.getString("borrow.js.scaption", "小类"),
                    id:"scaption",
                });
            }
            columns.push({
                caption:I18N.getString("borrow.js.book", "书名"),
                id:"book",
                hint:true,      //该列是否开启提示
            });
            columns.push({
                caption:I18N.getString("borrow.js.person", "借阅人"),
                id:"person",

                hint:true,      //该列是否开启提示
            });
            columns.push({
                caption:I18N.getString("borrow.js.fromdate", "借阅日期"),
                id:"fromdate",
                // width: 300,
                hint:true,      //该列是否开启提示
                dataRender:function (span, data, rowIndex, elist) {
                    span.innerHTML = EUI.date2String(new Date(data), "yyyy年mm月dd日 hh:ii:ss");
                }
            });
            columns.push({
                caption:I18N.getString("borrow.js.todate", "归还日期"),
                id:"todate",
                // width: 300,
                hint:true,      //该列是否开启提示
                dataRender:function (span, data, rowIndex, elist) {
                    if (data==null){
                        span.innerHTML = I18N.getString("borrow.js.borrowmgr.js.noreturn", "尚未归还")
                    }else{
                        span.innerHTML = EUI.date2String(new Date(data), "yyyy年mm月dd日 hh:ii:ss");
                    }

                }
            });
            columns.push({
                caption:I18N.getString("borrow.js.borrowmgr.js.operation", "操作"),
                // width:"200px",
                dataRender:function (cell){
                    var strhtml = ' <a id="delete" class="eui-btn eui-btn-m">'
                        +I18N.getString("borrow.js.borrowmgr.js.delete", "删除")+'</a> '
                    strhtml +=' <a id="return" class = "eui-btn eui-btn-m">'
                        +I18N.getString("borrow.js.borrowmgr.js.return", "还书")+'</a>'
                    cell.innerHTML = strhtml;
                },
                onCellClick: function (rowdata,td,evt){
                    var target = evt.target;
                    if (target.tagName==="A"){
                        if (target.id=="delete"){
                            self.delBorrow(rowdata);
                        }
                        else{
                            self.returnBook(rowdata);
                        }
                    }
                }
            });
            return columns
        }
        /**
         * 初始化分页条
         */
        BorrowMgr.prototype._initPageBar = function (){
            var self = this;
            this.pageBarObj = new EPageBar({
                parentElement:document.getElementById("pagebardom"),
                paramobj :{
                    style:"text",
                    pageSize:30,
                    totalCount:self.totalCount,
                    pageIndex:0,
                    listpage:false
                },
                isCustom:true,
                baseCss: "eui-pagebar-list",
                showNumberClick:false,//是否显示数字点击项，默认显示
                customSort:customSort,
                onshowpage:function (pageIndex,pageSize){
                    self.pageIndex = pageIndex
                    self._initData(pageIndex);
                }
            })
        }
        /**
         * 显示新添借阅对话框
         */
        BorrowMgr.prototype._borrowDialog = function (){
            /**
             * (1)对话框公用，不存在才创建对话框
             * (2)对话框创建在rootwindow上， EUI.getRootWindow()
             * (3)对话框的引用记录在Object01对象上 this.exportdlg
             */
            var self = this;
            if(!self.borrowdialog) {
                var options = {
                    wnd:EUI.getRootWindow(),
                    scaption : self.scaption
                };

                var dlg = require("borrow/js/borrowdialog");
                self.borrowdialog = new dlg.BorrowDialog(options);
                //设置点击确定的回调函数
                self.borrowdialog.setOnok(function (){
                    self.pageIndex=0;
                    self._initData(self.pageIndex);
                })
            }
            //打开对话框
            self.borrowdialog.open(true);
        }
        /**
         * 初始化数据
         */
        BorrowMgr.prototype._initData = function (pageIndex){
            var self = this;
            //初始化列表中
            EUI.post({
                url:EUI.getContextPath()+"web/borrow/borrowList.do",
                data:{
                    scaption:self.scaption,
                    pageIndex:pageIndex
                },
                callback:function (queryobj){
                    var obj = queryobj.getResponseJSON();
                    if (obj){
                        self.getCheck();
                        self.listObj.clear(true);
                        self.listObj.refreshData(obj.list);
                        self.totalCount = obj.totalCount;
                        if (self.pageBarObj==null){
                            self._initPageBar();
                        }else{
                            self.pageBarObj.resetDom(obj.totalCount,pageIndex);
                        }
                    }
                },
                waitMessage: {message: I18N.getString("ES.COMMON.LOADING", "正在加载..."),
                    finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
            })
        }
        /**
         * 点击删除事件
         */
        BorrowMgr.prototype.delBorrow = function (data){
            var self = this;
            EUI.confirmDialog(I18N.getString("borrow.js.borrowmgr.js.deleteevn", "确认"),
                I18N.getString("ES.COMMON.AFFIRM", '是否确认删除“{0}”于{1}借阅《{2}》的记录',
                    [data.person,data.fromdate,data.book]), false,
                function(){
                },
                function(){
                    EUI.post({
                        url:EUI.getContextPath()+"web/borrow/deleteBorrow.do",
                        data:{
                            id:data.id
                        },
                        callback:function (queryObj){
                            var obj = queryObj.getResponseJSON();
                            //刷新数据
                            self._initData(0);
                            this.close();
                        },
                        waitMessage: {message: I18N.getString("ES.COMMON.DELETING", "正在删除..."),
                            finish: I18N.getString("ES.COMMON.DELETESUCCESS", "删除成功")}
                    })

                },
                function(){

                }
            );
        }
        /**
         * 点击还书事件
         */
        BorrowMgr.prototype.returnBook = function (data){
            var self = this;
            EUI.confirmDialog(I18N.getString("borrow.js.borrowmgr.js.returnevn", "确认"),
                I18N.getString("ES.COMMON.AFFIRM", '是否确定“{0}”于{1}借阅《{2}》的借阅的数据已归还',
                    [data.person,data.fromdate,data.book]), false,
                function(){
                },
                function(){
                    if (data.todate!=I18N.getString("borrow.js.borrowmgr.js.noreturn", "尚未归还")){
                        EUI.showMessage(I18N.getString("borrow.js.borrowmgr.js.returned", "该书本已归还"),
                            I18N.getString("borrow.js.borrowmgr.js.message", "消息"));
                    }else{
                        data.todate= EUI.date2String(new Date(), "yyyymmdd")
                        EUI.post({
                            url:EUI.getContextPath()+"web/borrow/saveBorrow.do",
                            data:{
                                id:data.id,
                                todate:data.todate,
                            },
                            callback:function (queryObj){
                                var obj = queryObj.getResponseJSON();
                                //刷新数据
                                self.pageIndex = 0;
                                self._initData(self.pageIndex);
                                this.close();
                            },
                            waitMessage: {message: I18N.getString("borrow.js.borrowmgr.js.returning", "正在还书..."),
                                finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
                        })
                    }
                },
                function(){

                }
            );
        }
        /**
         * 点击生成分析表事件
         */
        BorrowMgr.prototype.createAnalyze = function (){
            var self = this;
            var datas=self.listObj.getCheckDatas();
            if (datas==null){//
                EUI.showMessage(I18N.getString("borrow.js.borrowmgr.js.nocheck", "没有勾选任何数据"), I18N.getString("borrow.js.borrowmgr.js.message", "消息"));
            }else{
                self.getCheck();
                self._analyzeDialog();
            }
        }
        /**
         * 显示生成分析表对话框
         */
        BorrowMgr.prototype._analyzeDialog = function (){
            /**
             * (1)对话框公用，不存在才创建对话框
             * (2)对话框创建在rootwindow上， EUI.getRootWindow()
             * (3)对话框的引用记录在Object01对象上 this.exportdlg
             */
            var self = this;
            if(!self.analyzedialog) {
                var options = {wnd:EUI.getRootWindow(),};
                var dlg = require("borrow/js/analyzedialog");
                self.analyzedialog = new dlg.AnalyzeDialog(options);
                //设置点击确定的回调函数
                self.analyzedialog.setOnok(function (title,type){
                    var pwnd= self.wnd.parent;
                    var rootItem= pwnd.getTreeRootItem();
                    var item = rootItem.getChildItem(0);
                    item = item.getChildItem(2);
                    var child = item.getAllChildrenItems(null,true);
                    var isnew = true;
                    for (var i=0;i<child.length;i++){
                        if (child[i].userObj.id==title){
                            isnew=false;
                        }
                    }
                    if (isnew){
                        var data = [{
                            id:title,
                            caption:title,
                            level:3,
                            img0:"&#xe881;",
                            datas:{
                                type:type,
                                data:chedata
                            }
                        }]
                        item.loadFromArray(data,function (item){
                            item.selectSelf();
                            item.doClick();
                        });
                        //清空历史选择的数据
                        chedata = new Map();
                        var indexs = self.listObj.getCheckRows();
                        self.listObj.setCheckRows(indexs,false);
                        return true;
                    }
                    else{
                        return false;
                    }
                })
            }
            //打开对话框
            self.analyzedialog.open(true);
        }
        /**
         * 获取当前列表中被选取的数据
         */
        BorrowMgr.prototype.getCheck = function (){
            var self = this;
            var k = 0;
            var chedatanow = self.listObj.getCheckDatas();
            if (chedatanow!=null){
                for (var i=0;i<chedatanow.length;i++){
                    chedata.set(chedatanow[i].id,chedatanow[i]);
                }
            }

        }

        /**
         * 列表中被选择过的数据标识选中
         */
        BorrowMgr.prototype.setCheck = function (){
            var self = this;
            var targets = self.listObj.getDatas();
            for (var i=0;i<targets.length;i++){
                if (chedata.has(targets[i].id)){
                    self.listObj.setCheckRow(i,true);
                    chedata.delete(targets[i].id);
                }
            }


        }
        return{
            BorrowMgr :BorrowMgr
        };
    });