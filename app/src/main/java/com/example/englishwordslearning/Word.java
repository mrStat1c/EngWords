package com.example.englishwordslearning;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Инкапсулирует слово
 */
public class Word {

    private String eng;
    private String rus;
    private String transcription;
    private UpdateAction updateAction;
    private String tags;
    private String zone;
    private LocalDateTime lastShow;

    public enum UpdateAction {
        UPDATE("u"),
        REMOVE("r");

        private String symbol;

        UpdateAction(String symbol) {
            this.symbol = symbol;
        }

        public static boolean hasValue(String value) {
            return Arrays.stream(UpdateAction.values())
                    .anyMatch(element -> element.symbol.equals(value));
        }

    }

    public Word(String eng, String rus, String transcription, UpdateAction updateAction, String tags) {
        this.eng = eng;
        this.rus = rus;
        this.transcription = transcription;
        this.updateAction = updateAction;
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

    public UpdateAction getUpdateAction() {
        return updateAction;
    }

    public void setUpdateAction(UpdateAction updateAction) {
        this.updateAction = updateAction;
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
