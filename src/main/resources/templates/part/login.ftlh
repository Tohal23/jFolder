<#macro login path isRegisterForm>
<div class="container h-100">
    <div class="row">
        <div class="container col align-self-center border">
        <form class="mx-auto vertical-align" action="${path}" method="post">
            <div class="form-group">
                <label for="inputAddress">Login</label>
                <input type="text" name="username" class="form-control" id="inputAddress" placeholder="Login...">
            </div>
            <div class="form-group">
                <label for="exampleInputPassword1">Password</label>
                <input type="password" name="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
            </div>
            <#if isRegisterForm>
            <div class="form-group">
                <label for="exampleInputEmail1">Email address</label>
                <input type="email" class="form-control" name="email" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
                <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
            </div>
            </#if>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">
                    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
                </label>
                <div class="col-sm-4">
                <#if !isRegisterForm><label class="col-form-label"><a href="/registration">Add new user</a></label></#if>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>
    </div>
</div>
</#macro>

<#macro logoutSubmit>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Sign Out</button>
</form>
</#macro>

<#macro loginSubmit>
<form action="/login" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Sign In</button>
</form>
</#macro>