define([ "eui/modules/edialog","eui/modules/eform","eui/modules/ecombobox"],
    function (edialog,eform,ecombobox) {
        var EDialog = edialog.EDialog;
        var eform = eform.eform;
        var EListCombobox = ecombobox.EListCombobox;
        var cid = "";
        var tid = "";
        /*-------------------------------------------------BookDialog--------------------------------------------*/
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
            self._options["caption"] = self._options["caption"]||"新建图书";//弹窗标题
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
            strhtml.push('  <div id = "bookdialog-info-content">');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 书名：</label>');
            strhtml.push('          <div  class="eui-input-block"><input id="name" type="text" class="eui-form-input" placeholder="请填写书名"></div>');
            strhtml.push('          </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 大类：</label>');
            strhtml.push('          <div class="eui-input-block" id="bcombobox">');
            strhtml.push('          </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 小类：</label>');
            strhtml.push('          <div class="eui-input-block" id="scombobox">');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label  class="eui-form-label "> 图书简介：</label>');
            strhtml.push('          <div  class="eui-input-block"><textarea id="desc" style="width: 200px" class="eui-form-textarea" ');
            strhtml.push('           placeholder="请填写图书的简介信息没有可以不填，" ></textarea></div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('  </div>');
            strhtml.push('</div>');
            content.innerHTML = strhtml.join(" ");
            self.addBottomButtom();
            self._initBList();
            self._initSList();
        }
        /**
         * 初始化大类列表
         */
        BookDialog.prototype._initBList = function (){
            var self = this;
            this.bComboboxObj  =new EListCombobox({
                parentElement:EUI.getChildDomByAttrib(this.getBaseDom(),"id","bcombobox",true),
                width:200,
                height:"100%",
                columns:[{caption:"请选择大类",id:"bcaption",width:"100%"}],
                showFilter:false,
                showCheckAll:false,
                placeholder:"请选择大类",
                data$key: "id",
                onclickitem:function (rowdata, td, evt, isCheck){
                    cid = rowdata.id;
                    self.refresh(cid);
                }
            })
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
                    tid = rowdata.id;
                }
            })
        }
        /**
         * 获取大类列表
         */
        BookDialog.prototype.getCategoryList = function (){
            var self = this;
            var data = new Array();
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
         * 绑定事件
         */
        BookDialog.prototype.addBottomButtom = function (){
            var self = this;
            this.addButton("确定","",false,true,function (){
                if(EUI.isFunction(self.onok)){
                    self.save();
                    self.onok;
                }
            })
            this.addButton("取消","",false,true,function (){
                var content = self.getContent();

            })
            //关闭对话框，清空对话框数据
            this.setOnClose(function (){
                self.defaultCloseEvent();
            })
        }

        /**
         * 保存新建的图书
         */
        BookDialog.prototype.save = function (){
            var self = this
            EUI.post({
                url: EUI.getContextPath() + "web/borrow/addBook.do",
                data:self.getValue(),
                callback: function (queryObj) {
                    var obj = queryObj.getResponseJSON();
                    debugger;
                    alert(obj);
                }
            })
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
        /**
         * 点确定时，获得对话框中的数据
         */
        BookDialog.prototype.getValue = function (){
            var self = this;

            var userdata = {
                name:EUI.getChildDomByAttrib(this.getBaseDom(),"id","name",true).value,
                cid:cid,
                tid:tid,
                desc:EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true).value,
                 // EUI.getChildDomByAttrib(EUI.getChildDomByAttrib(this.getBaseDom(),"id","desc",true),"id","desc",true).value
            }
            return userdata;
        }
        /**
         * 打开对话框时，设置显示的信息
         */
        BookDialog.prototype.setValue = function (value){}

        return{
            BookDialog :BookDialog,
        };
    });