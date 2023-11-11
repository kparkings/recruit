package com.arenella.recruit.recruiters.entities;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.recruiters.beans.CreditBasedSubscription;
import com.arenella.recruit.recruiters.beans.FirstGenRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.PaidPeriodRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscription;

/**
* Entity Representation of a Recruiters Subscription
* @author K Parkings
*/
@Entity
@Table(schema="recruiter", name="recruiter_subscriptions")
public class RecruiterSubscriptionEntity {

	@Id
	@Column(name="subscription_id")
	private UUID 					subscriptionId;
	
	@Column(name="recruiter_id")
	private String 					recruiterId;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private subscription_type		type;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private subscription_status		status;
	
	@Column(name="created")
	private LocalDateTime 			created;
	
	@Column(name="activated_date")
	private LocalDateTime 			activatedDate;
	
	@Column(name="current_subscription")
	private boolean					currentSubscription;
	
	/**
	* Constructor for Hibernate 
	*/
	@SuppressWarnings("unused")
	private RecruiterSubscriptionEntity() {
		//For Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterSubscriptionEntity(RecruiterSubscriptionEntityBuilder builder) {
	
		this.subscriptionId 		= builder.subscriptionId;
		this.created				= builder.created;
		this.activatedDate			= builder.activatedDate;
		this.recruiterId			= builder.recruiterId;
		this.type					= builder.type;
		this.status					= builder.status;
		this.currentSubscription 	= builder.currentSubscription;
		
	}
	
	/**
	* Returns the unique Id of the Subscription
	* @return Unique Id of the subscription
	*/
	public UUID getSubscriptionId() {
		return this.subscriptionId;
	}

	/**
	* Returns when the Subscriptions was created
	* @return Creation Date
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}

	/**
	* Returns when the Subscription is considered active from
	* @return When the subscriptions becomes active
	*/
	public LocalDateTime getActivatedDate() {
		return this.activatedDate;
	}

	/**
	* Returns the uniqueId of the Recruiter the Subscription is
	* associated with 
	* @return Id of Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}

	/**
	* Returns the type of the Subscription
	* @return Type of Subscription
	*/
	public subscription_type getType() {
		return this.type;
	}

	/**
	* Returns the current status of the Subscription
	* @return
	*/
	public subscription_status getStatus() {
		return this.status;
	}
	
	/**
	* Returns whether or not the subscription is the current subscription
	* @return
	*/
	public boolean isCurrentSubscription() {
		return this.currentSubscription;
	}
	
