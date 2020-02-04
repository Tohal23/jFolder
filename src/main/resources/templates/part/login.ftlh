<#macro login path isRegisterForm>
<div class="container h-100">
    <div class="row mt-5">
        <div class="container col align-self-center border p-3">
        <form class="mx-auto vertical-align" action="${path}" method="post">
            <div class="form-group">
                <label for="inputAddress">Login</label>
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       id="inputAddress" placeholder="Login..."/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <label for="exampleInputPassword1">Password</label>
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}" id="exampleInputPassword1" placeholder="Password">
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
            <#if isRegisterForm>
            <div class="form-group">
                    <label for="exampleInputPassword1">Password</label>
                    <input type="password" name="password2"
                           class="form-control ${(password2Error??)?string('is-invalid', '')}" id="exampleInputPassword2" placeholder="Retype password">
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
            </div>
            <div class="form-group">
                <label for="exampleInputEmail1">Email address</label>
                <input type="email" class="form-control ${(emailError??)?string('is-invalid', '')}" name="email"
                       id="exampleInputEmail1" aria-describedby="emailHelp"
                       placeholder="Enter email" value="<#if user??>${user.email}</#if>">
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
                <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
            </div>
            </#if>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">
                    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
                </label>
                <div class="col-sm-2">
                <#if !isRegisterForm><label class="col-form-label"><a class="btn btn-primary" href="/registration">Add new user</a></label></#if>
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