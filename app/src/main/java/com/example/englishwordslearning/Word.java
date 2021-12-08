package com.example.englishwordslearning;

public class Word {

    private String eng;
    private String rus;
    private String transcription;
    private String tags;
    private String zone;
    private String lastShow;

    public Word(String eng, String rus, String transcription, String tags) {
        this.eng = eng;
        this.rus = rus;
        this.transcription = transcription;
        this.tags = tags;
    }

    public Word(String eng, String rus, String transcription, String tags, String zone, String lastShow) {
        this.eng = eng;
        this.rus = rus;
        this.transcription = transcription;
        this.tags = tags;
        this.zone = zone;
        this.lastShow = lastShow;
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

    public String getLastShow() {
        return lastShow;
    }

    public void setLastShow(String lastShow) {
        this.lastShow = lastShow;
    }
}
