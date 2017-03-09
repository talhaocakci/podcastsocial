/**
 * PodcastModern API
 * PodcastModern API documentation
 * <p>
 * OpenAPI spec version: 0.0.1
 * <p>
 * <p>
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.javathlon.apiclient.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;


/**
 * SubscriptionDTO
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-24T13:50:52.801+04:00")
public class SubscriptionDTO {
    @SerializedName("applicationId")
    private Long applicationId = null;

    @SerializedName("createdBy")
    private String createdBy = null;

    @SerializedName("createdDate")
    private DateTime createdDate = null;

    @SerializedName("currency")
    private String currency = null;

    @SerializedName("id")
    private Long id = null;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy = null;

    @SerializedName("lastModifiedDate")
    private DateTime lastModifiedDate = null;

    @SerializedName("memberType")
    private String memberType = null;

    @SerializedName("payload")
    private String payload = null;

    @SerializedName("price")
    private Float price = null;

    @SerializedName("refreshCount")
    private Long refreshCount = null;

    @SerializedName("refreshPeriod")
    private String refreshPeriod = null;

    @SerializedName("skuName")
    private String skuName = null;

    @SerializedName("subscriptionDate")
    private LocalDate subscriptionDate = null;

    @SerializedName("subscriptionId")
    private Long subscriptionId = null;

    @SerializedName("subscriptionItem")
    private SubscriptionItem subscriptionItem = null;

    @SerializedName("trialEndDate")
    private LocalDate trialEndDate = null;

    @SerializedName("userId")
    private Long userId = null;

    public SubscriptionDTO applicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    /**
     * Get applicationId
     * @return applicationId
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public SubscriptionDTO createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Get createdBy
     * @return createdBy
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public SubscriptionDTO createdDate(DateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    /**
     * Get createdDate
     * @return createdDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public SubscriptionDTO currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Get currency
     * @return currency
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public SubscriptionDTO id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     * @return id
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionDTO lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    /**
     * Get lastModifiedBy
     * @return lastModifiedBy
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public SubscriptionDTO lastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    /**
     * Get lastModifiedDate
     * @return lastModifiedDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public SubscriptionDTO memberType(String memberType) {
        this.memberType = memberType;
        return this;
    }

    /**
     * Get memberType
     * @return memberType
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public SubscriptionDTO payload(String payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Get payload
     * @return payload
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public SubscriptionDTO price(Float price) {
        this.price = price;
        return this;
    }

    /**
     * Get price
     * @return price
     **/
    @ApiModelProperty(example = "null", value = "")
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public SubscriptionDTO refreshCount(Long refreshCount) {
        this.refreshCount = refreshCount;
        return this;
    }

    /**
     * Get refreshCount
     * @return refreshCount
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(Long refreshCount) {
        this.refreshCount = refreshCount;
    }

    public SubscriptionDTO refreshPeriod(String refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
        return this;
    }

    /**
     * Get refreshPeriod
     * @return refreshPeriod
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(String refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public SubscriptionDTO skuName(String skuName) {
        this.skuName = skuName;
        return this;
    }

    /**
     * Get skuName
     * @return skuName
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public SubscriptionDTO subscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
        return this;
    }

    /**
     * Get subscriptionDate
     * @return subscriptionDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public LocalDate getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public SubscriptionDTO subscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    /**
     * Get subscriptionId
     * @return subscriptionId
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public SubscriptionDTO subscriptionItem(SubscriptionItem subscriptionItem) {
        this.subscriptionItem = subscriptionItem;
        return this;
    }

    /**
     * Get subscriptionItem
     * @return subscriptionItem
     **/
    @ApiModelProperty(example = "null", value = "")
    public SubscriptionItem getSubscriptionItem() {
        return subscriptionItem;
    }

    public void setSubscriptionItem(SubscriptionItem subscriptionItem) {
        this.subscriptionItem = subscriptionItem;
    }

    public SubscriptionDTO trialEndDate(LocalDate trialEndDate) {
        this.trialEndDate = trialEndDate;
        return this;
    }

    /**
     * Get trialEndDate
     * @return trialEndDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public LocalDate getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(LocalDate trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public SubscriptionDTO userId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Get userId
     * @return userId
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubscriptionDTO subscriptionDTO = (SubscriptionDTO) o;
        return Objects.equals(this.applicationId, subscriptionDTO.applicationId) &&
                Objects.equals(this.createdBy, subscriptionDTO.createdBy) &&
                Objects.equals(this.createdDate, subscriptionDTO.createdDate) &&
                Objects.equals(this.currency, subscriptionDTO.currency) &&
                Objects.equals(this.id, subscriptionDTO.id) &&
                Objects.equals(this.lastModifiedBy, subscriptionDTO.lastModifiedBy) &&
                Objects.equals(this.lastModifiedDate, subscriptionDTO.lastModifiedDate) &&
                Objects.equals(this.memberType, subscriptionDTO.memberType) &&
                Objects.equals(this.payload, subscriptionDTO.payload) &&
                Objects.equals(this.price, subscriptionDTO.price) &&
                Objects.equals(this.refreshCount, subscriptionDTO.refreshCount) &&
                Objects.equals(this.refreshPeriod, subscriptionDTO.refreshPeriod) &&
                Objects.equals(this.skuName, subscriptionDTO.skuName) &&
                Objects.equals(this.subscriptionDate, subscriptionDTO.subscriptionDate) &&
                Objects.equals(this.subscriptionId, subscriptionDTO.subscriptionId) &&
                Objects.equals(this.subscriptionItem, subscriptionDTO.subscriptionItem) &&
                Objects.equals(this.trialEndDate, subscriptionDTO.trialEndDate) &&
                Objects.equals(this.userId, subscriptionDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, createdBy, createdDate, currency, id, lastModifiedBy, lastModifiedDate, memberType, payload, price, refreshCount, refreshPeriod, skuName, subscriptionDate, subscriptionId, subscriptionItem, trialEndDate, userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SubscriptionDTO {\n");

        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    memberType: ").append(toIndentedString(memberType)).append("\n");
        sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    refreshCount: ").append(toIndentedString(refreshCount)).append("\n");
        sb.append("    refreshPeriod: ").append(toIndentedString(refreshPeriod)).append("\n");
        sb.append("    skuName: ").append(toIndentedString(skuName)).append("\n");
        sb.append("    subscriptionDate: ").append(toIndentedString(subscriptionDate)).append("\n");
        sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
        sb.append("    subscriptionItem: ").append(toIndentedString(subscriptionItem)).append("\n");
        sb.append("    trialEndDate: ").append(toIndentedString(trialEndDate)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

