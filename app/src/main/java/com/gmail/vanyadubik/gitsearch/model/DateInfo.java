package com.gmail.vanyadubik.gitsearch.model;

public class DateInfo {
    private int numberBD;
    private int numberCard;
    private String name;
    private String symptom;
    private String group;
    private int amount;
    private int done;
    private Boolean isDone;
    private int amountpigs;
    private int donepigs;
    private int deathpigs;
    private int weakpigs;

    public DateInfo(int numberBD, int numberCard, String name, String symptom, String group, int amount, int done) {
        this.numberBD = numberBD;
        this.numberCard = numberCard;
        this.name = name;
        this.symptom = symptom;
        this.group = group;
        this.amount = amount;
        this.done = done;
    }

    public DateInfo(String name, int amount, int done) {
        this.name = name;
        this.amount = amount;
        this.done = done;

    }

    public DateInfo(int numberCard, String name, String group) {
        this.numberCard = numberCard;
        this.name = name;
        this.group = group;
    }

    public DateInfo(int numberCard, String name, String symptom, String group, boolean isDone) {
        this.numberCard = numberCard;
        this.name = name;
        this.symptom = symptom;
        this.group = group;
        this.isDone = isDone;
    }

    public DateInfo(int numberCard, String name, String group, boolean isDone, int amountpigs, int donepigs, int deathpigs, int weakpigs) {
        this.numberCard = numberCard;
        this.name = name;
        this.group = group;
        this.isDone = isDone;
        this.amountpigs = amountpigs;
        this.donepigs = donepigs;
        this.deathpigs = deathpigs;
        this.weakpigs = weakpigs;
    }

    public DateInfo(String name, String symptom, String group) {
        this.name = name;
        this.symptom = symptom;
        this.group = group;
    }

    public int getNumberBD() {
        return numberBD;
    }

    public void setNumberBD(int numberBD) {
        this.numberBD = numberBD;
    }

    public int getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(int numberCard) {
        this.numberCard = numberCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDone() {
        return done;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public int getAmountpigs() {
        return amountpigs;
    }

    public void setAmountpigs(int amountpigs) {
        this.amountpigs = amountpigs;
    }

    public int getDonepigs() {
        return donepigs;
    }

    public void setDonepigs(int donepigs) {
        this.donepigs = donepigs;
    }

    public int getDeathpigs() {
        return deathpigs;
    }

    public void setDeathpigs(int deathpigs) {
        this.deathpigs = deathpigs;
    }

    public int getWeakpigs() {
        return weakpigs;
    }

    public void setWeakpigs(int weakpigs) {
        this.weakpigs = weakpigs;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public void setIsDone(Boolean isDone) {
        isDone = isDone;
    }
}
