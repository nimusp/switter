<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as loginForm>

<@common.page>
${message?ifExists}
<@loginForm.login "/login" false/>
</@common.page>