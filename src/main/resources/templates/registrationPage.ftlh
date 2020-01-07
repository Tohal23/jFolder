<#import "part/common.ftlh" as c>
<#import "part/login.ftlh" as l>

<@c.page>
<div class="mb-1">Add new user</div>
${message?ifExists}
<@l.login "/registration" true />
</@c.page>