<@module.page>
<@module.head title="${title2}">
</@module.head>
<@module.body>
<div class="eui-layout-container">
	Hello world<br>
		<#list booklist as book>
			id:${book.id}
			name:${book.name}
			desc:${book.desc}
			scaption:${book.scaption}
			bcaption:${book.bcaption}
		</#list>
	</div>

</@module.body>
<script>
</script>
</@module.page>