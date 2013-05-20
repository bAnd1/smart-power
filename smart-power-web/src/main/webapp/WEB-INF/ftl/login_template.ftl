<#macro login error="">
    <#import "template/master.ftl" as template />
    <#import "/spring.ftl" as spring />
    <@template.master title="Login">
    <div id="login">
        <p>Login</p>

        <form name='loginForm' action='/j_spring_security_check' method='POST'>
            <ul>
                <li>
                    <input type='text' id="username" name='username' placeholder="username" autofocus required>
                </li>
                <li>
                    <input type='password' id="password" placeholder="password" name='password'/>
                </li>
                <li>
                    <input name='submit' onclick="" type="submit" class="stretch_submit" value="Login"/>
                </li>
            </ul>
        </form>
    </div>

    <div id="toggle_signup">or sign up here</div>

    <div id="signup">
        <p>Signup</p>

        <form name='loginForm' action='/users' method='POST'>
            <ul>
                <li>
                    <@spring.formInput path="user.username" attributes="placeholder='username'" />
                <@spring.showErrors separator="" classOrStyle='signup_error' />
                </li>
                <li>
                    <@spring.formPasswordInput path="user.password" attributes="placeholder='password'" />
                <@spring.showErrors separator="" classOrStyle='signup_error' />
                </li>
                <li>
                    <@spring.formPasswordInput path="user.passwordVerification" attributes="placeholder='repeat password'" />
                <@spring.showErrors separator="" classOrStyle='signup_error' />
                </li>
                <li><input type="submit" value="Sign up"/>
            </ul>
        </form>
    </div>
        <#if error=="">
        <script>
            $(document).ready(function () {
                $("#toggle_signup").click(function () {
                    $("#signup").slideToggle("slow");
                    $("#login").slideToggle("fast");
                    if ($("#toggle_signup").text() == "or sign up here") {
                        $("#toggle_signup").text("back to login");
                    } else {
                        $("#toggle_signup").text("or sign up here");
                    }
                });
            });
        </script>
        <#else>
        <script>
            $(document).ready(function () {
                $("#login").hide();
                $("#signup").slideToggle("fast");
            });
            $(document).ready(function () {
                $("#toggle_signup").click(function () {
                    $("#signup").slideToggle("slow");
                    $("#login").slideToggle("fast");
                    if ($("#toggle_signup").text() == "or sign up here") {
                        $("#toggle_signup").text("back to login");
                    } else {
                        $("#toggle_signup").text("or sign up here");
                    }
                });
            });
        </script>
        </#if>
    </@template.master>
</#macro>