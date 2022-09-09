define(["borrow/js/graph/graph"],
    function (graph){
        "use strict";

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
         * 初始化界面
         * @private
         */
        Analyze.prototype._initUI = function (){
            var self = this;
            //颜色基数
            self.red = 75;
            self.green = 150;
            self.blue = 225;

            var top = 20;//每个图形的偏移
            var self = this;
            var doc = self.doc;
            var dom = doc.getElementById("body");
            var unit = 80/self.max;
            dom.innerHTML = '<link  rel="stylesheet" type="text/css" href="'+EUI.getContextPath()+'borrow/css/analyze.css">'
            EUI.addClassName(dom,"barchar");
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
         * 销毁所持有的资源
         */
        Analyze.prototype.dispose = function (){
            Graph.prototype.dispose.call(this);
        }

        return{
            Analyze :Analyze
        };
    })