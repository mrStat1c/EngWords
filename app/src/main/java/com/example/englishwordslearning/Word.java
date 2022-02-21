package com.example.englishwordslearning;

import java.time.LocalDateTime;

public class Word {

    private String eng;
    private String rus;
    private String transcription;
    private boolean needUpdate;
    private String tags;
    private String zone;
    private LocalDateTime lastShow;

    public Word(String eng, String rus, String transcription, boolean needUpdate, String tags) {
        this.eng = eng;
        this.rus = rus;
        this.transcription = transcription;
        this.needUpdate = needUpdate;
        this.tags = tags;
    }

    public Word(String eng, String rus, String transcription, String tags, String zone, LocalDateTime lastShow) {
        this.eng = eng;
        this.rus = rus;
        this.transcription = transcription;
        this.tags = tags;
        this.zone = zone;
        this.lastShow = lastShow;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getRus() {
        return rus;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public LocalDateTime getLastShow() {
        return lastShow;
    }

    public void setLastShow(LocalDateTime lastShow) {
        this.lastShow = lastShow;
    }
}
