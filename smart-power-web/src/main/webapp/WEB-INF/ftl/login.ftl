<#import "template/master.ftl" as template />
<#import "/spring.ftl" as spring />
<@template.master>
<div id="login">
	<h3>Login with Username and Password</h3>

	<form name='loginForm' action='/j_spring_security_check' method='POST'>
		<table>
			<tr>
				<td><label for="username">Username: </label></td>
				<td><input type='text' id="username" name='username' value=''></td>
			</tr>
			<tr>
				<td><label for="password">Password: </label></td>
				<td><input type='password' id="password" name='password'/></td>
			</tr>
			<tr>
				<td colspan='2'><input name="submit" type="submit" value="Login"/></td>
			</tr>
		</table>
	</form>
</div>
<div id="signup">
	<h3>Signup</h3>

	<form name='loginForm' action='/users' method='POST'>
		<dl>
			<dt>E-Mail:</dt>
			<dd><@spring.formInput  "user.username" />
			<dd><@spring.showErrors "<br>" 'error' />
			<dt></dt>
			<dt>Password:</dt>
			<dd><@spring.formPasswordInput "user.password" />
			<dd><@spring.showErrors "<br>" 'error'/>
			<dt>Password verification:</dt>
			<dd><@spring.formPasswordInput "user.passwordVerification"/>
			<dd><@spring.showErrors "<br>" />
			<dd><input type="submit" value="Create"/>
		</dl>
	</form>

</div>
</@template.master>
