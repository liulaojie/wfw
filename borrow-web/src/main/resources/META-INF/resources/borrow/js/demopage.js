define(["eui/modules/ecoolbar", "eui/modules/elist", "eui/modules/epagebar"],
    function (ecoolbar, elist, epagebar) {
        "use strict";

        var ECoolBar = ecoolbar.ECoolBar;
        var EList = elist.EList;
        var EPageBar = epagebar.EPageBar;
        var DEFAULT_PAGESIZE = 100; //分页大小
        var TYPE_N="2"; //常量，含义某某
        var STATE_RUN = 1; //常量，含义某某
        var STATE_STOP = 2; //常量，含义某某

        /**
         * xxx界面
         * @param options 参数
         * @param {Window} [options.wnd=window] window对象
         * @param {String} [options.demoId] 某某ID
         * @param {String} [options.menuxml] 工具栏xml串
         * @param {Boolean} [options.canManage] 是否有管理权限
         */
        function DemoPageMgr(options) {
            var options = options || {};
            this.wnd = options["wnd"] || window;
            this.doc = this.wnd.document;
            this.menuxmlstr = options["menuxml"];
            this.demoId = options["demoId"];
            this.canManage = options["canManage"];
            this._initUI();
            this._initDatas();
        }

        /**
         * 销毁
         */
        DemoPageMgr.prototype.dispose = function () {
            this.coolbarObj.dispose();
            this.coolbarObj = null;
            this.listObj.dispose();
            this.listObj = null;
            this.pageObj.dispose();
            this.pageObj = null;
            this.wnd = null;
            this.doc = null;
        }

        /**
         * 工具栏菜单调用入口
         */
        DemoPageMgr.prototype.execmd = function (cmd, paramstr) {
            if (EUI.isStrEmpty(cmd)) {
                return;
            }
            var func = this[cmd];
            if (EUI.isFunction(func)) {
                func.call(this, paramstr);
            }
        }

        /**
         * 初始化
         */
        DemoPageMgr.prototype._initUI = function () {
            this._initToolBar();
            this._initList();
            this._initPagebar();
        }

        /**
         * 初始化数据
         */
        DemoPageMgr.prototype._initDatas = function () {
            this.queryData();
        }

        /**
         * 初始化工具栏
         */
        DemoPageMgr.prototype._initToolBar = function () {
            var self = this;
            var toolDom = this.doc.getElementById("toolbardom");
            this.coolbarObj = new ECoolBar({
                wnd: this.wnd,
                parentElement: toolDom,
                width: "100%",
                height: "100%",
                baseCss: "eui-coolbar-btn eui-inline-block eui-float-right"
            });
            this.coolbarObj.loadBands(this.menuxmlstr);
        }

        /**
         * 初始化表格
         */
        DemoPageMgr.prototype._initList = function () {
            var self = this;
            this.listObj = new EList({
                parentElement: self.doc.getElementById("listdom"),
                width: "100%",
                height: "100%",
                columnResize: true,     //是否拖动列宽
                autoTotalWidth: false,  //true:表示百分比对应的宽度是列表的宽度; false:表示表示百分比对应的宽度是（列表的宽度-固定列的宽度）
                ctrlMultSelect: true,   //列表是否支持Ctrl多选
                shiftMultSelect: true,  //列表是否支持Shift多选
                showblank: true,        //当表格无数据时，列表是否支持显示空白，为false是空白，为true是默认dom样式
                columns: [{
                    checkbox: true,
                    dataRener: function (checkboxdom, data, rowIndex, elist) {
                        if (data["key2"] === TYPE_N) {//不能被勾选
                            elist.disableCheckbox(checkboxdom, rowIndex);
                        }
                    }
                }, {
                    indexColumn: true,
                    start: 10           //序号列的起始值
                }, {
                    caption: I18N.getString("demo.xx.js.demopagemgr.js.key1", "图标文字"),
                    id: "key1",
                    sort: true,         //是否开启排序
                    //在文字前面添加图标，图标+文字共占一列
                    dataRener: function (span, data, rowIndex, elist) {
                        //图标加文字写法
                        data = EUI.ensureStrNotEmpty(data);
                        var strhtml = '<i class="eui-icon eui-icon-warn">&#xef9e;</i><a class="eui-elist-text-ie9">' + data + '</a>';
                        span.innerHTML = strhtml;
                        span.title = data;
                        EUI.addClassName(span, "eui-link");
                    }
                }, {
                    caption: "KEY2",
                    id: "key2",
                    hint: true
                }, {
                    caption: "KEY3",
                    id: "key3",
                    dataRener: function (span, data, rowIndex, elist) {
                        //实际值与显示值不一样时，不可加hint:true参数，需自行设置dom的title
                        var txt = "";
                        if (data === STATE_RUN ) {
                            txt = I18N.getString("demo.xx.js.demopagemgr.js.statusrun", "运行中");
                        } else if (data === STATE_STOP ) {
                            txt = I18N.getString("demo.xx.js.demopagemgr.js.statusstop", "已终止");
                        }
                        span.innerHTML = txt;
                        span.title = txt;
                    }
                }, {
                    caption: I18N.getString("demo.xx.js.demopagemgr.js.op", "操作"),
                    dataRener: function (span, data, rowIndex, elist) {
                        var rowdata = elist.getRowData(rowIndex);
                        //查看
                        var view = I18N.getString("demo.xx.js.demopagemgr.js.view", "查看");
                        var strHtml = "<a class='eui-btn eui-btn-m' id='view' title='" + view + "'>" + view + "</a>";

                        //编辑
                        var edit = I18N.getString("demo.xx.js.demopagemgr.js.edit", "编辑");
                        var cname = "eui-btn eui-btn-m";
                        if (!self.canManage) {
                            cname += " eui-disabled";
                        }
                        strHtml = "<a class='" + cname + "' id='edit' title='" + edit + "'>" + edit + "</a>";

                        //加入到span中
                        span.innerHTML = strHtml;
                    },
                    onCellClick: function (rowdata, td, evt) {
                        //注意这里直接使用组件的onCellClick ，不要自己绑定onclick事件
                        var target = evt.target;
                        if (EUI.hasClassName(target, "eui-disabled")) {
                            return false;
                        }
                        if (target.tagName === "A") {
                            if (target.id === "view") {
                                self.doView(rowdata);
                            } else if (target.id === "edit") {
                                self.doEdit(rowdata);
                            }
                        }
                    }
                }],
                datas: []
            });
        }

        /**
         * 分页栏
         */
        DemoPageMgr.prototype._initPagebar = function () {
            var self = this;
            this.pageObj = new EPageBar({
                parentElement: this.doc.getElementById("pagebardom"),
                paramobj: {
                    style: 'text',
                    pageSize: DEFAULT_PAGESIZE,
                    totalCount: 0,
                    pageIndex: 0
                },
                onshowpage: function (pageIndex, pageSize, userdata) {
                    //翻页事件
                    self.queryData(pageIndex, pageSize);
                }
            });
        }

        /**
         * 刷新数据
         */
        DemoPageMgr.prototype.queryData = function (pageIndex, pageSize, waitmsg) {
            var self = this;
            if (!waitmsg) {
                waitmsg = {
                    message: I18N.getString("demo.xx.js.demopagemgr.js.waiting", "正在加载..."),
                    timer: 800
                };
            }
            EUI.post({
                url: EUI.getContextPath() + "xxxx/hellodemo/queryDatas.do",
                data: {
                    "demoId": demoId,
                    "pageIndex": pageIndex || 0,
                    "pageSize": pageSize || DEFAULT_PAGESIZE
                },
                callback: function (queryObj) {
                    var obj = queryObj.getResponseJSON();
                    if (obj) {
                        var datas = obj.result;
                        self.listObj.refreshData(datas);
                        var count = obj.totalCount;
                        self.pageObj.resetDom(EUI.toPixNumber(count, 0), EUI.toPixNumber(pageIndex, 0));
                    }
                },
                waitMessage: waitmsg
            });
        }

        /**
         * 查看
         */
        DemoPageMgr.prototype.doView = function (rowdata) {
            var map = new EUI.Map();
            map.put("demoId", this.demoId);
            map.put("key1", rowdata.key1);
            map.put("key2", rowdata.key2);
            //这里注意使用map的export2uri，避免参数中有中文时，出现乱码问题
            var url = EUI.getContextPath() + "xxxx/hellodemo/toOtherPage.do?" + map.export2uri();
            EUI.openWindow(url, true, "yypage", true);
        }

        /**
         * 编辑
         */
        DemoPageMgr.prototype.doEdit = function (rowdata) {
            var map = new EUI.Map();
            map.put("demoId", this.demoId);
            map.put("key1", rowdata.key1);
            map.put("key2", rowdata.key2);
            var url = EUI.getContextPath() + "xxxx/hellodemo/toOtherPage.do?" + map.export2uri();
            EUI.openWindow(url, true, "yypage", true);
        }

        /**
         * 刷新
         */
        DemoPageMgr.prototype.funcRefresh = function () {
            var waitmsg = {
                message: I18N.getString("demo.xx.js.demopagemgr.js.refreshing", "刷新..."),
                finish: I18N.getString("demo.xx.js.demopagemgr.js.refreshed", "刷新完成"),
                timer: 800
            };
            this.queryData(0, DEFAULT_PAGESIZE, waitmsg);
        }

        /**
         * 新建
         */
        DemoPageMgr.prototype.funcAdd = function (paramstr) {
            var self = this;
            if ('new' === paramstr) { // 新建
                //...
            } else if ('batch' === paramstr) { // 批量新建
                //...
            }
        }

        /**
         * 导出模板
         */
        DemoPageMgr.prototype.funcExportModel = function () {
            //...
        }

        /**
         * 导出数据
         */
        DemoPageMgr.prototype.funcExportDatas = function () {
            //...
        }

        // ############################ 返回开放接口 ############################

        return {
            DemoPageMgr: DemoPageMgr
        };

    });