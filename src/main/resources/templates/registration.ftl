<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as loginForm>

<@common.page>
Register new user
${message?ifExists}
<@loginForm.login "/registration" />
</@common.page>