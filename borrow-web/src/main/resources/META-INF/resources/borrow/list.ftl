<@module.page>
<@module.head title="${title2}">
</@module.head>
<@module.body>
<div class="eui-layout-container">
	Hello world
		<#list borrowlist as borrow>
			id:${borrow.id}
			person:${borrow.person}
			bid:${borrow.bid}
			fromdate:${borrow.fromdate?string("yyyy-MM-dd HH:mm:ss ")}
			todate:${borrow.todate?string("yyyy-MM-dd HH:mm:ss ")}<br>
		</#list>
	</div>

</@module.body>
<script>
</script>
</@module.page>