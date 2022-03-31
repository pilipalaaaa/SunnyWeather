package com.sunnyweather.android.logic.model;

import com.google.gson.annotations.SerializedName;
public class RealtimeResponse {
    private String status;
    private Result result;
    private String error = new String();

    public String getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public class Result{
        private Realtime realtime;

        public Realtime getRealtime() {
            return realtime;
        }

        public void setRealtime(Realtime realtime) {
            this.realtime = realtime;
        }
    }
    public class Realtime{
        private String skycon;
        private Float temperature;
        @SerializedName("air_quality")
        private AirQuality airQuality;

        public String getSkycon() {
            return skycon;
        }

        public void setSkycon(String skycon) {
            this.skycon = skycon;
        }

        public Float getTemperature() {
            return temperature;
        }

        public void setTemperature(Float temperature) {
            this.temperature = temperature;
        }

        public AirQuality getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQuality airQuality) {
            this.airQuality = airQuality;
        }
    }
    public class AirQuality{
        private Aqi aqi;

        public Aqi getAqi() {
            return aqi;
        }

        public void setAqi(Aqi aqi) {
            this.aqi = aqi;
        }
    }
    public class Aqi{
        private Float chn;

        public Float getChn() {
            return chn;
        }

        public void setChn(Float chn) {
            this.chn = chn;
        }
    }
}
