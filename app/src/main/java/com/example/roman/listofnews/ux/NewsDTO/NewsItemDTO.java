package com.example.roman.listofnews.ux.NewsDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class NewsItemDTO {

    @SerializedName("subsection")
    @Expose
    private String subsection;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abstract")
    @Expose
    private String _abstract;
    @SerializedName("updated_date")
    @Expose
    private Date updatedDate;
    /*@SerializedName("multimedia")
    @Expose
    private MultimediaDTO multimediaUrl;*/

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getMultimediaURL () {
        return "https://static01.nyt.com/images/2019/03/29/us/00levees-02/00levees-02-thumbStandard.jpg";
    }
    /*public String getMultimediaURL () {
        return multimediaUrl.getOriginalSize().getUrl();
    }

    public void setMultimediaURL (MultimediaDTO multimediaUrl) {
        this.multimediaUrl = multimediaUrl;
    }
*/
}
