define(["borrow/web/js/graph/graph"],
    function (graph){
        "use strict";

        //颜色基数
        var red = 75
        var green = 150
        var blue = 225

        var top = 20//每个图形的偏移

        var Graph = graph.Graph;
        /**
         * 构造函数——条形图
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
            var unit = 80/self.max;
            dom.innerHTML = '<link  rel="stylesheet" type="text/css" href="'+EUI.getContextPath()+'borrow/web/css/analyze.css">'
            self.datas.forEach(function (value,key){
                //整体div
                var div= doc.createElement("div");
                EUI.addClassName(div,"baritem");
                div.style.top = top+"px";
                var strhtml = [];
                //书籍标题
                strhtml.push('<div class="barlabdiv" style="width:10% ">');
                strhtml.push('  <label class=" eui-align-right eui-textcolor barmylable" >'+key);
                strhtml.push('  </label>');
                strhtml.push('</div>');
                //条形图
                strhtml.push('<div class="bargraph"');
                strhtml.push('style="width:'+unit*value+'% ;background-color:#'+self._getColor()+' ;">');
                strhtml.push('</div>');
                //数据
                strhtml.push('<span>'+value+'</span>');
                div.innerHTML = strhtml.join(" ");
                dom.appendChild(div);
                //更新偏移
                top+=22+20;//22为一个数据项的高度
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