	/**
	* Sets the unique id of the Subscription
	* @param subscriptionId - Unique Id of the Subscription
	*/
	public void setSubscriptionId(UUID subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	/**
	* Sets the unique id of the Recruiter
	* @param recruiterId - Unique Id of the Recruiter
	*/
	public void setRecruiterId(String recruiterId) {
		this.recruiterId = recruiterId;
	}
	
	/**
	* Sets the type of the Subscription
	* @param type - Subscription type
	*/
	public void setType(subscription_type type) {
		this.type = type;
	}
	
	/**
	* Sets whether or the not Subscription is the current subscription
	* @param currentSubscription - whether or not the Subscription is the current Subscription
	*/
	public void setCurrentSubscription(boolean currentSubscription) {
		this.currentSubscription = currentSubscription;
	}
	
	/**
	* Sets the Status of the Subscription
	* @param status - Subscription Status
	*/
	public void setStatus(subscription_status status) {
		this.status = status;
	}
	
	/**
	* Sets when the Subscription was created
	* @param created - When the Subscription was created
	*/
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	/**
	* Sets when the Subscription was activated or will become active
	* @param activatedDate - activation date of the Subscription
	*/
	public void setActivatedDate(LocalDateTime activatedDate) {
		this.activatedDate = activatedDate;
	}
	
	/**
	* Returns a Builder for the RecruiterSubscriptionEntityBuilder class
	* @return Builder
	*/
	public static RecruiterSubscriptionEntityBuilder builder() {
		return new RecruiterSubscriptionEntityBuilder();
	}
	
	/**
	* Builder for the RecruiterSubscriptionEntityBuilder class
	* @author K Parkings
	*/
	public static class RecruiterSubscriptionEntityBuilder {
		
		private UUID 					subscriptionId;
		private String 					recruiterId;
		private LocalDateTime 			created;
		private LocalDateTime 			activatedDate;
		private subscription_status		status;
		private subscription_type		type;
		private boolean					currentSubscription;
		
		/**
		* Sets the Unique Id of the subscription
		* @param subscriptionId - Unique identifier of the subscription
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder subscriptionId(UUID subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recruiter the Subscription is associated with
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Date the Subscriptions was created
		* @param created - Creation date of the Subscription
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Status of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder status(subscription_status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the Type of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder type(subscription_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the date from which the Subscription is active
		* @param activatedDate - When the subscription becomes/became active
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder activateDate(LocalDateTime activatedDate) {
			this.activatedDate = activatedDate;
			return this;
		}
		
		/**
		* Sets whether or not the Subscription is the Recruiters current subscription 
		* @param currentSubscription - whether or not the subscription is the current subscription
		* @return Builder
		*/
		public RecruiterSubscriptionEntityBuilder currentSubscription(boolean currentSubscription) {
			this.currentSubscription = currentSubscription;
			return this;
		}
		
		/**
		* Returns an initialzied instance of FirstGenRecruiterSubscription
		* @return Initialzied instance of FirstGenRecruiterSubscription
		*/
		public RecruiterSubscriptionEntity build() {
			return new RecruiterSubscriptionEntity(this);
		}
		
	}
	
	/**
	* Converts a domain representation of a Subscription to an entity representation. Where a Managed version already exists 
	* it can be passed to the method and its attribute values will be updated instead of creating a new object
	* @param subscription	- Subscription to be converted
	* @param existingEntity - Optional existing Entity representing the Subscription to be updated
	* @return Entity representation of the Subscription
	*/
	public static RecruiterSubscriptionEntity convertToEntity(RecruiterSubscription subscription, Optional<RecruiterSubscriptionEntity> existingEntity) {
		 
		if (!existingEntity.isPresent()) {
			
			return RecruiterSubscriptionEntity
										.builder()
											.recruiterId(subscription.getRecruiterId())
											.status(subscription.getStatus())
											.activateDate(subscription.getActivatedDate())
											.created(subscription.getCreated())
											.subscriptionId(subscription.getSubscriptionId())
											.type(subscription.getType())
											.currentSubscription(subscription.isCurrentSubscription())
										.build();
			
		}
		
		RecruiterSubscriptionEntity entity = existingEntity.get();
		
		entity.setActivatedDate(subscription.getActivatedDate());
		entity.setCreated(subscription.getCreated());
		entity.setRecruiterId(subscription.getRecruiterId());
		entity.setStatus(subscription.getStatus());
		entity.setSubscriptionId(subscription.getSubscriptionId());
		entity.setType(subscription.getType());
		entity.setCurrentSubscription(subscription.isCurrentSubscription());
		
		return entity;
		
	}
	
	/**
	* Converts the Entity representation of a Subscription to the concrete implementation of 
	* the Domain interface representation based upon the correct type for the Subscription
	* @param entity - Entity to convert
	* @return Domain representation of the Subsription
	*/
	public static RecruiterSubscription convertFromEntity(RecruiterSubscriptionEntity entity) {
		switch(entity.getType()) {
			case FIRST_GEN -> {
				return FirstGenRecruiterSubscription
											.builder()
												.activateDate(entity.getActivatedDate())
												.created(entity.getCreated())
												.recruiterId(entity.getRecruiterId())
												.status(entity.getStatus())
												.subscriptionId(entity.getSubscriptionId())
												.currentSubscription(entity.isCurrentSubscription())
											.build();
			}
			case TRIAL_PERIOD -> {
				return TrialPeriodSubscription
											.builder()
												.activateDate(entity.getActivatedDate())
												.created(entity.getCreated())
												.recruiterId(entity.getRecruiterId())
												.status(entity.getStatus())
												.subscriptionId(entity.getSubscriptionId())
												.currentSubscription(entity.isCurrentSubscription())
											.build();
				
			}
			case YEAR_SUBSCRIPTION, ONE_MONTH_SUBSCRIPTION, THREE_MONTHS_SUBSCRIPTION, SIX_MONTHS_SUBSCRIPTION -> {
				return PaidPeriodRecruiterSubscription
											.builder()
												.activateDate(entity.getActivatedDate())
												.created(entity.getCreated())
												.recruiterId(entity.getRecruiterId())
												.status(entity.getStatus())
												.subscriptionId(entity.getSubscriptionId())
												.currentSubscription(entity.isCurrentSubscription())
												.type(entity.getType())
											.build();
				
			}
			case CREDIT_BASED_SUBSCRIPTION -> {
				return CreditBasedSubscription.builder()
											.activateDate(entity.getActivatedDate())
											.created(entity.getCreated())
											.recruiterId(entity.getRecruiterId())
											.status(entity.getStatus())
											.subscriptionId(entity.getSubscriptionId())
											.currentSubscription(entity.isCurrentSubscription())
										.build();
			}
			default -> {
				throw new IllegalArgumentException("Unknown type for recruiter subscription: " + entity.getType());
			}
		}
		
	}

}