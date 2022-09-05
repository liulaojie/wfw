define(["borrow/js/graph/graph"],
    function (graph){
        "use strict";

        //颜色基数
        var red = 75
        var green = 150
        var blue = 225
        var left = 20 //每个图形的偏移

        var Graph = graph.Graph;
        /**
         * 条形图构造函数
         */
        function Analyze(options){
            var self = this;
            Graph.call(this,options);
            self._init(options.datas);
        }

        EUI.extendClass(Analyze,Graph,"Analyze");


        /**
         * 初始化
         * @param datas
         * @private
         */
        Analyze.prototype._init = function (datas){
            var self = this;
            self._initData(datas);
            self._initUI();
        }
        /**
         * 初始化数据
         * @param datas
         * @private
         */
        Analyze.prototype._initData = function (datas){
            var self = this;
            self.datas= Graph.prototype._initData.call(this,datas);
            self.max =1;
            self.datas.forEach(function (value) {
                if (value>self.max){
                    self.max = value;
                }
            })
        }
        /**
         * 初始化界面
         * @private
         */
        Analyze.prototype._initUI = function (){
            var self = this;
            var doc = self.doc;
            var dom = doc.getElementById("body");
            dom.innerHTML = '<link  rel="stylesheet" type="text/css" href="'+EUI.getContextPath()+'/borrow/css/analyze.css">'
            var unit = 80/self.max;
            self.datas.forEach(function (value,key) {
                var height = unit*value;
                //整体div
                var div= doc.createElement("div");
                EUI.addClassName(div,"histitem");
                div.style.left = left+"px";
                var strhtml = [];
                //数据
                var nheight = 10+height+5;
                strhtml.push('<div class="histnumdiv" style="bottom: '+nheight+'%" >');
                strhtml.push('  <span>'+value+'</span>');
                strhtml.push('</div>');
                //柱状图
                strhtml.push('<div class="histgraph"');
                strhtml.push('style="height:'+height+'% ;background-color:#'+self._getColor()+' ;">');
                //书籍标题
                strhtml.push('  <div class="histlabdiv" >');
                strhtml.push('      <label class=" eui-align-center eui-textcolor histmylable" >'+key);
                strhtml.push('      </label>');
                strhtml.push('  </div>');
                strhtml.push('</div>');
                div.innerHTML = strhtml.join(" ");
                dom.appendChild(div);
                //更新偏移
                left+=100+20//100为数据项宽度
            })
        }
        /**
         * 获取颜色
         * @returns {string}
         * @private
         */
        Analyze.prototype._getColor=function (){
            red+=16
            green+=32
            blue+=64
            red = red%255;
            green = green%255;
            blue = blue%255;
            return red.toString(16)+green.toString(16)+blue.toString(16);
        }

        /**
         * 销毁所持有的资源
         */
        Analyze.prototype.dispose = function (){
            Graph.prototype.dispose.call(this);
        }

        return{
            Analyze :Analyze
        };
    })