<@module.page>
<@module.head title="${title2}">
</@module.head>
<@module.body>
<div class="eui-layout-container">
	<div id="toolbardom" class="eui-layout-row-1 eui-layout-row-first "></div>
	<div id="listdom" class="eui-layout-row-3 eui-layout-row-offset-1 eui-layout-row-offsetbottom-1 eui-padding-left-20 eui-padding-right-20"></div>
	<div id="pagebardom" class="eui-layout-row-1 eui-layout-row-last eui-padding-top-10 eui-padding-left-10 eui-padding-right-10 eui-margin-bottom-5 eui-align-center"></div>
	</div>

</@module.body>
	<script type="text/javascript">
		var scaption = '${scaption}';
		require(["borrow/js/borrowmgr"],function (borrowmgr){
			var borrowmgr = new borrowmgr.BorrowMgr({
				wnd:window,
				scaption:scaption
			});
			EUI.addDispose(borrowmgr,window);
		});
	</script>
</@module.page>