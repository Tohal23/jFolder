<#import "part/common.ftlh" as c>
<#import "part/login.ftlh" as l>

<@c.page>
${message?ifExists}
<@l.login "/registration" true />
</@c.page>