<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as loginForm>

<@common.page>
Register new user
${message}
<@loginForm.login "/registration" />
</@common.page>