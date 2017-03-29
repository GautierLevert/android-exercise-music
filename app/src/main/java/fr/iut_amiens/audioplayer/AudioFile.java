package fr.iut_amiens.audioplayer;

public enum AudioFile {
    GOING_HIGHER("Going Higher", R.raw.goinghigher),
    HAPPINESS("Happiness", R.raw.happiness),
    SCI_FI("Sci-Fi", R.raw.scifi);

    private String title;

    private int resourceId;

    AudioFile(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public AudioFile next() {
        switch (this) {
            case GOING_HIGHER:
                return AudioFile.HAPPINESS;
            case HAPPINESS:
                return AudioFile.SCI_FI;
            case SCI_FI:
                return AudioFile.GOING_HIGHER;
        }
        return null;
    }

    public AudioFile previous() {
        switch (this) {
            case GOING_HIGHER:
                return AudioFile.SCI_FI;
            case HAPPINESS:
                return AudioFile.GOING_HIGHER;
            case SCI_FI:
                return AudioFile.HAPPINESS;
        }
        return null;
    }
}
