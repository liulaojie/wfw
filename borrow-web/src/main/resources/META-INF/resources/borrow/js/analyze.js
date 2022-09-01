define(["eui/modules/etree","eui/modules/uibase","eui/modules/epanelsplitter", "eui/modules/etabctrl","eui/modules/edialog"],
    function (etree, uibase,epanelsplitter,etabctrl,edialog) {
        "use strict";

        var EComponent = uibase.EComponent;
        var ETree = etree.ETree;
        var EPanelSplitter = epanelsplitter.EPanelSplitter;
        var ETabCtrl = etabctrl.ETabCtrl;
        var EDialog = edialog.EDialog;
        /**
         * 主页构造函数
         */
        function Analyze(options){
            EComponent.call(this,options);
            // var options = options||{};
            // this.wnd = options["wnd"]||window;
            // this.doc = this.wnd.document;
            this._initUI();
        }
        EUI.extendClass(Analyze,EComponent,"Analyze");

        /**
         * 初始化UI界面
         */
        Analyze.prototype._initUI = function () {
            var self = this;
            var pwnd = self.wnd.parent;
            var datas = pwnd.getTabData();
            var type = datas.type;
            datas = datas.data;
            if (type==1){//生成柱状图
                self._histogram(datas);
            }else {//生成条形图
                self._barTable(datas);
            }
        }
        /**
         * 生成条形图
         * @param datas为用户选择的数据
         * @private
         */
        Analyze.prototype._barTable = function (datas){
            var self = this;
            var doc = self.doc
            var dom = doc.getElementById("body");
            var map = self._transform(datas);
            var red = 75
            var green = 150
            var blue = 225
            var top = 20
            var unit = 80/self.max;
            map.forEach(function (value, key, map) {
                var div = doc.createElement("div")
                EUI.addClassName(div,"baritem");
                div.style.top = top+"px";
                top+=22+20;//22为一个数据项的高度
                //书名标题
                var labdiv = doc.createElement("div");
                labdiv.style.width="10%"
                EUI.addClassName(labdiv,"barlabdiv")
                var label = doc.createElement("span");
                EUI.addClassName(label,"eui-align-right eui-textcolor barmylable");
                var head = doc.createTextNode(key);
                label.appendChild(head);
                labdiv.appendChild(label)
                //条形图
                var graph = doc.createElement("div");
                graph.style.width = unit*value+"%";
                red = red%255;
                green = green%255;
                blue = blue%255;
                graph.style.backgroundColor = "#"+red.toString(16)+green.toString(16)+blue.toString(16);
                red+=16
                green+=32
                blue+=64
                EUI.addClassName(graph,"bargraph")
                //数量
                var numdom = doc.createElement("span");
                numdom.innerText = value;
                //装入界面
                div.appendChild(labdiv);
                div.appendChild(graph);
                div.appendChild(numdom)
                dom.appendChild(div);
            })
        }
        /**
         * 生成柱状图
         * @param datas为用户选择的数据
         * @private
         */
        Analyze.prototype._histogram = function (datas){
            var self = this;
            var doc = self.doc
            var dom = doc.getElementById("body");
            var map = self._transform(datas);
            var red = 75
            var green = 150
            var blue = 225
            var left =20
            var unit = 80/self.max;
            map.forEach(function (value, key, map) {
                var div = doc.createElement("div");
                EUI.addClassName(div,"histitem");
                div.style.left = left+"px";
                left+=100+20//100为数据项宽度
                //书名标题
                var labdiv = doc.createElement("div");
                EUI.addClassName(labdiv,"histlabdiv")
                var label = doc.createElement("span");
                EUI.addClassName(label,"eui-align-center eui-textcolor histmylable");
                var head = doc.createTextNode(key);
                label.appendChild(head);
                labdiv.appendChild(label)
                var w = head.offsetWidth;
                //条形图
                var graph = doc.createElement("div");
                graph.style.height = unit*value+"%";
                red = red%255;
                green = green%255;
                blue = blue%255;
                graph.style.backgroundColor = "#"+red.toString(16)+green.toString(16)+blue.toString(16);
                red+=16
                green+=32
                blue+=64
                EUI.addClassName(graph,"histgraph")
                //数量
                var numdiv = doc.createElement("div");

                EUI.addClassName(numdiv,"histnumdiv")
                numdiv.style.bottom =10+unit*value+5+"%"
                var numdom = doc.createElement("span");
                numdom.innerText = value;
                numdiv.appendChild(numdom);
                //装入界面
                graph.appendChild(labdiv);
                div.appendChild(graph);
                div.appendChild(numdiv);
                dom.appendChild(div);
            })
        }
        /**
         * 将数组转换为map集合
         * @param datas
         * @private
         */
        Analyze.prototype._transform = function (datas){
            var self = this;
            self.max = 1;
            var map = new Map();
            for (var i=0;i<datas.length;i++){
                var data = datas[i];
                if (map.has(data.book)){
                    var num = map.get(data.book);
                    map.set(data.book,num+1);
                    if (map.get(data.book)>self.max){
                        self.max = num+1;
                    }
                }else{
                    map.set(data.book,1);
                }
            }
            return map;
        }
        /**
         * 销毁所持有的资源
         */
        Analyze.prototype.dispose = function (){
            EComponent.prototype.dispose.call(this);
        }

        return{
            Analyze :Analyze
        };
    });