define([ "eui/modules/edialog","eui/modules/eform","eui/modules/ecombobox"],
    function (edialog,eform,ecombobox) {
        var EDialog = edialog.EDialog;
        var eform = eform.eform;
        var EListCombobox = ecombobox.EListCombobox;
        var tid;//小类ID
        var cid;//大类ID
        /**
         * BookDialog的构造函数,继承EDialog
         */
        function BookDialog(options){
            EDialog.call(this,options);
            this._init(options);

        }
        EUI.extendClass(BookDialog,EDialog,"BookDialog");

        /**
         * 销毁BookDialog所持有的资源
         */
        BookDialog.prototype.dispose = function (){
            this.bComboboxObj.dispose();
            this.bComboboxObj = null;
            this.sComboboxObj.dispose();
            this.sComboboxObj = null;
            EDialog.prototype.dispose.call(this);
        }
        /**
         * 初始化对话框
         */
        BookDialog.prototype._init=function (options){
            var self = this;
            self._options = options||{};
            self.isnew = self._options["isnew"];
            if (self.isnew){//弹窗标题
                self._options["caption"] = self._options["caption"]||I18N.getString("borrow.web.js.bookdialog.js.addbook", "新建图书");
            }else{
                self._options["caption"] = self._options["caption"]||I18N.getString("borrow.web.js.bookdialog.js.editbook", "编辑图书");
            }
            self._options["width"] = self._options["width"]||380;
            self._options["height"] = self._options["height"]||400;
            EDialog.call(self,self._options);
            self.setCanResizable(false);
            self.setMaxButtonVisible(false);
            self.data = {};
            self._initUI();
        }
        /**
         * 初始化UI界面
         */
        BookDialog.prototype._initUI = function (){
            var self = this;
            var content = self.getContent();
            var strhtml = [];
            strhtml.push('<div class="eui-layout-container  eui-scroll-auto ">');
            strhtml.push('  <div id = "bookdialog-info-content">');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> '+
                                        I18N.getString("borrow.web.js.bookdialog.js.name", "书名")+'：</label>');
            strhtml.push('          <div  class="eui-input-block"><input id="name" type="text" class="eui-form-input" ' +
                '                       placeholder="'+I18N.getString("borrow.web.js.bookdialog.js.planame", "请填写书名")+'">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="nametips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required">'+I18N.getString("borrow.web.js.bookdialog.js.bcaption", "大类")+'：</label>');
            strhtml.push('          <div class="eui-input-block" id="bcombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="bcomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> '
                                        +I18N.getString("borrow.web.js.bookdialog.js.scaption", "小类")+'：</label>');
            strhtml.push('          <div class="eui-input-block" id="scombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="scomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label  class="eui-form-label "> '
                                    +I18N.getString("borrow.web.js.bookdialog.js.desc", "图书简介")+'：</label>');
            strhtml.push('          <div  class="eui-input-block"><textarea id="desc" style="width: 200px" class="eui-form-textarea" ' +
                '                       placeholder='+I18N.getString("borrow.web.js.bookdialog.js.nodesc", "请填写图书的简介信息没有可以不填，")
                                        +' ></textarea></div>');
            strhtml.push('           ');
            strhtml.push('      </div>');
            strhtml.push('  </div>');
            strhtml.push('</div>');
            content.innerHTML = strhtml.join(" ");
            var dom= EUI.getChildDomByAttrib(this.getBaseDom(),"class","eui-dialog-body",true)
            EUI.addClassName(dom,"eui-padding-top-4n eui-padding-bottom-4n eui-padding-right-6n")
            self.addBottomButtom();
            self._initBList();
            self._initSList();
        }
        /**
         * 初始化大类列表
         */
        BookDialog.prototype._initBList = function (){
            var self = this;
            var content = $(self.getContent());
            this.bComboboxObj  =new EListCombobox({
                wnd: this.wnd,
                parentElement: content.find("#bcombobox"),
                width:200,
                height:"100%",
                showFilter:false,
                showCheckAll:false,
                placeholder:I18N.getString("borrow.web.js.bookdialog.js.choosebcaption", "请选择大类"),
                data$key: "caption",
                onclickitem:function (rowdata, td, evt, isCheck){
                    cid = rowdata.id;
                    self.scaption = null;
                    self.refresh(cid);
                }
            });
            self.getCategoryList();
        }
        /**
         *
         * 初始化小类列表
         */
        BookDialog.prototype._initSList = function (){
            var self = this;
            this.sComboboxObj =new EListCombobox({
                parentElement: EUI.getChildDomByAttrib(this.getBaseDom(),"id","scombobox",true),
                width:200,
                height:"100%",
                columns:[{caption: I18N.getString("borrow.web.js.bookdialog.js.title", "标题"), id:"caption", width: "100%"}],
                placeholder:I18N.getString("borrow.web.js.bookdialog.js.choosescaption", "请选择小类"),
                data$key: "caption",
                showFilter:false,
                showCheckAll:false,
                onclickitem:function (rowdata, td, evt, isCheck){
                    tid = rowdata.id;
                }
            })
        }
        /**
         * 获取大类列表
         */
        BookDialog.prototype.getCategoryList = function (){
            var self = this;
            EUI.post({
                url:EUI.getContextPath()+"book/categoryList.do",
                callback:function (queryObj){
                    var obj = queryObj.getResponseJSON();
                    self.bComboboxObj.setDatas(obj);
                    if (!isNull(self.bcaption)){
                        self._setBScaption();
                    }
                },
                waitMessage: {message: I18N.getString("ES.COMMON.LOADING", "正在加载..."),
                    finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
            })

        }
        /**
         * 获取大类对应的小类列表
         */
        BookDialog.prototype.refresh = function (cid){
            var self = this;
            EUI.post({
                url: EUI.getContextPath() + "book/typeList.do",
                data:{
                    cid:cid
                },
                callback: function (queryObj) {
                    var obj = queryObj.getResponseJSON();
                    self.sComboboxObj.setDatas(obj);
                    if (isNull(self.scaption)){
                    }else{
                        self.sComboboxObj.setSelectValue(self.scaption,false);
                        var elist = self.sComboboxObj.getEList();
                        var data = elist.getSelectDatas();
                        tid = data[0].id;
                    }
                },
                waitMessage: {message: I18N.getString("ES.COMMON.LOADING", "正在加载..."),
                    finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
            })
        }
        /**
         * 绑定按钮事件
         */
        BookDialog.prototype.addBottomButtom = function (){
            var self = this;
            this.addButton(I18N.getString("ES.COMMON.CONFIRM", "确定"),"",false,true,function (){
                if(EUI.isFunction(self.onok)){
                    self.getValue()
                    if (self.check()){//数据正确
                        self.save();//新建图书信息
                        self.close();
                    }
                }
            })
            var cancel =this.addButton(I18N.getString("ES.COMMON.CANCEL", "取消"),"",false,true,function (){
                self.close();

            })
            EUI.removeClassName(cancel,"eui-btn-primary");
            //关闭对话框，清空对话框数据
            this.setOnClose(function (){
                self.clear();
                self.hideErrMsg("nametips");
                self.hideErrMsg("bcomboboxtips");
                self.hideErrMsg("scomboboxtips");
            })
        }

        /**
         * 保存或新建的图书
         */
        BookDialog.prototype.save = function (){
            var self = this
            if(self.isnew){//新建图书
                EUI.post({
                    url: EUI.getContextPath() + "book/addBook.do",
                    data:self.userdata,
                    callback: function (queryObj) {
                        var obj = queryObj.getResponseJSON();
                        self.onok();

                    },
                    waitMessage: {message: I18N.getString("ES.COMMON.SAVEING", "正在保存..."),
                        finish: I18N.getString("ES.COMMON.SUCCEED", "成功")}
                })
            }else{//保存修改
               EUI.post({
                   url:EUI.getContextPath() + "book/saveBook.do",
                   data:self.userdata,
                   callback:function (queryObj){
                       var obj = queryObj.getResponseJSON();
                       self.onok();
                   },
                   waitMessage: {message: I18N.getString("ES.COMMON.SAVEING", "正在保存..."),
                       finish: I18N.getString("ES.COMMON.SAVESUCCESS", "保存成功")}
               })
            }
        }

        /**
         * 点击确定后执行的回调事件
         */
        BookDialog.prototype.setOnok = function (func){
            this.onok = func;
        }
        /**
         * 检查是否有必填项为空
         * @returns {boolean}
         */
        BookDialog.prototype.check = function (){
            var  self = this;
            var flag = true;
            //检查书名
            if (isNull(self.userdata.name)){
               self.showErrMsg("nametips",I18N.getString("borrow.web.js.bookdialog.js.nobook", "书名不能为空"));
                flag= false;
            }else {
                self.hideErrMsg("nametips");
            }
            //检查大类
            if (self.userdata.cid==null){
                self.showErrMsg("bcomboboxtips",I18N.getString("borrow.web.js.bookdialog.js.nobcaption", "大类不能为空"));
                flag=  false;
            } else{
                self.hideErrMsg("bcomboboxtips");
            }
            //检查小类
            if (self.userdata.tid==null){
                self.showErrMsg("scomboboxtips",I18N.getString("borrow.web.js.bookdialog.js.noscaption", "小类不能为空"));
                flag= false;
            }else{
                self.hideErrMsg("scomboboxtips");
            }
            return flag;
        }
        /**
         * 显示错误提示信息
         * @param id 需要显示错误提示信息的dom的ID
         * @param msg 错误提示信息
         */
        BookDialog.prototype.showErrMsg = function (id,msg){
            var self = this;
            var content = $(self.getContent());
            var dom = content.find("#"+id);//找到要显示提示信息的dom
            dom.html(msg);//载入提示信息
            var p = dom.parent()[0];//得到父节点
            EUI.addClassName(p,"eui-show");
            EUI.removeClassName(p,"eui-hide");
        }
        /**
         * 隐藏错误提示信息
         * @param id 需要隐藏错误提示信息的dom的ID
         */
        BookDialog.prototype.hideErrMsg = function (id){
            var self = this;
            var content = $(self.getContent());
            var p = content.find("#"+id).parent()[0];//得到父节点
            if (EUI.hasClassName(p,"eui-show")){
                EUI.addClassName(p,"eui-hide");
                EUI.removeClassName(p,"eui-show");
            }
        }
        /**
         *清除弹框中的数据
         */
        BookDialog.prototype.clear = function (){
            var self = this;
            //清除书名
            EUI.getChildDomByAttrib(self.getBaseDom(),"id","name",true).value="";
            //清除大类
            self.bComboboxObj.setSelectValue("",false);
            cid=null;
            //清除小类
            self.sComboboxObj.setSelectValue("",false);
            tid =null;
            //清除描述
            EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value="";

        }
        /**
         * 得到弹框中的值
         */
        BookDialog.prototype.getValue = function (){
            var self = this;
            self.userdata = {
                id:self.bid,
                name:EUI.getChildDomByAttrib(this.getBaseDom(),"id","name",true).value,
                cid:cid,
                tid:tid,
                desc:EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value,
                isnew:self.isnew,
            }
        }
        /**
         * 打开对话框时，设置显示的信息
         */
        BookDialog.prototype.setValue = function (datas){
            var self = this;
            //设置ID
            self.bid = datas.id;
            //设置书名
            EUI.getChildDomByAttrib(self.getBaseDom(),"id","name",true).value=datas.name;
            //设置大类和小类
            self.bcaption = datas.bcaption;
            self.scaption = datas.scaption;
            //设置描述
            EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value=datas.desc;
            self._setBScaption()
        }
        /**
         * 设置大小类数据
         */
        BookDialog.prototype._setBScaption = function (){
            var self = this;
            self.bComboboxObj.setSelectValue(self.bcaption,false);
            var elist = self.bComboboxObj.getEList();
            if (elist!=null){
                var data = elist.getSelectDatas();
                cid = data[0].id;
                self.refresh(cid);
            }
        }
        /**
         * 判断是否为空
         */
        var isNull = function (str){
                return str==null||str.match(/^\s*$/);
        }

        return{
            BookDialog :BookDialog,
        };
    });