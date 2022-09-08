define([ "eui/modules/edialog","eui/modules/ecombobox"],
    function (edialog,ecombobox) {
        var EDialog = edialog.EDialog;
        var EListCombobox = ecombobox.EListCombobox;
        /**
         * AnalyzeDialog的构造函数
         */
        function AnalyzeDialog(options){
            EDialog.call(this,options);
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
            self._options["caption"] = self._options["caption"]||
                I18N.getString("borrow.js.analyzedialog.js.createanalyze", "生成分析表");
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
            strhtml.push('<link  rel="stylesheet" type="text/css" href="'+EUI.getContextPath()+'borrow/css/analyzedialog.css">');
            strhtml.push('  <div id = "AnalyzeDialog-info-content">');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> '+I18N.getString("borrow.js.analyzedialog.js.analyzename", "分析表名称")+'：</label>');
            strhtml.push('          <div  class="eui-input-block"><input id="name" type="text" class="eui-form-input"');
            strhtml.push('           placeholder="'+I18N.getString("borrow.js.analyzedialog.js.plaanalyzename", "请填写分析表名称不能是纯数字！")+'">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error tips" id="nametips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('      <div class="eui-form-item">');
            strhtml.push('          <label class="eui-form-label eui-form-required"> '+I18N.getString("borrow.js.analyzedialog.js.analyzetype", "统计图类型")+'：</label>');
            strhtml.push('          <div class="eui-input-block" id="graphcombobox">');
            strhtml.push('              <div class="eui-form-mid eui-input-block eui-clear eui-hide" >');
            strhtml.push('                  <div class="eui-tips-container eui-error tips" id="graphcomboboxtips" ></div>');
            strhtml.push('              </div>');
            strhtml.push('          </div>');
            strhtml.push('      </div>');
            strhtml.push('  </div>');
            strhtml.push('</div>');
            content.innerHTML = strhtml.join(" ");
            self.addBottomButton();//添加按钮
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
                    {caption: I18N.getString("borrow.js.analyzedialog.js.barchart", "条形图"), value: "barchart"},
                    {caption: I18N.getString("borrow.js.analyzedialog.js.histogram", "柱状图"), value: "histogram"},
                    ],
                showFilter:false,
                showCheckAll:false,
                placeholder:I18N.getString("borrow.js.analyzedialog.js.chooseanalyzetype", "请选择统计图类型！"),
                onclickitem:function (rowdata, td, evt, isCheck){//点击事件
                    self.id = rowdata.value;
                },
            });
        }
        /**
         * 添加按钮并绑定对应按钮的点击事件
         */
        AnalyzeDialog.prototype.addBottomButton = function (){
            var self = this;
            this.addButton(I18N.getString("ES.COMMON.CONFIRM", "确定"),"",false,true,function (){
                if(EUI.isFunction(self.onok)){
                    self.getValue()
                    if (self.check()){//数据正确
                        var flag= self.onok(self.data.name,self.data.id);
                        if (!flag){
                            EUI.showMessage(I18N.getString("borrow.js.analyzedialog.js.exitanalyze", "已存在表名为《{0}》的分析表",[self.data.name])
                                , I18N.getString("ES.COMMON.PROMPT", "提示"));
                        }
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
                self.hideErrMsg("graphcomboboxtips");
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
                self.showErrMsg("nametips",I18N.getString("borrow.js.analyzedialog.js.noanalyzename", "分析表名称不能为空"));
                flag = false;
            }else {
                if (isNum(self.data.name)){
                    self.showErrMsg("nametips",I18N.getString("borrow.js.analyzedialog.js.allnumber", "分析表名称不能全为数字"));
                    flag = false;
                }else{
                    self.hideErrMsg("nametips");
                }
            }
            //检查统计图类型
            if (self.data.id==null){
                self.showErrMsg("graphcomboboxtips",I18N.getString("borrow.js.analyzedialog.js.notype", "统计图类型不能为空"));
                flag=  false;
            } else{
                self.hideErrMsg("graphcomboboxtips");
            }
            return  flag;
        }
        /**
         * 显示错误提示信息
         * @param id 需要显示错误提示信息的dom的ID
         * @param msg 错误提示信息
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
         * @param id 需要隐藏错误提示信息的dom的ID
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