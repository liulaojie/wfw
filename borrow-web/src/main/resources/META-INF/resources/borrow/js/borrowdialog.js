define([ "eui/modules/edialog","eui/modules/eform","eui/modules/ecombobox"],
    function (edialog,eform,ecombobox) {
        var EDialog = edialog.EDialog;
        var eform = eform.eform;
        var EListCombobox = ecombobox.EListCombobox;
        BorrowDialog.prototype.userdata = {};
        BorrowDialog.prototype.cid = null;
        BorrowDialog.prototype.tid = null;
        BorrowDialog.prototype.bid = null;
        BorrowDialog.prototype.scaption = null;
        BorrowDialog.prototype.pageIndexnow = null;//现在所在的页数
        BorrowDialog.prototype.pageIndexnext = null;//将要得到的页数
        /**
         * BorrowDialog的构造函数,继承EDialog
         */
        function BorrowDialog(options){
            EDialog.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            this._init(options);

        }
        EUI.extendClass(BorrowDialog,EDialog,"BorrowDialog");

        /**
         * 销毁BorrowDialog所持有的资源
         */
        BorrowDialog.prototype.dispose = function (){
            var self = this;
            self.bookComboboxObj.dispose();
            self.bookComboboxObj = null;
            self.fromDateObj.dispose();
            self.fromDateObj = null;
            EDialog.prototype.dispose.call(this);
        }
        /**
         * 初始化对话框
         */
        BorrowDialog.prototype._init=function (options){
            var self = this;
            self.scaption = options.scaption;
            self._options = options||{};
            self._options["caption"] = self._options["caption"]||"新建借阅记录";
            self._options["width"] = self._options["width"]||380;
            self._options["height"] = self._options["height"]||400;
            EDialog.call(self,self._options);
            self.setCanResizable(false);
            self.setMaxButtonVisible(false);
            self._initUI();
        }
        /**
         * 初始化UI界面
         */
        BorrowDialog.prototype._initUI = function (){
            var self = this;
            var content = self.getContent();
            var strhtml = [];
            strhtml.push('<div class="eui-layout-container eui-padding-top-10 eui-padding-bottom-10 eui-scroll-auto">');
            strhtml.push('<link  rel="stylesheet" type="text/css" href="../../borrow/css/borrowdialog.css">');
            strhtml.push('  <div id = "borrowdialog-info-content">');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 借阅人：</label>');
            strhtml.push('          <div  class="eui-input-block"><input id="person" type="text" class="eui-form-input"');
            strhtml.push('           placeholder="请填写借阅人姓名！">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="persontips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 书名：</label>');
            strhtml.push('          <div class="eui-input-block" id="bookcombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="bookcomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 借阅时间：</label>');
            strhtml.push('          <div class="eui-input-block" id="fromdate">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error" id="fromdatetips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('  </div>');
            strhtml.push('</div>');
            content.innerHTML = strhtml.join(" ");
            self.addBottomButtom();//添加按钮
            self._initBookList();//初始化书本下拉框
            self._initFromDate();//初始化借阅时间日期搜索框
        }
        /**
         * 初始化书名列表
         */
        BorrowDialog.prototype._initBookList = function (){
            var self = this;
            var content = $(self.getContent());
            this.bookComboboxObj  =new EListCombobox({
                wnd: this.wnd,
                parentElement: content.find("#bookcombobox"),
                width:200,
                height:"100%",
                showFilter:false,
                showCheckAll:false,
                placeholder:"请选择图书",
                data$key: "id",
                caption$key:"name",
                onclickitem:function (rowdata, td, evt, isCheck){//点击事件
                    self.bid = rowdata.id;
                },
                onmoredata:function (list){//当下滑到最低端
                    self.pageIndexnow++;
                    //异步获得图书列表，要判断追加时机
                    if (self.pageIndexnow==self.pageIndexnext){
                        self.getBookList();
                    }else{//回溯
                        self.pageIndexnow--;
                    }

                },
            });
            self.pageIndexnow=0;
            self.pageIndexnext=0;
            self.getBookList();
        }
        /**
         *
         * 初始化借阅时间日期选择框
         */
        BorrowDialog.prototype._initFromDate = function (){
            var self = this;
            this.fromDateObj = eform.date({
                parentElement: EUI.getChildDomByAttrib(this.getBaseDom(),"id","fromdate",true),
                wnd: this.wnd,
                value:EUI.date2String(new Date(), "yyyy年mm月dd日"),
                width:'200',
                onclickitem:function (data, td, evt, isCheck){
                    console.log(data);
                }
            })
        }
        /**
         * 获取书本列表,每次获取的新列表追加到旧列表后
         */
        BorrowDialog.prototype.getBookList = function (){
            var self = this;
            var index = self.pageIndex;
            EUI.post({
                url:EUI.getContextPath()+"web/borrow/bookList.do",
                data:{
                    scaption:self.scaption,
                    pageIndex:self.pageIndexnow,
                },
                callback:function (queryObj){
                    var obj = queryObj.getResponseJSON();
                    if (!!obj){
                        self.bookComboboxObj.addOptions(obj);
                        self.pageIndexnext++;
                    }
                }
            })

        }

        /**
         * 绑定按钮事件
         */
        BorrowDialog.prototype.addBottomButtom = function (){
            var self = this;
            this.addButton("确定","",false,true,function (){
                if(EUI.isFunction(self.onok)){
                    self.getValue()
                    if (self.check()){//数据正确
                        self.save();//新建图书信息
                        self.close();
                    }
                }
            })
            var cancel =this.addButton("取消","",false,true,function (){
                self.clear();
                self.close();

            })
            EUI.removeClassName(cancel,"eui-btn-primary");
            //关闭对话框，清空对话框数据
            this.setOnClose(function (){
                self.clear();
            })

        }

        /**
         * 新建的借阅记录
         */
        BorrowDialog.prototype.save = function (){
            var self = this
            EUI.showWaitDialog(I18N.getString("ES.COMMON.SAVING", "正在保存..."));
            EUI.post({
                url: EUI.getContextPath() + "web/borrow/addBorrow.do",
                data:self.userdata,
                callback: function (queryObj) {
                    var obj = queryObj.getResponseJSON();
                    if(obj){
                        EUI.hideWaitDialogWithComplete(1000, I18N.getString("ES.COMMON.SAVESUCCESS", "添加成功"));
                        self.clear();
                        self.onok();
                    }
                }
            })
        }

        /**
         * 点击确定后执行的回调事件
         */
        BorrowDialog.prototype.setOnok = function (func){
            this.onok = func;
        }
        /**
         * 检查是否有必填项为空
         * @returns {boolean}
         */
        BorrowDialog.prototype.check = function (){
            var  self = this;
            var flag = true;
            //检查借阅人信息
            if (isNull(self.userdata.person)){
               self.showErrMsg("persontips","借阅人不能为空");
                flag= false;
            }else {
                self.hideErrMsg("persontips");
            }
            //检查书本信息
            if (self.userdata.bid==null){
                self.showErrMsg("bookcomboboxtips","书名不能为空");
                flag=  false;
            } else{
                self.hideErrMsg("bookcomboboxtips");
            }
            //检查借书时间信息
            if (self.userdata.fromdate==null){
                self.showErrMsg("fromdatetips","借阅时间不能为空");
                flag= false;
            }else{
                self.hideErrMsg("fromdatetips");
            }
            return flag;
        }
        /**
         * 显示错误提示信息
         */
        BorrowDialog.prototype.showErrMsg = function (id,msg){
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
        BorrowDialog.prototype.hideErrMsg = function (id){
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
        BorrowDialog.prototype.clear = function (){
            var self = this;
            //清除借阅人
            EUI.getChildDomByAttrib(this.getBaseDom(),"id","person",true).value="";
            //清除大类
            self.bookComboboxObj.setCaption("",false);
            self.bid=null;
            //还原日期
            EUI.getChildDomByAttrib(self.fromDateObj,"type","text",true).value=
                EUI.date2String(new Date(), "yyyy年mm月dd日");
            EUI.getChildDomByAttrib(self.fromDateObj,"type","hidden",true).value=
                EUI.date2String(new Date(), "yyyymmdd");
        }
        /**
         * 得到弹框中的值
         * @returns {{name: *, tid: string, cid: string, desc: *}}
         */
        BorrowDialog.prototype.getValue = function (){
            var self = this;
            self.userdata = {
                person:EUI.getChildDomByAttrib(this.getBaseDom(),"id","person",true).value,
                bid:self.bid,
                fromdate:EUI.getChildDomByAttrib(
                    EUI.getChildDomByAttrib(this.getBaseDom(),"id","fromdate",true),"type","hidden",true).value,
            }
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
            BorrowDialog :BorrowDialog,
        };
    });