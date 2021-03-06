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

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;


/**
 * PodcastDTO
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-24T13:50:52.801+04:00")
public class PodcastDTO {
    @SerializedName("allowedForPublic")
    private Boolean allowedForPublic = null;

    @SerializedName("author")
    private String author = null;

    @SerializedName("bucketName")
    private String bucketName = null;

    @SerializedName("coverImage")
    private String coverImage = null;

    @SerializedName("coverVideo")
    private String coverVideo = null;

    @SerializedName("createdBy")
    private String createdBy = null;

    @SerializedName("createdDate")
    private DateTime createdDate = null;

    @SerializedName("currentItemCount")
    private Integer currentItemCount = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("id")
    private Long id = null;

    @SerializedName("imageUrl")
    private String imageUrl = null;

    @SerializedName("itunesUrl")
    private String itunesUrl = null;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy = null;

    @SerializedName("lastModifiedDate")
    private DateTime lastModifiedDate = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("otherRssUrl")
    private String otherRssUrl = null;

    @SerializedName("publisher")
    private String publisher = null;

    public PodcastDTO allowedForPublic(Boolean allowedForPublic) {
        this.allowedForPublic = allowedForPublic;
        return this;
    }

    /**
     * Get allowedForPublic
     * @return allowedForPublic
     **/
    @ApiModelProperty(example = "null", value = "")
    public Boolean getAllowedForPublic() {
        return allowedForPublic;
    }

    public void setAllowedForPublic(Boolean allowedForPublic) {
        this.allowedForPublic = allowedForPublic;
    }

    public PodcastDTO author(String author) {
        this.author = author;
        return this;
    }

    /**
     * Get author
     * @return author
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public PodcastDTO bucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    /**
     * Get bucketName
     * @return bucketName
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public PodcastDTO coverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    /**
     * Get coverImage
     * @return coverImage
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public PodcastDTO coverVideo(String coverVideo) {
        this.coverVideo = coverVideo;
        return this;
    }

    /**
     * Get coverVideo
     * @return coverVideo
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getCoverVideo() {
        return coverVideo;
    }

    public void setCoverVideo(String coverVideo) {
        this.coverVideo = coverVideo;
    }

    public PodcastDTO createdBy(String createdBy) {
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

    public PodcastDTO createdDate(DateTime createdDate) {
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

    public PodcastDTO currentItemCount(Integer currentItemCount) {
        this.currentItemCount = currentItemCount;
        return this;
    }

    /**
     * Get currentItemCount
     * @return currentItemCount
     **/
    @ApiModelProperty(example = "null", value = "")
    public Integer getCurrentItemCount() {
        return currentItemCount;
    }

    public void setCurrentItemCount(Integer currentItemCount) {
        this.currentItemCount = currentItemCount;
    }

    public PodcastDTO description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     * @return description
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PodcastDTO id(Long id) {
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

    public PodcastDTO imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * Get imageUrl
     * @return imageUrl
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PodcastDTO itunesUrl(String itunesUrl) {
        this.itunesUrl = itunesUrl;
        return this;
    }

    /**
     * Get itunesUrl
     * @return itunesUrl
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getItunesUrl() {
        return itunesUrl;
    }

    public void setItunesUrl(String itunesUrl) {
        this.itunesUrl = itunesUrl;
    }

    public PodcastDTO lastModifiedBy(String lastModifiedBy) {
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

    public PodcastDTO lastModifiedDate(DateTime lastModifiedDate) {
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

    public PodcastDTO name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PodcastDTO otherRssUrl(String otherRssUrl) {
        this.otherRssUrl = otherRssUrl;
        return this;
    }

    /**
     * Get otherRssUrl
     * @return otherRssUrl
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getOtherRssUrl() {
        return otherRssUrl;
    }

    public void setOtherRssUrl(String otherRssUrl) {
        this.otherRssUrl = otherRssUrl;
    }

    public PodcastDTO publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    /**
     * Get publisher
     * @return publisher
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PodcastDTO podcastDTO = (PodcastDTO) o;
        return Objects.equals(this.allowedForPublic, podcastDTO.allowedForPublic) &&
                Objects.equals(this.author, podcastDTO.author) &&
                Objects.equals(this.bucketName, podcastDTO.bucketName) &&
                Objects.equals(this.coverImage, podcastDTO.coverImage) &&
                Objects.equals(this.coverVideo, podcastDTO.coverVideo) &&
                Objects.equals(this.createdBy, podcastDTO.createdBy) &&
                Objects.equals(this.createdDate, podcastDTO.createdDate) &&
                Objects.equals(this.currentItemCount, podcastDTO.currentItemCount) &&
                Objects.equals(this.description, podcastDTO.description) &&
                Objects.equals(this.id, podcastDTO.id) &&
                Objects.equals(this.imageUrl, podcastDTO.imageUrl) &&
                Objects.equals(this.itunesUrl, podcastDTO.itunesUrl) &&
                Objects.equals(this.lastModifiedBy, podcastDTO.lastModifiedBy) &&
                Objects.equals(this.lastModifiedDate, podcastDTO.lastModifiedDate) &&
                Objects.equals(this.name, podcastDTO.name) &&
                Objects.equals(this.otherRssUrl, podcastDTO.otherRssUrl) &&
                Objects.equals(this.publisher, podcastDTO.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedForPublic, author, bucketName, coverImage, coverVideo, createdBy, createdDate, currentItemCount, description, id, imageUrl, itunesUrl, lastModifiedBy, lastModifiedDate, name, otherRssUrl, publisher);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PodcastDTO {\n");

        sb.append("    allowedForPublic: ").append(toIndentedString(allowedForPublic)).append("\n");
        sb.append("    author: ").append(toIndentedString(author)).append("\n");
        sb.append("    bucketName: ").append(toIndentedString(bucketName)).append("\n");
        sb.append("    coverImage: ").append(toIndentedString(coverImage)).append("\n");
        sb.append("    coverVideo: ").append(toIndentedString(coverVideo)).append("\n");
        sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    currentItemCount: ").append(toIndentedString(currentItemCount)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
        sb.append("    itunesUrl: ").append(toIndentedString(itunesUrl)).append("\n");
        sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    otherRssUrl: ").append(toIndentedString(otherRssUrl)).append("\n");
        sb.append("    publisher: ").append(toIndentedString(publisher)).append("\n");
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

