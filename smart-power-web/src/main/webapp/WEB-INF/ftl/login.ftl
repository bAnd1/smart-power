<#import "template/master.ftl" as template />
<#import "/spring.ftl" as spring />
<@template.master title="Login">
<div id="login" class="login-list">
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

<div id="toggle_signup">
	<span id="signupText">or sign up here</span>
	<span id="loginText">back to login</span>
</div>

<div id="signup" class="login-list">
	<p>Signup</p>

	<form name='loginForm' action='/users' method='POST'>
		<ul>
			<li>
				<@spring.formInput path="user.username" attributes="placeholder='username'" />
                <@spring.showErrors separator="" classOrStyle='error' />
			</li>
			<li>
				<@spring.formPasswordInput path="user.password" attributes="placeholder='password'" />
                <@spring.showErrors separator="" classOrStyle='error' />
			</li>
			<li>
				<@spring.formPasswordInput path="user.passwordVerification" attributes="placeholder='repeat password'" />
                <@spring.showErrors separator="" classOrStyle='error' />
			</li>
			<li><input type="submit" value="Sign up"/>
		</ul>
	</form>
</div>

<script>
	var login = true;
	$(document).ready(function () {
		toggleText();
		$("#signup").hide();
	});
		<@spring.bind 'user.*'/>
		<#if spring.status.error>
		login = false;
		$(document).ready(function () {
			$("#login").hide();
			$("#signup").show();
		});
		</#if>

	$("#toggle_signup").click(function () {
		login = !login;
		$("#signup").slideToggle("slow");
		$("#login").slideToggle("fast");
		toggleText();
	});
	function toggleText() {
		$("#signupText").toggle(login);
		$("#loginText").toggle(!login);
	}

</script>

</@template.master>