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
 * PurchaseDTO
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-24T13:50:52.801+04:00")
public class PurchaseDTO {
    @SerializedName("applicationId")
    private Long applicationId = null;

    @SerializedName("channel")
    private String channel = null;

    @SerializedName("createdBy")
    private String createdBy = null;

    @SerializedName("createdDate")
    private DateTime createdDate = null;

    @SerializedName("id")
    private Long id = null;

    @SerializedName("itemId")
    private Long itemId = null;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy = null;

    @SerializedName("lastModifiedDate")
    private DateTime lastModifiedDate = null;

    @SerializedName("paymentType")
    private String paymentType = null;

    @SerializedName("price")
    private Double price = null;

    @SerializedName("purchaseDate")
    private LocalDate purchaseDate = null;

    @SerializedName("subscriberId")
    private Long subscriberId = null;

    public PurchaseDTO applicationId(Long applicationId) {
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

    public PurchaseDTO channel(String channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Get channel
     * @return channel
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public PurchaseDTO createdBy(String createdBy) {
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

    public PurchaseDTO createdDate(DateTime createdDate) {
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

    public PurchaseDTO id(Long id) {
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

    public PurchaseDTO itemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    /**
     * Get itemId
     * @return itemId
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public PurchaseDTO lastModifiedBy(String lastModifiedBy) {
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

    public PurchaseDTO lastModifiedDate(DateTime lastModifiedDate) {
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

    public PurchaseDTO paymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    /**
     * Get paymentType
     * @return paymentType
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public PurchaseDTO price(Double price) {
        this.price = price;
        return this;
    }

    /**
     * Get price
     * @return price
     **/
    @ApiModelProperty(example = "null", value = "")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PurchaseDTO purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    /**
     * Get purchaseDate
     * @return purchaseDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public PurchaseDTO subscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
        return this;
    }

    /**
     * Get subscriberId
     * @return subscriberId
     **/
    @ApiModelProperty(example = "null", value = "")
    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseDTO purchaseDTO = (PurchaseDTO) o;
        return Objects.equals(this.applicationId, purchaseDTO.applicationId) &&
                Objects.equals(this.channel, purchaseDTO.channel) &&
                Objects.equals(this.createdBy, purchaseDTO.createdBy) &&
                Objects.equals(this.createdDate, purchaseDTO.createdDate) &&
                Objects.equals(this.id, purchaseDTO.id) &&
                Objects.equals(this.itemId, purchaseDTO.itemId) &&
                Objects.equals(this.lastModifiedBy, purchaseDTO.lastModifiedBy) &&
                Objects.equals(this.lastModifiedDate, purchaseDTO.lastModifiedDate) &&
                Objects.equals(this.paymentType, purchaseDTO.paymentType) &&
                Objects.equals(this.price, purchaseDTO.price) &&
                Objects.equals(this.purchaseDate, purchaseDTO.purchaseDate) &&
                Objects.equals(this.subscriberId, purchaseDTO.subscriberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, channel, createdBy, createdDate, id, itemId, lastModifiedBy, lastModifiedDate, paymentType, price, purchaseDate, subscriberId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PurchaseDTO {\n");

        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
        sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
        sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    paymentType: ").append(toIndentedString(paymentType)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    purchaseDate: ").append(toIndentedString(purchaseDate)).append("\n");
        sb.append("    subscriberId: ").append(toIndentedString(subscriberId)).append("\n");
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
