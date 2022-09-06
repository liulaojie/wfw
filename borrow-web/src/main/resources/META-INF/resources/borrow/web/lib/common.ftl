<#macro cfb  >
    <#list 1..5 as j>
        <#list 1..j as i>
            ${j}*${i}=${i*j}
        </#list>
        <br>
    </#list>
</#macro>
<@cfb ></@cfb>