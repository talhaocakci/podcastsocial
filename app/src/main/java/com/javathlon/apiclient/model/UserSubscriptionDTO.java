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
 * UserSubscriptionDTO
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-24T13:50:52.801+04:00")
public class UserSubscriptionDTO {
    @SerializedName("active")
    private Boolean active = null;

    @SerializedName("autoRenew")
    private Boolean autoRenew = null;

    @SerializedName("createdBy")
    private String createdBy = null;

    @SerializedName("createdDate")
    private DateTime createdDate = null;

    @SerializedName("endDate")
    private LocalDate endDate = null;

    @SerializedName("id")
    private Long id = null;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy = null;

    @SerializedName("lastModifiedDate")
    private DateTime lastModifiedDate = null;

    @SerializedName("startDate")
    private LocalDate startDate = null;

    public UserSubscriptionDTO active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Get active
     * @return active
     **/
    @ApiModelProperty(example = "null", value = "")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserSubscriptionDTO autoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
        return this;
    }

    /**
     * Get autoRenew
     * @return autoRenew
     **/
    @ApiModelProperty(example = "null", value = "")
    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public UserSubscriptionDTO createdBy(String createdBy) {
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

    public UserSubscriptionDTO createdDate(DateTime createdDate) {
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

    public UserSubscriptionDTO endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * Get endDate
     * @return endDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public UserSubscriptionDTO id(Long id) {
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

    public UserSubscriptionDTO lastModifiedBy(String lastModifiedBy) {
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

    public UserSubscriptionDTO lastModifiedDate(DateTime lastModifiedDate) {
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

    public UserSubscriptionDTO startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Get startDate
     * @return startDate
     **/
    @ApiModelProperty(example = "null", value = "")
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSubscriptionDTO userSubscriptionDTO = (UserSubscriptionDTO) o;
        return Objects.equals(this.active, userSubscriptionDTO.active) &&
                Objects.equals(this.autoRenew, userSubscriptionDTO.autoRenew) &&
                Objects.equals(this.createdBy, userSubscriptionDTO.createdBy) &&
                Objects.equals(this.createdDate, userSubscriptionDTO.createdDate) &&
                Objects.equals(this.endDate, userSubscriptionDTO.endDate) &&
                Objects.equals(this.id, userSubscriptionDTO.id) &&
                Objects.equals(this.lastModifiedBy, userSubscriptionDTO.lastModifiedBy) &&
                Objects.equals(this.lastModifiedDate, userSubscriptionDTO.lastModifiedDate) &&
                Objects.equals(this.startDate, userSubscriptionDTO.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, autoRenew, createdBy, createdDate, endDate, id, lastModifiedBy, lastModifiedDate, startDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserSubscriptionDTO {\n");

        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    autoRenew: ").append(toIndentedString(autoRenew)).append("\n");
        sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
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
