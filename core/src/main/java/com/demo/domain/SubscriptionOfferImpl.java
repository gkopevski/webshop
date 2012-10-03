/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.domain;

import com.demo.domain.SubscriptionOffer;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.RequiredOverride;
import org.broadleafcommerce.core.offer.domain.OfferImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * @author Jeff Fischer
 */
@Entity
@Table(name = "HIBU_BILL_PERIOD")
@Inheritance(strategy= InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@AdminPresentationClass(friendlyName = "Subscription Offer")
public class SubscriptionOfferImpl extends OfferImpl implements SubscriptionOffer {

    @Column(name = "BILLING_PERIOD")
    @AdminPresentation(friendlyName = "Billing Period", order=1, group = "Billing", helpText = "Enter the number of months this promotion spans", requiredOverride = RequiredOverride.NOT_REQUIRED)
    protected Integer billingPeriod;

    @Override
    public Integer getBillingPeriod() {
        return billingPeriod;
    }

    @Override
    public void setBillingPeriod(Integer billingPeriod) {
        this.billingPeriod = billingPeriod;
    }
}