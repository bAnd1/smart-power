package edu.hm.smartpower.domain;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Entity
public class User implements UserDetails {

    @Id
	@Email
	@Size(min = 5, max = 255)
    private String username;
    @NotNull
    @Size(min = 5, max = 255)
    private String password;
	@Transient
    private String passwordVerification;
	@Min(1) @Max(20)
	private Integer personsInHousehold;
	@DecimalMin("0.01") @Digits(integer = 3, fraction = 2)
	private BigDecimal pricePerKwh = new BigDecimal("0.25");
	@Min(1)
	private Integer gramPerKwh = 600;
	private Boolean notificationsActivated;
	@Min(1) @Max(1000)
	private Integer maxDeviationFromAverage;
	@Temporal(TemporalType.DATE)
	private Date lastDeviationNotification;
	@Min(1)
	private Integer maxUsagePerDay;
	@Temporal(TemporalType.DATE)
	private Date lastMaxUsageNotification;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public User(String username, String password) {
		this.username = username;
		this.password=password;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	public Integer getPersonsInHousehold() {
		return personsInHousehold;
	}

	public void setPersonsInHousehold(Integer personsInHousehold) {
		this.personsInHousehold = personsInHousehold;
	}

	public BigDecimal getPricePerKwh() {
		return pricePerKwh;
	}

	public void setPricePerKwh(BigDecimal pricePerKwh) {
		this.pricePerKwh = pricePerKwh;
	}

	public Integer getGramPerKwh() {
		return gramPerKwh;
	}

	public void setGramPerKwh(Integer gramPerKwh) {
		this.gramPerKwh = gramPerKwh;
	}

	public Integer getMaxDeviationFromAverage() {
		return maxDeviationFromAverage;
	}

	public void setMaxDeviationFromAverage(Integer maxDeviationFromAverage) {
		this.maxDeviationFromAverage = maxDeviationFromAverage;
	}

	public Integer getMaxUsagePerDay() {
		return maxUsagePerDay;
	}

	public void setMaxUsagePerDay(Integer maxUsagePerDay) {
		this.maxUsagePerDay = maxUsagePerDay;
	}

	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	public String getPasswordVerification() {
		return passwordVerification;
	}

	public void setPasswordVerification(String passwordVerification) {
		this.passwordVerification = passwordVerification;
	}

	public Boolean getNotificationsActivated() {
		return notificationsActivated;
	}

	public void setNotificationsActivated(Boolean notificationsActivated) {
		this.notificationsActivated = notificationsActivated;
	}

	public Date getLastDeviationNotification() {
		return lastDeviationNotification;
	}

	public void setLastDeviationNotification(Date lastDeviationNotification) {
		this.lastDeviationNotification = lastDeviationNotification;
	}

	public Date getLastMaxUsageNotification() {
		return lastMaxUsageNotification;
	}

	public void setLastMaxUsageNotification(Date lastMaxUsageNotification) {
		this.lastMaxUsageNotification = lastMaxUsageNotification;
	}
}
