define([ "eui/modules/edialog","eui/modules/eform","eui/modules/ecombobox"],
    function (edialog,eform,ecombobox) {
        var EDialog = edialog.EDialog;
        var eform = eform.eform;
        var EListCombobox = ecombobox.EListCombobox;
        AnalyzeDialog.prototype.id = null;
        AnalyzeDialog.prototype.data = null;
        /**
         * AnalyzeDialog的构造函数,继承EDialog
         */
        function AnalyzeDialog(options){
            EDialog.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            this._init(options);

        }
        EUI.extendClass(AnalyzeDialog,EDialog,"AnalyzeDialog");

        /**
         * 销毁AnalyzeDialog所持有的资源
         */
        AnalyzeDialog.prototype.dispose = function (){
            var self = this;
            self.graphComboboxObj.dispose();
            self.graphComboboxObj = null;
            EDialog.prototype.dispose.call(this);
        }
        /**
         * 初始化对话框
         */
        AnalyzeDialog.prototype._init=function (options){
            var self = this;
            self._options = options||{};
            self._options["caption"] = self._options["caption"]||"生成分析表";
            self._options["width"] = self._options["width"]||380;
            self._options["height"] = self._options["height"]||200;
            EDialog.call(self,self._options);
            self.setCanResizable(false);
            self.setMaxButtonVisible(false);
            self._initUI();
        }
        /**
         * 初始化UI界面
         */
        AnalyzeDialog.prototype._initUI = function (){
            var self = this;
            var content = self.getContent();
            var strhtml = [];
            strhtml.push('<div class="eui-layout-container eui-padding-top-10 eui-padding-bottom-10 eui-scroll-auto">');
            strhtml.push('<link  rel="stylesheet" type="text/css" href="../../borrow/css/analyzedialog.css">');
            strhtml.push('  <div id = "AnalyzeDialog-info-content">');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 分析表名称：</label>');
            strhtml.push('          <div  class="eui-input-block"><input id="name" type="text" class="eui-form-input"');
            strhtml.push('           placeholder="请填写分析表名称不能时纯数字！">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error tips" id="nametips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> 统计图类型：</label>');
            strhtml.push('          <div class="eui-input-block" id="graphcombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error tips" id="graphcomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('  </div>');
            strhtml.push('</div>');
            content.innerHTML = strhtml.join(" ");
            self.addBottomButtom();//添加按钮
            self._initGraphList();//初始化书名列表
        }
        /**
         * 初始化统计图列表
         */
        AnalyzeDialog.prototype._initGraphList = function (){
            var self = this;
            var content = $(self.getContent());
            this.graphComboboxObj  =new EListCombobox({
                wnd: this.wnd,
                parentElement: content.find("#graphcombobox"),
                width:200,
                height:"100%",
                datas:[
                    {caption: "条形图", value: "0"},
                    {caption: "柱状图", value: "1"},
                    ],
                showFilter:false,
                showCheckAll:false,
                placeholder:"请选择统计图类型！",
                onclickitem:function (rowdata, td, evt, isCheck){//点击事件
                    self.id = rowdata.value;
                },
            });
        }
        /**
         * 绑定按钮事件
         */
        AnalyzeDialog.prototype.addBottomButtom = function (){
            var self = this;
            this.addButton("确定","",false,true,function (){
                if(EUI.isFunction(self.onok)){
                    self.getValue()
                    if (self.check()){//数据正确
                        alert("生成"+self.data.name+"的分析表，统计图类型为"+self.data.id);
                        self.onok(self.data.name,self.data.id);
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
         * 点击确定后执行的回调事件
         */
        AnalyzeDialog.prototype.setOnok = function (func){
            this.onok = func;
        }
        /**
         * 检查是否有必填项为空
         * @returns {boolean}
         */
        AnalyzeDialog.prototype.check = function (){
            var  self = this;
            var flag = true;
            //检查借阅人信息
            if (isNull(self.data.name)){
                self.showErrMsg("nametips","分析表名称不能为空");
                flag = false;
            }else {
                if (isNum(self.data.name)){
                    self.showErrMsg("nametips","分析表名称不能全为数字");
                    flag = false;
                }else{
                    self.hideErrMsg("nametips");
                }
            }
            //检查统计图类型
            if (self.data.id==null){
                self.showErrMsg("graphcomboboxtips","统计图类型不能为空");
                flag=  false;
            } else{
                self.hideErrMsg("graphcomboboxtips");
            }
            return  flag;
        }
        /**
         * 显示错误提示信息
         */
        AnalyzeDialog.prototype.showErrMsg = function (id,msg){
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
        AnalyzeDialog.prototype.hideErrMsg = function (id){
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
        AnalyzeDialog.prototype.clear = function (){
            var self = this;
            //清除借阅人
            EUI.getChildDomByAttrib(this.getBaseDom(),"id","name",true).value="";
            //清除大类
            self.graphComboboxObj.setCaption("",false);
            self.bid=null;
        }
        /**
         * 得到弹框中的值
         * @returns {{name: *, tid: string, cid: string, desc: *}}
         */
        AnalyzeDialog.prototype.getValue = function (){
            var self = this;
            self.data = {
                name:EUI.getChildDomByAttrib(this.getBaseDom(),"id","name",true).value,
                id:self.id,
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
        /**
         * 判断是否为数字
         */
        var isNum = function (str){
            if (str.match(/^\d+$/)){
                return true;
            }else{
                return false;
            }
        }

        return{
            AnalyzeDialog :AnalyzeDialog,
        };
    });