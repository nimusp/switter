<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as loginForm>

<@common.page>
<div class="mb-1">Add new user</div>
${userError?ifExists}
<@loginForm.login "/registration" true/>
</@common.page>