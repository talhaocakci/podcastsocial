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

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;


/**
 * KeyAndPasswordVM
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-24T13:50:52.801+04:00")
public class KeyAndPasswordVM {
    @SerializedName("key")
    private String key = null;

    @SerializedName("newPassword")
    private String newPassword = null;

    public KeyAndPasswordVM key(String key) {
        this.key = key;
        return this;
    }

    /**
     * Get key
     * @return key
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KeyAndPasswordVM newPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    /**
     * Get newPassword
     * @return newPassword
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyAndPasswordVM keyAndPasswordVM = (KeyAndPasswordVM) o;
        return Objects.equals(this.key, keyAndPasswordVM.key) &&
                Objects.equals(this.newPassword, keyAndPasswordVM.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, newPassword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class KeyAndPasswordVM {\n");

        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
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
