<#import "template/master.ftl" as template />
<#import "/spring.ftl" as spring />
<@template.master>
<div id="settings" class="login-list">
	<p>Settings</p>

	<form name='settingsForm' action='/user/settings' method='POST'>
		<ul>
			<li>Anzahl der Personen im Haushalt:</li>
			<li><@spring.formInput  path="user.personsInHousehold" fieldType='number'/> </li>
			<li><@spring.showErrors "<br>" 'error' /></li>
			<li>Preis pro kWh:</li>
			<li><@spring.formInput path="user.pricePerKwh" attributes="step='0.01'" fieldType='number'/> </li>
			<li><@spring.showErrors "<br>" 'error'/> </li>
			<li>Gramm CO2 pro kWh:</li>
			<li><@spring.formInput path="user.gramPerKwh" fieldType='number'/> </li>
			<li><@spring.showErrors "<br>" 'error'/> </li>
			<li><input type="submit" value="Save"/></li>
		</ul>
	</form>

</div>
</@template.master>
