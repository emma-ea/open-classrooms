package com.oddlycoder.ocr.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class WorldClock {

    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("currentDateTime")
    @Expose
    private String currentDateTime;
    @SerializedName("utcOffset")
    @Expose
    private String utcOffset;
    @SerializedName("isDayLightSavingsTime")
    @Expose
    private Boolean isDayLightSavingsTime;
    @SerializedName("dayOfTheWeek")
    @Expose
    private String dayOfTheWeek;
    @SerializedName("timeZoneName")
    @Expose
    private String timeZoneName;
    @SerializedName("currentFileTime")
    @Expose
    private Long currentFileTime;
    @SerializedName("ordinalDate")
    @Expose
    private String ordinalDate;
    @SerializedName("serviceResponse")
    @Expose
    private Object serviceResponse;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    public Boolean getIsDayLightSavingsTime() {
        return isDayLightSavingsTime;
    }

    public void setIsDayLightSavingsTime(Boolean isDayLightSavingsTime) {
        this.isDayLightSavingsTime = isDayLightSavingsTime;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    public Long getCurrentFileTime() {
        return currentFileTime;
    }

    public void setCurrentFileTime(Long currentFileTime) {
        this.currentFileTime = currentFileTime;
    }

    public String getOrdinalDate() {
        return ordinalDate;
    }

    public void setOrdinalDate(String ordinalDate) {
        this.ordinalDate = ordinalDate;
    }

    public Object getServiceResponse() {
        return serviceResponse;
    }

    public void setServiceResponse(Object serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                $id,
                currentDateTime,
                utcOffset,
                isDayLightSavingsTime,
                dayOfTheWeek,
                timeZoneName,
                currentFileTime,
                ordinalDate,
                serviceResponse
        );
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        WorldClock wc = (WorldClock) obj;
        return Objects.equals($id, wc.$id) &&
                Objects.equals(currentDateTime, wc.currentDateTime) &&
                Objects.equals(utcOffset, wc.utcOffset) &&
                Objects.equals(isDayLightSavingsTime, wc.isDayLightSavingsTime) &&
                Objects.equals(dayOfTheWeek, wc.dayOfTheWeek) &&
                Objects.equals(timeZoneName, wc.timeZoneName) &&
                Objects.equals(currentFileTime, wc.currentFileTime) &&
                Objects.equals(ordinalDate, wc.ordinalDate) &&
                Objects.equals(serviceResponse, wc.serviceResponse);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "id: %s currentDateTime: %s dayOfWeek %s",
                get$id(),
                getCurrentDateTime(),
                getDayOfTheWeek());
    }
}
