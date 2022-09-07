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
         * 销毁所持有的资源
         */
        Graph.prototype.dispose = function (){
            EComponent.prototype.dispose.call(this);
        }

        return{
            Graph :Graph
        };
    })