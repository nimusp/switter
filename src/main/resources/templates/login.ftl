<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as loginForm>

<@common.page>
Login page
<@loginForm.login "/login" />
<a href="/registration">Add new user</a>
</@common.page>