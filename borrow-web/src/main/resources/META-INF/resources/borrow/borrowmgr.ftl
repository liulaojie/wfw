<@module.page>
<@module.head title="${title2}">
</@module.head>
<@module.body>
<div class="eui-layout-container">
	<div id="toolbardom" class="eui-layout-row-1 eui-layout-row-first "></div>
	<div id="listdom" class="eui-layout-row-3 eui-layout-row-offset-1 eui-layout-row-offsetbottom-1 eui-padding-left-20 eui-padding-right-20"></div>
	<div id="pagebardom" class="eui-layout-row-1 eui-layout-row-last eui-padding-left-5n eui-padding-right-5n eui-margin-bottom-5n eui-align-center"></div>
	</div>

</@module.body>
	<script type="text/javascript">
		var tid = '${tid}';
		require(["borrow/js/borrowmgr"],function (borrowmgr){
			var borrowmgr = new borrowmgr.BorrowMgr({
				wnd:window,
				tid:tid
			});
			EUI.addDispose(borrowmgr,window);
		});
	</script>
</@module.page>