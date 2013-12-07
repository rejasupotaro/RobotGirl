package rejasupotaro.robotgirl.faker;

public enum Locale {
    EN("en.yml"),
    JA("ja.yml");

    private String mFileName;

    public String getFileName() {
        return mFileName;
    }

    private Locale(String fileName) {
        mFileName = fileName;
    }
}
