define([ "eui/modules/edialog","eui/modules/eform","eui/modules/ecombobox"],
    function (edialog,eform,ecombobox) {
        var EDialog = edialog.EDialog;
        var eform = eform.eform;
        var EListCombobox = ecombobox.EListCombobox;
        BookDialog.prototype.userdata = {};
        BookDialog.prototype.cid = null;
        BookDialog.prototype.tid = null;
        BookDialog.prototype.bid = null;
        BookDialog.prototype.isnew = null;
        /**
         * BookDialog的构造函数,继承EDialog
         */
        function BookDialog(options){
            EDialog.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
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
                self._options["caption"] = self._options["caption"]||"新建图书";
            }else{
                self._options["caption"] = self._options["caption"]||"编辑图书";
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
            strhtml.push('<div class="eui-layout-container eui-padding-top-10 eui-padding-bottom-10 eui-scroll-auto">');
            strhtml.push('<link  rel="stylesheet" type="text/css" href="../../borrow/css/bookdialog.css">');
            strhtml.push('  <div id = "bookdialog-info-content">');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 书名：</label>');
            strhtml.push('          <div  class="eui-input-block"><input id="name" type="text" class="eui-form-input" placeholder="请填写书名">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="nametips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 大类：</label>');
            strhtml.push('          <div class="eui-input-block" id="bcombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="bcomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 小类：</label>');
            strhtml.push('          <div class="eui-input-block" id="scombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="scomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label  class="eui-form-label "> 图书简介：</label>');
            strhtml.push('          <div  class="eui-input-block"><textarea id="desc" style="width: 200px" class="eui-form-textarea" ');
            strhtml.push('           placeholder="请填写图书的简介信息没有可以不填，" ></textarea></div>');
            strhtml.push('      </div>');
            strhtml.push('  </div>');
            strhtml.push('</div>');
            content.innerHTML = strhtml.join(" ");
            //绑定输入事件
           //  var dom = $(self.getContent()).find("#name");
           // dom.oninput = function (){
           //     debugger;
           //     self.hideErrMsg("nametips")
           // }
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
                placeholder:"请选择大类",
                data$key: "id",
                onclickitem:function (rowdata, td, evt, isCheck){
                    self.cid = rowdata.id;
                    self.refresh(self.cid);
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
                columns:[{caption: "标题", id:"caption", width: "100%"}],
                placeholder:"请选择小类",
                data$key: "id",
                showFilter:false,
                showCheckAll:false,
                onclickitem:function (rowdata, td, evt, isCheck){
                    self.tid = rowdata.id;
                }
            })
        }
        /**
         * 获取大类列表
         */
        BookDialog.prototype.getCategoryList = function (){
            var self = this;
            EUI.post({
                url:EUI.getContextPath()+"web/borrow/categoryList.do",
                callback:function (queryObj){
                    var obj = queryObj.getResponseJSON();
                    self.bComboboxObj.setDatas(obj);
                }
            })

        }
        /**
         * 获取大类对应的小类列表
         */
        BookDialog.prototype.refresh = function (cid){
            var self = this;
            var datas = new Array();
            EUI.post({
                url: EUI.getContextPath() + "web/borrow/typeList.do",
                data:{cid:cid},
                callback: function (queryObj) {
                    var obj = queryObj.getResponseJSON();
                    self.sComboboxObj.setDatas(obj);

                }
            })
        }
        /**
         * 绑定按钮事件
         */
        BookDialog.prototype.addBottomButtom = function (){
            var self = this;
            this.addButton("确定","",false,true,function (){
                if(EUI.isFunction(self.onok)){
                    self.getValue()
                    if (self.check()){//数据正确
                        self.save();//新建图书信息
                        self.onok;
                    }
                }
            })
            var cancel =this.addButton("取消","",false,true,function (){
                self.clear();

            })
            EUI.removeClassName(cancel,"eui-btn-primary");
            //关闭对话框，清空对话框数据
            this.setOnClose(function (){
                self.defaultCloseEvent();
                self.clear();
            })

        }

        /**
         * 保存或新建的图书
         */
        BookDialog.prototype.save = function (){
            var self = this
            EUI.showWaitDialog(I18N.getString("ES.COMMON.SAVING", "正在保存..."));
            if(self.isnew){//新建图书
                EUI.post({
                    url: EUI.getContextPath() + "web/borrow/addBook.do",
                    data:self.userdata,
                    callback: function (queryObj) {
                        var obj = queryObj.getResponseJSON();
                        if(obj){
                            EUI.hideWaitDialogWithComplete(1000, I18N.getString("ES.COMMON.SAVESUCCESS", "添加成功"));
                            self.clear();
                        }
                        else {
                            EUI.hideWaitDialogWithComplete(0,);
                            var dom = EUI.showMessage("已存在书名为《"+self.userdata.name+"》的书籍", "提示");

                        }
                    }
                })
            }else{//保存修改

               EUI.post({
                   url:EUI.getContextPath() + "web/borrow/saveBook.do",
                   data:self.userdata,
                   callback:function (queryObj){
                       var obj = queryObj.getResponseJSON();
                       if(obj){
                           EUI.hideWaitDialogWithComplete(1000, I18N.getString("ES.COMMON.SAVESUCCESS", "修改成功"));
                           self.clear();
                       }
                       else {
                           EUI.hideWaitDialogWithComplete(0,);
                           var dom = EUI.showMessage("修改失败", "提示");

                       }
                   }
               })
            }

        }
        /**
         * 对话框默认的关闭事件
         */
        BookDialog.prototype.defaultCloseEvent = function (){

        }
        /**
         * 点击确定后执行的回调事件
         */
        BookDialog.prototype.setOnok = function (func){
            this.onok = func;
        }
        BookDialog.prototype.check = function (){
            var  self = this;
            var flag = true;
            //检查书名
            if (isNull(self.userdata.name)){
               self.showErrMsg("nametips","书名不能为空");
                flag= false;
            }else {
                self.hideErrMsg("nametips");
            }
            //检查大类
            if (self.userdata.cid==null){
                self.showErrMsg("bcomboboxtips","大类不能为空");
                flag=  false;
            } else{
                self.hideErrMsg("bcomboboxtips");
            }
            //检查小类
            if (self.userdata.tid==null){
                self.showErrMsg("scomboboxtips","大类不能为空");
                flag= false;
            }else{
                self.hideErrMsg("scomboboxtips");
            }
            return flag;
        }
        /**
         * 显示错误提示信息
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
         * 点确定时，获得对话框中的数据
         */
        /**
         *清除弹框中的数据
         */
        BookDialog.prototype.clear = function (){
            var self = this;
            //清除书名
            EUI.getChildDomByAttrib(self.getBaseDom(),"id","name",true).value="";
            //清除大类
            self.bComboboxObj.setCaption("",false);
            self.cid=null;
            //清除小类
            self.sComboboxObj.setCaption("",false);
            self.tid =null;
            //清除描述
            EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value="";

        }
        /**
         * 得到弹框中的值
         * @returns {{name: *, tid: string, cid: string, desc: *}}
         */
        BookDialog.prototype.getValue = function (){
            var self = this;
            self.userdata = {
                id:self.bid,
                name:EUI.getChildDomByAttrib(this.getBaseDom(),"id","name",true).value,
                cid:self.cid,
                tid:self.tid,
                desc:EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value,
                isnew:self.isnew,
                 // EUI.getChildDomByAttrib(EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true),"id","desc",true).value
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
            //设置大类
            self.bComboboxObj.setCaption(datas.bcaption,false);
            //设置小类
            self.sComboboxObj.setCaption(datas.scaption,false);
            //设置描述
            EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value=datas.desc;
            self.cid=0;
            self.tid=0;
        }

        /**
         * 判断是否为空
         */
        var isNull = function (str){
            if (str==null||str.match(/^\s*$/)){
                return true;
            }else{
                return false;
            }
        }

        return{
            BookDialog :BookDialog,
        };
    });