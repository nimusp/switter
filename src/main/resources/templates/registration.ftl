<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as loginForm>

<@common.page>
<div class="mb-1">Add new user</div>
${message?ifExists}
<@loginForm.login "/registration" true/>
</@common.page>