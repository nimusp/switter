<#import "parts/common.ftl" as common>

<@common.page>
List of users

<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <table cellpadding="5">
        <col width="150" valign="top">
        <col width="150" valign="top">
        <col width="50" valign="top">
        <tr>
            <td>${user.username}</td>
            <td><#list user.roles as role>${role}<#sep>, </#list></td>
            <td><a href="/user/${user.id}">edit</a> </td>
        </tr>
    </table>
    </#list>
    </tbody>
</table>
</@common.page>