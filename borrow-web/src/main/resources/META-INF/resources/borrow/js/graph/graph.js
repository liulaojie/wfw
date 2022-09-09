define(["eui/modules/uibase"],
    function (uibase){
        "use strict";

        var EComponent = uibase.EComponent;

        /**
         * 分析图基类构造函数
         */
        function Graph(options){
            EComponent.call(this,options);
        }
        EUI.extendClass(Graph,EComponent,"Graph");
        /**
         * 初始化
         * @param datas
         * @private
         */
        Graph.prototype._init = function (datas){
            var self = this;
            self.datas=self._initData(datas);
            self._getMax();
            self._initUI();
        }
        /**
         * 初始化数据，将传来的数据转成书籍名为key，数量为value的map
         * @param datas 需要转换的数据（用户选择的数据）
         * @returns {Map<String, int>}
         */
        Graph.prototype._initData = function (datas){
            var result = new Map();
            datas.forEach(function (value) {
                var key = value.book;
                if (result.has(key)){
                    var num = result.get(key);
                    result.set(key,num+1);
                }else{
                    result.set(key,1);
                }
            })
            return  result;
        }

        /**
         * 初始化数据
         * @param datas
         * @private
         */
        Graph.prototype._getMax = function (){
            var self = this;
            self.max =1;
            self.datas.forEach(function (value) {
                if (value>self.max){
                    self.max = value;
                }
            })
        }
        /**
         * 获取颜色
         * @returns {string}
         * @private
         */
        Graph.prototype._getColor=function (){
            var self = this;
            self.red+=16
            self.green+=32
            self.blue+=64
            self.red = self.red%255;
            self.green = self.green%255;
            self.blue = self.blue%255;
            return self.red.toString(16)+self.green.toString(16)+self.blue.toString(16);
        }

        /**
         * 销毁所持有的资源
         */
        Graph.prototype.dispose = function (){
            EComponent.prototype.dispose.call(this);
        }

        return{
            Graph :Graph
        };
    })